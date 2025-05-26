package com.std_data_mgmt.app.security.jwt;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Component
public class JwtKeyProvider {
    @Value("${keystore.path}")
    private String path;
    
    @Value("${keystore.password}")
    private String password;
    
    @Value("${keystore.alias}")
    private String alias;
    
    @Getter
    private PrivateKey privateKey;
    
    @Getter
    private PublicKey publicKey;
    
    @Getter
    private final int expirationMs = 24 * 60 * 60 * 1000; // 24 Hours
    
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
    
    @Override
    public String toString() {
        return "JwtKeyProvider{" +
                "expirationMs=" + expirationMs +
                ", privateKey=" + (privateKey != null ? "[HIDDEN]" : "null") +
                ", publicKey=" + (publicKey != null ? "[HIDDEN]" : "null") +
                '}';
    }
}