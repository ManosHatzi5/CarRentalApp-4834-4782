# 🚗 Car Rental Management System

A full-stack desktop application designed to manage car rental operations, built strictly with Java and object-oriented design principles. The system provides a robust graphical user interface and handles data persistence without relying on external databases.

## 🚀 Features

*   **Object-Oriented Data Persistence:** Implements custom file-handling mechanisms to save and load state (vehicles, customers, rentals) entirely through Java object serialization/file I/O.
*   **Advanced GUI Architecture:** Utilizes Java Swing with a `CardLayout` architecture to ensure seamless, flicker-free navigation between different dashboard views.
*   **Dynamic Data Rendering:** Employs custom `JTable` models to dynamically display, sort, and update rental inventory and active contracts in real-time.
*   **State Management:** Encapsulates business logic to prevent double-booking and maintain accurate vehicle availability states.

## 🛠️ Tech Stack

*   **Language:** Java (JDK 25 compatible)
*   **GUI Framework:** Java Swing, AWT
*   **Build Tool:** Gradle

## 🏁 Getting Started

1. Clone the repository:
   ```bash
   git clone <your-repo-link>
   ```
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
3. Ensure your JDK is configured correctly to JDK 25.
4. Run the `Main` class to launch the Swing application.
