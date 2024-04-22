# File Management System

## Purpose

This repository contains an implementation of a File Management System, developed as an assignment task. The system is designed to manage json files by storing them in an S3 bucket, downloading them, parsing them, and finally storing them in a PostgreSQL database. When requested from the frontend, it displays the files with correct JSON format in the frontend UI.

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
