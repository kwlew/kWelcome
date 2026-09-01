package dev.kwlew.kwelcome.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MessageRenderer {

    private MessageRenderer() {}

    static Renderer compatibleRenderer() {
        try {
            return nativeRenderer();
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return legacyRenderer();
        }
    }

    static Renderer legacyRenderer() {
        MiniMessage miniMessage = MiniMessage.get();
        return (message, placeholders) -> {
            Template[] templates = new Template[placeholders.length];
            for (int i = 0; i < placeholders.length; i++) {
                MessageManager.Placeholder placeholder = placeholders[i];
                templates[i] = Template.of(
                        placeholder.key(),
                        Component.text(placeholder.value())
                );
            }
            return miniMessage.parse(message, templates);
        };
    }

    static Renderer nativeRenderer() throws ReflectiveOperationException {
        // This name is assembled at runtime so Shadow does not relocate it with
        // the private 1.18-compatible MiniMessage copy bundled in the plugin.
        String className = String.join(".",
                "net", "kyori", "adventure", "text", "minimessage", "MiniMessage");
        Class<?> miniMessageClass = Class.forName(className);
        Object miniMessage = miniMessageClass.getMethod("miniMessage").invoke(null);
        Method escapeTags = miniMessageClass.getMethod("escapeTags", String.class);
        // ComponentSerializer's generic input type is erased to Object at the
        // bytecode level in both Adventure 4 and 5.
        Method deserialize = miniMessageClass.getMethod("deserialize", Object.class);

        return (message, placeholders) -> {
            String rendered = message;
            try {
                for (MessageManager.Placeholder placeholder : placeholders) {
                    String escapedValue = (String) escapeTags.invoke(
                            miniMessage,
                            placeholder.value()
                    );
                    rendered = rendered.replace(
                            "<" + placeholder.key() + ">",
                            escapedValue
                    );
                }
                return (Component) deserialize.invoke(miniMessage, rendered);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Cannot access the server MiniMessage API", e);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new IllegalStateException("The server MiniMessage parser failed", cause);
            }
        };
    }

    @FunctionalInterface
    interface Renderer {
        Component render(String message, MessageManager.Placeholder... placeholders);
    }
}
