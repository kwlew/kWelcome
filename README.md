# kWelcome

kWelcome is a lightweight Paper plugin for customizable player join, first join, and quit messages. It also tracks each player's previous login time so it can be included in welcome messages.

## Features

- Custom join and quit messages
- A separate message for a player's first join
- Accurate first-join detection on established servers
- Previous login date and time formatting
- MiniMessage colors and formatting
- Safe `<player>` and `<last_login>` placeholders
- Runtime configuration reloads
- UUID based player storage
- Optional join, first join, and quit messages
- bStats usage metrics

## Compatibility

One plugin JAR supports the following Paper versions:

| Paper version | Server Java version |
| --- | --- |
| 1.18 through 1.19.4 | Java 17 |
| 1.20 through 1.21.11 | Java 21 |
| 26.1.x and 26.2 | Java 25 |

The plugin itself is compiled for Java 17. Newer Java runtimes can load Java 17 bytecode.

kWelcome targets Paper and uses Paper's Adventure component API. Other Bukkit based server implementations are not currently tested or officially supported.

## Installation

1. Download or build `kWelcome-2.1.0.jar`.
2. Place the JAR in the server's `plugins` directory.
3. Start or restart the server.
4. Edit the generated files in `plugins/kWelcome`.
5. Run `/kwelcome reload` after changing the configuration or messages.

## Configuration

The main options are stored in `config.yml`:

```yaml
enable-join-message: true
enable-firstjoin-message: true
enable-quit-message: true

last-login:
  format: "yyyy-MM-dd HH:mm:ss"
  zone: "system"
```

`last-login.format` accepts a Java date and time pattern. `last-login.zone` accepts `system` or a valid zone such as `America/Sao_Paulo`.

If a pattern or zone is invalid, kWelcome logs a warning and uses a safe default.

## Messages

Messages are stored in `messages.yml` and support [MiniMessage formatting](https://docs.papermc.io/adventure/minimessage/format/).

Example:

```yaml
prefix: "<gray>[<gold>kWelcome</gold>]</gray> "

player:
  join: "<gray>[<green>+</green>]</gray> <yellow><player></yellow> <green>joined the server! Last login: <last_login>"
  first-join: "<gray>[<green>+</green>]</gray> <yellow><player></yellow> <green>joined for the first time!"
  quit: "<gray>[<red>-</red>]</gray> <yellow><player></yellow> <red>left the server."
```

Available placeholders:

| Placeholder | Availability | Description |
| --- | --- | --- |
| `<player>` | Join, first join, and quit messages | The player's current name |
| `<last_login>` | Regular join messages | The player's previous login time |

Placeholder values are inserted as plain text, so player names cannot inject MiniMessage tags.

## Commands and permissions

| Command | Permission | Description |
| --- | --- | --- |
| `/kwelcome reload` | `kwelcome.reload` | Reloads the configuration, messages, and player data |

The `kwelcome.reload` permission is granted to server operators by default.

## Data files

kWelcome creates these files in `plugins/kWelcome`:

| File | Purpose |
| --- | --- |
| `config.yml` | Feature toggles and last login formatting |
| `messages.yml` | Prefix and configurable messages |
| `players.yml` | UUID based first join and last login data |

Back up `players.yml` before manually editing it.

Player data is saved once per minute, before a player-data reload, and when the server shuts down. This avoids writing the file during every player join.

## Building

The project uses the Gradle wrapper and a Java 25 build toolchain. The resulting plugin still targets Java 17.

```bash
bash ./gradlew clean build
```

The completed plugin is written to:

```text
build/libs/kWelcome-2.1.0.jar
```

To start a development server for a specific supported version:

```bash
bash ./gradlew runServer -PrunMinecraftVersion=26.2
```

## Metrics

kWelcome uses bStats to collect anonymous plugin usage statistics. Server owners can disable bStats through the shared bStats configuration in the server's `plugins/bStats` directory.
