
# **Brick Breaker Game – Java OOP Project**

## 🎯 **Overview**

This project is a **Brick Breaker** game developed in **Java** using **Swing** for the GUI and applying **core Object-Oriented Programming (OOP) principles**.
It includes multiple difficulty levels, obstacles, bonuses, restart functionality, and a main menu.
The goal is to demonstrate OOP concepts in a fully functional, interactive game.

---

## 📜 **Features**

* **3 Difficulty Levels** – Easy, Medium, Hard
* **Custom Game Rules** – Unbreakable bricks, dynamic levels, bonuses
* **Score System** – Gain points for breaking bricks
* **Lives System** – Lose a life when the ball falls
* **Restart & Main Menu Options** – Play again without restarting the program
* **Exception Handling** – Prevents crashes due to invalid inputs or missing files

---

## 🛠 **OOP Concepts Used**

* **Encapsulation** – Private attributes with getters/setters in all classes
* **Information Hiding** – Implementation details hidden from other classes
* **Inheritance** – `Ball`, `Paddle`, and `Brick` inherit from `GameObject`
* **Polymorphism**

  * By inclusion (dynamic binding)
  * Overloading
  * Parametric (Generics)
  * Coercion
* **Abstraction** – `GameObject` is abstract, enforcing `draw()` and `update()`
* **Composition** – `BrickBreakerGame` contains `Ball`, `Paddle`, and multiple `Brick` objects
* **Subtyping** – Superclass references to subclass objects

---

## 🖥 **Technologies Used**

* **Language:** Java (OpenJDK 24)
* **GUI Framework:** Java Swing
* **IDE:** Visual Studio Code
* **Platform:** Cross-platform (Windows, macOS, Linux)

---

## 🚀 **How to Run**

1. Clone or download the project folder.
2. Open the folder in **VS Code** or any Java IDE.
3. Compile:

   ```bash
   javac *.java
   ```
4. Run:

   ```bash
   java LevelSelectionMenu
   ```

---

## 📂 **Project Structure**

```
BrickBreaker.java           // Main game launcher
LevelSelectionMenu.java     // Difficulty selection menu
GameWindow.java             // Main game window
GameObjects.java            // Abstract class and object definitions (Ball, Paddle, Brick)
```

---

## 🏆 **Learning Outcomes**

* Applied OOP principles in a practical game development project.
* Designed modular, maintainable code with clear class responsibilities.
* Implemented event handling, graphics rendering, and collision detection.
* Strengthened Java GUI programming skills using Swing.

