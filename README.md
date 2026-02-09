# Shipment Routing Challenge
A native Android application that calculates the optimal assignment of shipments to drivers to maximize the total Suitability Score (SS).

## Tech Stack
  - Language: Kotlin
  - UI: Jetpack Compose
  - Architecture: MVVM + Clean Architecture
  - Dependency Injection: Hilt
  - Concurrency: Kotlin Coroutines & Flow
  - Parsing: Gson

## Architecture
  - MVVM + Clean: enforcing a strict separation of concerns into three layers. This ensures the core business logic is isolated, testable, and portable.
  - Dependency Injection (Hilt): Used to decouple the layers
  - Use Case Pattern: The CalculateOptimizedAssignmentsUseCase acts as an orchestrator. It fetches data from the repository and passes it to the algorithm. This prevents the ViewModel from becoming bloated with business logic.

### 1. Domain-Driven Design
The core business logic is isolated in the `domain` package.
* **`SuitabilityScorer`**: A pure logic class responsible for calculating the SS between a single Driver and Shipment.
* **`RoutingAlgorithm`**: An interface defining the contract for assignment strategies. This allows for easy swapping between implementations (e.g., Greedy vs. Hungarian).
* **`CalculateOptimizedAssignmentsUseCase`**: The orchestrator. It fetches data, builds the score matrix, runs the algorithm, and maps the results to domain objects.

### 2. Algorithmic Strategy
Two algorithms were implemented to compare results:


* **Greedy Algorithm (Comparison):**
    - Speed: O(N^2)
    - Instead of just looking at the first driver and picking their best shipment, this algorithm looks at the entire grid of possibilities to find the single highest Suitability Score available anywhere, locks it in, and then repeats.
    - pros
       * simpler to implement
       * faster than hungarian
       * good for massive datasets
    - cons
       * ** Not best solution ** because the total of other combination could be better.
       * The result may not be the most optimal

* **Hungarian Algorithm (Selected Default):**
    - Speed: O(N^3) 
    - It finds the optimal way to assign N resources (e.g., drivers) to N tasks (e.g., shipments) such that the total cost is minimized or the total profit is maximized.
    - It guarantees finding the global optimum, unlike a Greedy algorithm which only finds a local optimum.
    - pros
       * best optimal solution
       * maximize the Total Suitability Score.
    - cons
       * more complicated to implement
       * slower than greedy

    ** Hungarian Algorithm is the best solution for this assignment.
    ** The Greedy algorithm is a viable choice for large-scale data where performance is prioritized over absolute accuracy.
    ** we could easily swtich to a other algorithm later (with RoutingAlgorithm Interface).

### 3. Data Handling
* **`AssetDataRepository`**: Handles reading the local `data.json` file.
* **Performance:** The file reading is explicitly dispatched to `Dispatchers.IO` and uses Kotlin's `.use { }` block to strictly manage InputStreams and prevent memory leaks.

## Key Assumptions
  - Street Name Parsing:
    * The requirements specified "Street Name" length. I assumed the leading numeric portion (e.g., "9856") is the house number and should be excluded.
    * Example: "9856 Marvin Stravenue" -> "Marvin Stravenue" (Length 16).
  - data.json Size
    * I assume the data size will remain similar to the example given.
    * There for, using Hungarian Algorithm would be the best option.
  - Vowel/Consonant Rules
    * **Vowels:** defined as `[a, e, i, o, u]`.
    * **Consonants:** defined as any alphabetic character that is not a vowel.
    * **Case Sensitivity:** All comparisons are case-insensitive.

## Project Structure
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
   
## Branching
  - since it's only for one developer(me), I'm using a simple branching strategy to separate development and submission build
  - main            # for final submission - Merge when when there is workable solution
    - develop       # for development - Development PRs
 
## How to Run
1. Clone the repository.
2. Open in Android Studio.
3. Let Gradle sync.
4. Run on an Emulator or Physical Device.
  - Note: The app reads from src/main/assets/data.json.
