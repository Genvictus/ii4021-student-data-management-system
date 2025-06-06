package com.std_data_mgmt.app.configs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * An EnvironmentPostProcessor that loads properties from a .env file
 * located at the project root into the Spring Environment.
 * This allows Spring to resolve placeholders like ${KEYSTORE_PATH} from
 * variables defined in the .env file.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DOTENV_FILE_NAME = ".env";
    private static final String DOTENV_PROPERTY_SOURCE_NAME = "dotenvProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        File dotenvFile = findDotenvFile();

        if (dotenvFile != null && dotenvFile.exists()) {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(dotenvFile)) {
                props.load(fis);
                environment.getPropertySources()
                        .addFirst(new PropertiesPropertySource(DOTENV_PROPERTY_SOURCE_NAME, props));
                System.out.println("Loaded .env file from: " + dotenvFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to load .env file from " + dotenvFile.getAbsolutePath() + ": " + e.getMessage());
            }
        } else {
            System.out.println("No .env file found at project root. Skipping .env loading.");
        }
    }

    /**
     * Attempts to find the .env file in the current working directory (project root).
     *
     * @return The File object for the .env file, or null if not found or accessible.
     */
    private File findDotenvFile() {
        try {
            String currentDir = System.getProperty("user.dir");
            File dotenvFile = new File(currentDir, DOTENV_FILE_NAME);

            if (dotenvFile.isFile() && dotenvFile.canRead()) {
                return dotenvFile;
            }
        } catch (SecurityException e) {
            System.err.println("Security exception while trying to access .env file: " + e.getMessage());
        }
        return null;
    }
}