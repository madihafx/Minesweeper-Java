# 💣 Minesweeper in Java

> A fully-featured terminal Minesweeper game built in Java.  
> *Intro to CS Final Project — Madiha, Hana, Laura & Samia · Spring 2026*

---

## 🎮 About

This is a command-line implementation of the classic Minesweeper game. Navigate a hidden grid, use logic to avoid bombs, flag suspicious cells, and reveal every safe square to win!

---

## ▶️ How to Play

1. **Choose a board size** — any grid from `2×2` up to `20×20`
2. **Choose a difficulty:**
   - Easy → ~10% of cells are bombs
   - Medium → ~20% of cells are bombs
   - Hard → ~33% of cells are bombs
3. **On your turn, choose an action:**
   - `R` — Reveal a cell (enter row number + column letter)
   - `F` — Flag / unflag a cell you suspect has a bomb
4. **Your first move is always safe** — bombs are placed *after* your first pick
5. **Reveal every safe cell** to win — hit a bomb and it's game over!

---

## 🗺️ Board Symbols

| Symbol | Meaning |
|--------|---------|
| `[🔒]` | Hidden — not yet revealed |
| `[🛡️]` | Revealed — 0 bombs nearby (auto-expands neighbors!) |
| `[1]`–`[8]` | Revealed — number of neighboring bombs |
| `[🚩]` | Flagged by you (protected from accidental reveal) |
| `[💣]` | Bomb (shown when the game ends) |

### Example Board (mid-game)

```
       A      B      C
  1  [🛡️]  [🛡️]  [1]
  2  [🛡️]  [1]   [🔒]
  3  [🔒]  [🚩]  [🔒]
```
*Row 3, Column B is flagged as a suspected bomb.*

---

## ✨ Special Features

- **🔒 First-Move Safety** — Bombs are placed after your first reveal, so you can never lose on move one
- **🚩 Flag System** — Mark cells you suspect are bombs; the game warns you if you try to place more flags than there are bombs
- **⚡ Auto-Reveal Flood Fill** — Revealing an empty cell (`[🛡️]`) automatically cascades to uncover all connected safe neighbors
- **🎯 Scalable Difficulty** — Bomb density scales correctly to any board size using `Math.max()` to guarantee at least 1 bomb
- **📋 Animated Instructions** — On startup, instructions print line-by-line with delays to ease new players in
- **🏆 Win Detection + Replay** — Win condition is checked after every move; remaining bombs are auto-flagged on victory; play again without restarting

---

## 🧠 Java Concepts Used

| Concept | How it's used |
|---------|--------------|
| **2D Arrays** | Three parallel arrays track board values (`int[][]`), revealed state (`boolean[][]`), and flags (`boolean[][]`) |
| **Recursion** | `reveal()` calls itself on all 8 neighbors when a cell has 0 adjacent bombs — classic flood fill |
| **`Random` + `Scanner`** | `Random` places bombs randomly; `Scanner` reads all player input from the terminal |
| **`try` / `catch`** | Every numeric input is wrapped in exception handling so bad input never crashes the program |
| **`Thread.sleep()`** | Used in the instruction printer and game events for animated, readable output |
| **Static helper methods** | Logic is split into 11 focused methods (`placeBombs`, `calculateNeighbors`, `checkWin`, etc.) |

---

## 🚀 Running the Game

**Requirements:** Java (any modern version)

```bash
# Compile
javac Minesweeper.java

# Run
java Minesweeper
```

---

## 📁 File Structure

```
Minesweeper-Java/
├── Minesweeper.java   # All game logic — 485 lines, 11 helper methods
└── README.md
```

---

## 👩‍💻 Authors

Made with ❤️ (and a lot of debugging) by **Madiha, Hana, Laura, and Samia** for our Intro to CS final project.
