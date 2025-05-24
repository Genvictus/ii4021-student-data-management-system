Requirements:
- Java 21.0.2 (openJDK)
- Gradle 8.14.1


## Getting Started
1. Copy environment variable template
    ```
    cp .env.example .env
    ```

2. Set up public/private key pair for jwt signing
    ```
    openssl genrsa -out .secrets/private_key.pem 2048
    ```

    ```
    openssl rsa -in .secrets/private_key.pem -pubout -out .secrets/public_key.pem
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

    It will prompt you to enter an export password, make sure to adjust the KEYSTORE_PASSWORD in the .env to your inserted password

3. Export .env variables 
    ```
    export $(cat .env | xargs)
    ```

## Development

1. To add dependency:
- Add dependency in build.gradle, could be `developmentOnly`, `runtimeOnly`, `implementation`, `testRuntimeOnly`, or `testImplementation` (for example)
    ```
    dependencies {
        ...
      implementation 'org.springframework.boot:spring-boot-starter-web'
    }
    ```

- Build gradle:
    ```
    ./gradlew build
    ```


2. To run:
- Run (locally)
    ```
    ./gradlew bootRun
    ```
- or run the jar directly (might be more suitable for prod)
    ```
    java -jar ./build/libs/*.jar
    ```

3. To test:
- Run the entire tests
    ```
    ./gradlew test 
    ```
- Run individual test
    ```
    ./gradlew test --tests com.std_data_mgmt.app.config.ApplicationTests
    ```
