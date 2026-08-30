# Contribution Guide

Thank you for your interest in contributing to **Mental Math**! This is a source-available native Android application designed to train mental calculation skills through a modern, clean, and fast user interface.

To maintain code quality, consistency, and simplicity, please follow these guidelines.

---

## How Can I Contribute?

### 1. Reporting Bugs or Requesting Features (Issues)
* Please check existing [Issues](https://github.com/NegK01/Mental-Math/issues) first to avoid duplicates.
* When creating a new Issue, select the appropriate form (Bug Report, Feature Request, or General Question).

### 2. Submitting Code (Pull Requests)
* All code contributions must be submitted via a **Pull Request (PR)** targeting the `main` branch.

---

## Code Style & Architecture Guidelines

The project follows a native **Clean Architecture + MVVM** pattern using Jetpack Compose, without heavy dependency injection frameworks.

* **Language:** 100% Kotlin.
* **User Interface:** Jetpack Compose with Material 3.
* **Design Tokens:** Use constants from `Tokens.kt` (`Spacing.*`, `Radius.*`, `Opacity.*`, `Motion.*`) instead of hardcoded literal values (`16.dp`, `8.dp`).
* **UI State:** Use `StateFlow` and State Hoisting to keep Composables pure and testable.

---

## Commit Message Convention

We follow the **Conventional Commits** specification in **English** to keep our commit history clear and maintainable:

* **Format:** `type(scope): concise description in English`
* **Types:** `feat`, `fix`, `refactor`, `style`, `docs`, `chore`, `test`.

### Valid Examples:
- `feat(records): add time tie-breaker for personal bests`
- `fix(ui): adjust history total games string formatting`
- `refactor(theme): adopt Spacing tokens across UI components`

---

## Git Workflow

1. **Fork** the repository to your GitHub account.
2. Create a descriptive branch for your work:
   ```bash
   git checkout -b feature/your-feature-name
   # or for bug fixes:
   git checkout -b fix/bug-description
   ```
3. Verify that the project compiles cleanly:
   ```bash
   ./gradlew assembleDebug
   ```
4. Submit your **Pull Request**, filling out the PR description template.
