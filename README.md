# File Management System

## Purpose

This repository contains an implementation of a File Management System, developed as an assignment task. The system is designed to manage JSON files by storing them in an S3 bucket, downloading them, parsing them, and finally storing them in a PostgreSQL database. When requested from the frontend, it displays the files with correct JSON format in the frontend UI.

## Features

- **S3 Integration**: Files are stored in an S3 bucket for efficient and scalable storage.
- **File Download**: The system provides functionality to fetch files from the S3 bucket.
- **File Parsing**: After downloading, the system parses the files to extract relevant information.
- **PostgreSQL Integration**: Parsed data is stored in a PostgreSQL database for structured querying and retrieval.
- **Frontend UI**: Displays file data in the frontend UI of only files with correct JSON format.

## Technology Stack

- **Frontend**: React.js
- **Backend**: Java 8, Spring Boot, REST API
- **Database**: PostgreSQL
- **Testing**: JUnit

## Prerequisites

Before running the application, ensure you have the following prerequisites installed:

- **Java Development Kit (JDK)**: Ensure you have Java Development Kit (JDK) version 8 or higher installed on your system.
- **Node.js and npm**: You need Node.js and npm (Node Package Manager) to run the frontend application. You can download and install them from [Node.js website](https://nodejs.org/).
- **PostgreSQL Database**: You should have PostgreSQL installed either locally or in a Docker container. The database should have a schema named `postgres`.

### Installing PostgreSQL Locally

If you're installing PostgreSQL locally, you can download and install it from the [official PostgreSQL website](https://www.postgresql.org/). After installation, make sure the PostgreSQL service is running.

## Running PostgreSQL in Docker Container

If you prefer to use Docker, you can run PostgreSQL in a Docker container. Use the following command to run a PostgreSQL container with a database named `assignmentTask`:

```bash
docker run -d --name postgres-db -e POSTGRES_DB=assignmentTask -p 5432:5432 postgres
```

## Getting Started

### Starting Front-end

To start the front-end, navigate to `\assessment-task\Assignment_Frontend\front-end` directory and run the following command:

```bash
npm start
```

### Starting Back-end

To start the back-end, navigate to `\assessment-task\Assignment_Backend\` directory of the project and run the following command:

```bash
./mvnw spring-boot:run
```
