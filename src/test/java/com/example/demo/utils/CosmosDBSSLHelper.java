package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Slf4j
public class CosmosDBSSLHelper {

    public static void setupTrustStore(String emulatorHost, int emulatorPort, String keyStorePassword) throws Exception {
        // Step 1: Create a temporary directory for the KeyStore
        Path tempFolder = Files.createTempDirectory("cosmos-emulator-keystore");
        Path certFile = tempFolder.resolve("cosmos-emulator.cert");
        Path keyStoreFile = tempFolder.resolve("azure-cosmos-emulator.keystore");

        // Step 2: Fetch and save the emulator certificate
        OpenSSLCommandExecutor.fetchCertificateWithOpenSSL(emulatorHost, emulatorPort, certFile.toString());

        // Step 3: Load the certificate into a KeyStore
        KeyStore keyStore = createKeyStoreFromCertificate(certFile.toString(), keyStorePassword);

        // Step 4: Save the KeyStore to a file
        try (FileOutputStream keyStoreOut = new FileOutputStream(keyStoreFile.toFile())) {
            keyStore.store(keyStoreOut, keyStorePassword.toCharArray());
        }

        // Step 5: Configure JVM to use the new KeyStore as the trust store
        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
        System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");

        log.info("TrustStore configured with certificate from: {}", certFile);
    }

    private static KeyStore createKeyStoreFromCertificate(String certPath, String keyStorePassword) throws Exception {
        // Load the PEM-formatted certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        try (FileInputStream fis = new FileInputStream(certPath)) {
            X509Certificate cert = (X509Certificate) cf.generateCertificate(fis);

            // Create a new KeyStore and add the certificate
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, keyStorePassword.toCharArray());
            keyStore.setCertificateEntry("cosmos-emulator", cert);

            return keyStore;
        }
    }

}
