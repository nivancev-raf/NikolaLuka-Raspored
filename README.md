
# Scheduling Management System

**Scheduling Management System** is a microservices-based application developed as part of the "Software Components" course. The project focuses on creating a flexible and extensible library (component) for managing schedule data, with two different implementations, each offering unique ways to handle schedule operations. The system is designed to be highly customizable, allowing it to be adapted for various scheduling needs.

## Project Overview

The **Scheduling Management System** provides a robust solution for managing time and space schedules, with the ability to handle associated data such as event names, instructors, room capacities, and equipment. The project includes a well-defined API with two distinct implementations for schedule management and a command-line interface for interacting with the system.

### Key Features:
- **API for Schedule Management**:
  - Initialization of schedules and rooms with specific attributes.
  - Addition, removal, and modification of time slots with conflict checking.
  - Querying schedules for availability and occupied slots based on various criteria.

- **Multiple Implementations**:
  - **Implementation 1**: Schedule stored as a collection of individual time slots.
  - **Implementation 2**: Weekly recurring schedules over a specified period.

- **File Operations**:
  - Import schedules from JSON, CSV files.
  - Export schedules to JSON, CSV, and PDF formats.

- **Command-Line Interface**:
  - Load and manage schedules from files.
  - Search and modify schedules through the command line.

### Technologies Used

- **Programming Language**: Java
- **Build Tools**: Maven
- **File Formats**: JSON, CSV, PDF

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Apache Maven or Gradle

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/nivancev-raf/scheduling-microservices.git
   ```

2. Build the project using Maven or Gradle:
   ```bash
   cd scheduling-microservices
   mvn clean install
   ```

3. Run the command-line interface:
   ```bash
   java -jar target/scheduling-microservices.jar
   ```

### Usage

- **Initialize Schedule**: Set up rooms and time slots, then manage and query the schedule.
- **Import/Export Schedules**: Use the command-line interface to load schedules from files or export them for reporting.

## Documentation

The complete documentation for the **Scheduling Management System** project, including the technical and functional requirements, is available in the following pdf document:
- [Scheduling Management System Project Documentation](https://github.com/nivancev-raf/scheduling-microservices/blob/main/SK-prvi%20projekat2023.pdf)


## Contact
For any questions or feedback, please reach out to nivancev02@gmail.com.
