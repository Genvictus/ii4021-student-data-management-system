package com.std_data_mgmt.app.config;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "keystore")
@Getter
@Setter
@ToString
public class JwtConfig {

    private String path;
    private String password;
    private String alias;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private int expirationMs = 24 * 60 * 60 * 1000;

    @PostConstruct
    public void initKeys() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        try (FileInputStream fis = new FileInputStream(this.path)) {
            keyStore.load(fis, this.password.toCharArray());
        }

        this.privateKey = (PrivateKey) keyStore.getKey(this.alias, this.password.toCharArray());
        Certificate cert = keyStore.getCertificate(this.alias);
        this.publicKey = cert.getPublicKey();

    }
}
