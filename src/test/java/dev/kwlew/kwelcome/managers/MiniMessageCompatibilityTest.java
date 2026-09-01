package dev.kwlew.kwelcome.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiniMessageCompatibilityTest {

    @Test
    void rendersConfiguredMessagesAndEscapesPlaceholderTags() throws ReflectiveOperationException {
        MessageRenderer.Renderer renderer = Boolean.getBoolean("kwelcome.test.native-minimessage")
                ? MessageRenderer.compatibleRenderer()
                : MessageRenderer.legacyRenderer();

        Component prefix = renderer.render("<gray>[<gold>kWelcome</gold>]</gray> ");
        Component message = prefix.append(renderer.render(
                "<yellow><player></yellow> <green>joined the server!",
                new MessageManager.Placeholder("player", "Alex<red>")
        ));

        assertEquals(
                "[kWelcome] Alex<red> joined the server!",
                PlainTextComponentSerializer.plainText().serialize(message)
        );
    }
}
