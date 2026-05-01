# Dungeon Crawler (Java)

A simple terminal-based dungeon crawler written in Java. You play as a single character navigating procedurally generated floors, fighting enemies, opening chests, and hunting down a boss before reaching the legendary Key.

## Features

- **Single-player** — one `@` character with HP, ATK, and a level-up system
- **Procedural dungeons** — BSP-tree map generation on a fixed 70×30 grid
- **Enemies** — randomly named from a pool of 15 types (Goblin, Skeleton, Orc, Shade, etc.); stats scale with floor number
- **Boss** — "The Dungeon Lord" appears on floor 5; actively chases you once in range (Manhattan distance ≤ 12)
- **Chests** — randomly placed each floor; reward either Max HP or ATK increase
- **XP and leveling** — gain XP from kills; each level increases Max HP and ATK
- **6-floor structure** — floors 1–4 normal, floor 5 boss floor, floor 6 has the Key (win condition)
- **Logger** — scrolling message log shows combat results and floor events
- **Game over / win screens** — tombstone on death, victory banner on Key pickup

## Requirements

- Java 11 or later

## Building

```bash
javac game/*.java
```

## Running

```bash
java game.Game
```

## Controls

The game uses text input — type a character and press Enter each turn.

| Input | Action |
|-------|--------|
| `W` | Move up |
| `S` | Move down |
| `A` | Move left |
| `D` | Move right |

Moving into an enemy or boss tile attacks it. Enemies move each turn after you.

## Map Symbols

| Symbol | Meaning |
|--------|---------|
| `@` | Player |
| `E` | Enemy |
| `B` | Boss |
| `>` | Stairs (descend to next floor) |
| `K` | Key (win condition) |
| `C` | Chest |
| `#` | Wall |
| ` ` | Floor |

## Floor Structure

| Floor | Content |
|-------|---------|
| 1–4 | Normal floors; enemy count = 4 + floor number |
| 5 | Boss floor — defeat The Dungeon Lord before descending |
| 6 | Key floor — reach the Key to win |

## Project Structure

```
.
├── Game.java        # Entry point, game loop, input handling
├── Character.java   # Abstract base class (HP, ATK, position)
├── Player.java      # Player stats, leveling, chest rewards
├── Enemy.java       # Enemy stats and name pool
├── Boss.java        # Boss with directional chase AI
├── AI.java          # Enemy movement (random walk + melee attack; boss pathfinding)
├── Room.java        # BSP dungeon generation and chest placement
├── Spawner.java     # Floor loading: map gen, player/enemy/boss placement
├── Renderer.java    # Terminal rendering and title/tombstone screens
└── Logger.java      # Message log and session log file output
```

## Notes

- There is no save system; closing the game ends the run
- Combat is bump-based — no separate battle screen
- The boss will actively path toward you once it has line of sight (within 12 tiles); outside that range it moves randomly
- A session log is saved to disk on game over or victory