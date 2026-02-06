Shipment Routing Challenge
A native Android application that calculates the optimal assignment of shipments to drivers to maximize the total Suitability Score (SS).

- Architecture
  - MVVM + Clean: enforcing a strict separation of concerns into three layers. This ensures the core business logic is isolated, testable, and portable.
  - Dependency Injection (Hilt): Used to decouple the layers
  - Use Case Pattern: The CalculateOptimizedAssignmentsUseCase acts as an orchestrator. It fetches data from the repository and passes it to the algorithm. This prevents the ViewModel from becoming bloated with business logic.

- Folder structure

  app/
  - domain/                  # Pure Kotlin
    - algorithm/             # the more math and algorithm
    - model/                 # Entitles
    - repository/            # Repo Interface
    - usecase/               # Business rules
  - data/                    # Frameworks & I/O
    - repository/            # Repo Implementations
    - model                  # DTOs
  - ui/                      # Presenter
    - screens.drivers/       # Compose Screens (View + ViewMode)
  - di/                      # Dependencies Injections

- Tech Stack
  - Language: Kotlin
  - UI: Jetpack Compose
  - Architecture: MVVM + Clean Architecture
  - Dependency Injection: Hilt
 
- How to Run
1. Clone the repository.
2. Open in Android Studio.
3. Let Gradle sync.
4. Run on an Emulator or Physical Device.
  - Note: The app reads from src/main/assets/data.json.
