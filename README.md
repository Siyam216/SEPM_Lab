# Student Management System

## CI/CD Pipeline

This project uses GitHub Actions for automated testing and deployment. The CI pipeline runs on every push and pull request to ensure code quality and functionality.

**Note:** The Maven Wrapper (`mvnw`) is used in CI/CD to ensure consistent build environment across different systems. On Linux/Unix systems (GitHub Actions), execute permissions are automatically set via `chmod +x mvnw` before running any Maven commands.
