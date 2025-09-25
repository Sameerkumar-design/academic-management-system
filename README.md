## Campus Course & Records Manager (CCRM)

Console-based Java SE application demonstrating OOP pillars, patterns (Singleton, Builder), Streams, lambdas, NIO.2, recursion, and robust CLI UX.

### Project Layout
```
src/
  edu/ccrm/
    cli/            # Menu system, input handling
    config/         # AppConfig singleton
    domain/         # Entities, enums, builders, immutables
    io/             # Import/Export, Backup (NIO.2)
    service/        # Service interfaces
      impl/         # In-memory implementations
    util/           # Interfaces, exceptions
    Main.java       # Entry point
data/               # Sample CSVs (export/import target)
```

### Features
- OOP: Abstraction (`Person`), Inheritance (`Student`/`Instructor`), Encapsulation (private fields), Polymorphism (service interfaces).
- Patterns: Singleton (`AppConfig`), Builders (`Course.Builder`, `Transcript.Builder`).
- NIO.2: CSV import/export, timestamped backups, recursive size, depth listing.
- Streams & Lambdas: Sorting, filtering, ranking, Runnable async demo.
- CLI: Student/Course CRUD, Enrollment/Grades, Import/Export, Backups, Reports.
- Assertions: Credits overflow guard (enable with `-ea`).

### Prerequisites
- Java 17+ recommended
- Windows PowerShell or any shell

### Build & Run
Windows PowerShell:
```powershell
javac -d out (Get-ChildItem -Recurse src\*.java).FullName
java -ea -cp out edu.ccrm.Main
```

macOS/Linux (bash):
```bash
find src -name "*.java" | xargs javac -d out
java -ea -cp out edu.ccrm.Main
```

### Sample Data
Place CSVs in `data/` or export from the app via Import/Export menu.

students.csv
```
id,regNo,fullName,email,status,registrationDate
1,2023001,John Doe,john.doe@uni.edu,ACTIVE,2023-08-15
```

courses.csv
```
code,title,credits,instructor,department,semester
CS101- A,Introduction to Programming,3,prof.Vijay kumar trivedi,COMPUTER_SCIENCE,SPRING_2024
```

### Syllabus Mapping (Where Concepts Are Demonstrated)
- Abstraction/Inheritance: `edu.ccrm.domain.Person`, `Student`, `Instructor`
- Encapsulation: All domain classes (private fields + getters/setters)
- Polymorphism: `edu.ccrm.service.*` interfaces with `impl` implementations
- Singleton: `edu.ccrm.config.AppConfig`
- Builder: `edu.ccrm.domain.Course.Builder`, `Transcript.Builder`
- Streams: `cli.Menu#reportsMenu`, `service.impl.*` filtering
- Lambdas: Comparator in reports, Runnable async demo
- NIO.2: `io.ImportExportService`, `io.BackupService`
- Recursion: `io.BackupService#directorySize` via `walkFileTree`
- Custom Exceptions: `util.Exceptions`
- Assertions: credit overflow in `InMemoryEnrollmentService#enroll`

### Contributing
PRs welcome. Please format code consistently and keep features well-isolated.

### License
MIT


