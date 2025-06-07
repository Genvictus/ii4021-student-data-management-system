## Requirements:

1. (Required) To run the app with docker

    - Docker

2. (Optional) For development or to run locally

    - Java 21.0.2 (openJDK)
    - Gradle 8.14.1

## Getting Started

1. Copy environment variable template
    ```
    cp .env.example .env
    ```

2. Set up public/private key pair for jwt signing
    ```
    mkdir .secrets
    ```

    ```
    openssl genrsa -out .secrets/private_key.pem 2048
    ```

    ```
    openssl req -new -x509 -key .secrets/private_key.pem -out .secrets/certificate.pem -days 365 -subj "/CN=JWT-Signing"
    ```

    ```
    openssl pkcs12 -export \
      -in .secrets/certificate.pem \
      -inkey .secrets/private_key.pem \
      -out .secrets/keystore.p12 \
      -name jwt-signing-key
    ```

   It will prompt you to enter an export password, make sure to adjust the `KEYSTORE_PASSWORD` in the `.env` to your
   inserted password

## How to run

The app will run on `localhost:3000`

### How to Run Everything in Docker

There are two options (pick one):

1. Run the docker compose with the `dev` profile (simpler)

   The `dev` profile will build the jar inside the container with multi-stage build. It might take some time
    ```
   docker compose --profile dev up
    ```

2. Run the docker compose with the `dev-light` profile (lighter)
   The `dev-light` profile will build NOT the jar inside the container. Instead, it will copy a prebuilt jar from
   your local machine to the container and run it directly (hence lighter). It's recommended if you work IDEs that
   have auto-build feature like IntelliJ
    - Build the jar (you can skip this part if it's already built before, or if it's automatically built)
        ```
      ./gradlew build
      ```
    - Run the docker compose
        ```
       docker compose --profile dev-light up
       ```

#### 💡 Pro Tip: Run with `--watch` command to enable hot reload. It works for both the `dev` and `dev-light` profile

```
docker compose --profile dev up --watch
```

```
docker compose --profile dev-light up --watch
```

### How to Run The App Locally

1. Run the database container
    ```
     docker compose up postgres-dev
    ```
2. Build the jar
    ```
   ./gradlew build
   ```
3. Run the jar
    ```
   java -jar build/libs/app-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=dev
   ```

## Development

1. To build the jar
    ```
    ./gradlew build
    ```
2. To test
    - Run the entire tests
        ```
        ./gradlew test 
        ```
    - Run an individual test
        ```
        ./gradlew test --tests com.std_data_mgmt.app.config.ApplicationTests
        ```
3. To run
   ```
   java -jar build/libs/app-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=dev
    ```
4. To bootRun
    ```
    ./gradlew bootRun
    ```
5. To add dependency

    - Add dependency in build.gradle, could be `developmentOnly`, `runtimeOnly`, `implementation`, `testRuntimeOnly`, or
      `testImplementation`
        ```
        dependencies {
            ...
          implementation 'org.springframework.boot:spring-boot-starter-web'
        }
        ```
    - Build the jar
        ```
      ./gradlew build
      ```