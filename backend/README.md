Requirements:
- Java 21.0.2 (openJDK)
- Gradle 8.14.1


To add dependency:
- Add dependency in build.gradle, could be `developmentOnly`, `runtimeOnly`, `implementation`, `testRuntimeOnly`, or `testImplementation` (for example)
```
dependencies {
    ...
	implementation 'org.springframework.boot:spring-boot-starter-web'
}
```


- Build app:
```
./gradlew build
```


To run:
- Build app
```
./gradlew build

```
- Run (locally)
```
./gradlew bootRun
```
or run the jar directly (might be more suitable for prod)
```
java -jar ./build/libs/*.jar
```


TODO:
- Lombok (remove getter/setter boilerplate)
- Null safety & annotation

```
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

@NonNullApi
public class Example {
  private String name;

  public Example(@NonNull String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void processData(@Nullable String data) {
    if (data != null) {
      // Process data
    }
  }

  @NonNull
  public String getDefaultValue() {
    return "default";
  }
}
```
