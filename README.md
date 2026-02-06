# Shipment Routing Challenge
A native Android application that calculates the optimal assignment of shipments to drivers to maximize the total Suitability Score (SS).

## Architecture
  - MVVM + Clean: enforcing a strict separation of concerns into three layers. This ensures the core business logic is isolated, testable, and portable.
  - Dependency Injection (Hilt): Used to decouple the layers
  - Use Case Pattern: The CalculateOptimizedAssignmentsUseCase acts as an orchestrator. It fetches data from the repository and passes it to the algorithm. This prevents the ViewModel from becoming bloated with business logic.

## Folder structure

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

## Tech Stack
  - Language: Kotlin
  - UI: Jetpack Compose
  - Architecture: MVVM + Clean Architecture
  - Dependency Injection: Hilt
 
## Algorithm
  - Greedy Algorithm
    - Instead of just looking at the first driver and picking their best shipment, this algorithm looks at the entire grid of possibilities to find the single highest Suitability Score available anywhere, locks it in, and then repeats.
    - pros
       * good enough solution
       * simpler to implement
       * faster than hungarian O(N^2): scan and sort
       * it's a better solution for big data
    - cons
       * ** Not best solution ** because the total of other combination could be higher.
    - user can easily swtich to a better algorithm later (with RoutingAlgorithm Interface)
   
## Branching
  - since it's only for one developer(me), I'm using a simple branching strategy to separate development and submission build
  - main            # for final submission - Merge when when we is workable solution
    - develop       # for development - Merge all development PRs
 
## How to Run
1. Clone the repository.
2. Open in Android Studio.
3. Let Gradle sync.
4. Run on an Emulator or Physical Device.
  - Note: The app reads from src/main/assets/data.json.
