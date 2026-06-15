# 🐍 Snake & Ladder Game — DSA Final Project

## 📌 Project Info
| Field | Detail |
|-------|--------|
| **Student Name** | Sawera Shehzadi |
| **Roll Number** | L1F24BSSE0304 |
| **Subject** | Data Structures & Algorithms |
| **Semester** | 4th Semester |
| **University** | University of Central Punjab (UCP), Lahore |

---

## 🎮 Project Overview

A fully functional **Snake & Ladder game** built in **Java Swing** (GUI) that demonstrates **5 core Data Structures** from DSA.

Two players take turns rolling a dice. Land on a ladder → go up. Land on a snake → go down. First to reach Square 100 wins!

---

## 🧠 Data Structures Used (5 Total)

| # | Data Structure | Class | Purpose |
|---|---------------|-------|---------|
| 1 | **Array** | `Board.java` | Stores all 100 board squares |
| 2 | **HashMap** | `BoardPanel.java` | O(1) lookup for snakes & ladders positions |
| 3 | **Stack** | `MoveHistory.java` | Stores move history for Undo feature (LIFO) |
| 4 | **Queue** | `TurnQueue.java` | Manages circular player turns (FIFO) |
| 5 | **LinkedList** | `EventLog.java` | Stores scrollable game event log |

---

## 📁 Project Structure

```
DSA_Final_Project/
├── src/
│   ├── Main.java          → Entry point
│   ├── Player.java        → Player model (name, position, color)
│   ├── Board.java         → DSA #1: Array (board squares)
│   ├── BoardPanel.java    → DSA #2: HashMap (snakes & ladders) + GUI rendering
│   ├── MoveHistory.java   → DSA #3: Stack (undo move)
│   ├── TurnQueue.java     → DSA #4: Queue (player turns)
│   ├── EventLog.java      → DSA #5: LinkedList (game log)
│   └── GameFrame.java     → Main GUI frame (integrates all DSA)
├── screenshots/
│   └── gameplay.png
└── README.md
```

---

## ✨ Features

- ✅ 2-player turn-based gameplay
- ✅ Animated board with snakes (red curves) and ladders (green)
- ✅ Dice roll (random 1–6)
- ✅ **Undo last move** (Stack-powered)
- ✅ Circular turn management (Queue-powered)
- ✅ Live scrollable game log (LinkedList-powered)
- ✅ Win detection with popup
- ✅ Restart game option

---

## 🗺️ Snakes & Ladders Map

### 🐍 Snakes (Head → Tail)
| Head | Tail |
|------|------|
| 98 | 12 |
| 88 | 30 |
| 74 | 53 |
| 62 | 19 |
| 47 | 16 |

### 🪜 Ladders (Base → Top)
| Base | Top |
|------|-----|
| 4 | 56 |
| 13 | 46 |
| 33 | 74 |
| 64 | 90 |
| 72 | 92 |

---

## ▶️ How to Run

### Prerequisites
- Java JDK 8 or above
- IntelliJ IDEA (recommended) or any Java IDE

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/DSA_Final_Project.git
   ```
2. Open in IntelliJ IDEA
3. Right-click `Main.java` → **Run 'Main'**

---



## 📚 OOP Concepts Used

- **Encapsulation** — Private fields with getters/setters in all classes
- **Abstraction** — Each DSA structure in its own dedicated class
- **Inheritance** — `BoardPanel extends JPanel`
- **Polymorphism** — `paintComponent()` override for custom rendering

---

## 👩‍💻 Developed By

**Sawera Shehzadi** | BS Software Engineering | UCP Lahore | 2024–2028
