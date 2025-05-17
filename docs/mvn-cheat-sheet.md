Here's the updated cheat sheet with `fatjar` profile commands added:

---

# Maven Stress Test Cheat Sheet

## Running (or skipping) Tests and Building Fat JARs

| Command                                   | What It Does                                                |
| ----------------------------------------- | ----------------------------------------------------------- |
| `mvn clean install -DskipTests`           | Skips all tests                                             |
| `mvn clean install`                       | Runs default profile: all tests EXCEPT stress tests         |
| `mvn test`                                | Runs default profile: all tests EXCEPT stress tests         |
| `mvn clean package -Pfatjar`              | Builds fat JAR only                                         |
| `mvn clean install -Pfatjar`              | Builds fat JAR and installs to local Maven repo             |

## Profiles

1. **fatjar profile** (activated with `-Pfatjar`)

    * Builds an executable fat JAR via the Maven Shade Plugin
    * Only runs when explicitly activated