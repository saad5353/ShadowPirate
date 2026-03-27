# ShadowPirate Game

A 2D Java game built with [Bagel](https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/), with two levels, enemy combat, collectible items, and a final treasure objective.


## GamePlay

<img width="1026" height="800" alt="1" src="https://github.com/user-attachments/assets/c186efd7-d962-4530-933d-77ee1cabfe20" />
<img width="1026" height="800" alt="2" src="https://github.com/user-attachments/assets/8e75fac0-f602-403c-a597-f0b5ff406acb" />
<img width="1026" height="800" alt="3" src="https://github.com/user-attachments/assets/57c09c82-9679-4edd-9965-09e3915327f6" />

## Features

- Two-level progression (`level0` -> `level1`)
- Player movement and melee attack
- Pirate and Blackbeard enemies with projectile attacks
- Collectible items in level 1:
  - Sword (increases attack damage)
  - Elixir (increases max/current health)
  - Potion (heals current health)
- Bomb/barrel hazards in level 1
- Health display and game-over/win states

## Project Structure

- `src/ShadowPirate.java` - game entry point and main update loop
- `src/files/level0.java` - level 0 logic
- `src/files/level1.java` - level 1 logic, items, bombs, boss
- `src/files/salior.java` - player model and movement/collision
- `src/files/pirate.java` - pirate enemy model
- `src/files/blackbeard.java` - blackbeard enemy model
- `res/` - sprites, font, and CSV map files
- `pom.xml` - Maven build and LWJGL/Bagel dependencies

## Requirements

- Java 11+
- Maven 3.8+
- Windows/Linux/macOS (native LWJGL profile selected from OS)

## Build

From the project root:

```powershell
mvn -DskipTests compile
```

## Run

### Recommended (IDE)

Open the project in IntelliJ IDEA and run `main()` in `src/ShadowPirate.java`.

### Optional (Maven package only)

```powershell
mvn -DskipTests package
```

> Note: `pom.xml` does not currently define an `exec-maven-plugin` run target.
> Running from IDE is the simplest path unless you add a dedicated run plugin/config.

## Controls

- Arrow keys: move
- `S`: attack
- `SPACE`: start game / progress from level 0 completion screen
- `ESC`: exit

## Gameplay Notes

- Defeat/avoid enemies while navigating platforms and obstacles.
- In level 1, collect items before reaching treasure for better survivability.
- Bomb/barrel obstacles can damage the player on contact.

## Troubleshooting

- If assets do not load, confirm working directory is project root so `res/` paths resolve.
- If native library errors occur, ensure Maven resolved dependencies for your OS profile.
- If compile fails, verify Java version is 11 (`maven.compiler.source/target` in `pom.xml`).

