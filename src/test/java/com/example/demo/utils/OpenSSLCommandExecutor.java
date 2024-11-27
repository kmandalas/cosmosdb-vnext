package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class OpenSSLCommandExecutor {

    public static void fetchCertificateWithOpenSSL(String host, int port, String outputPath) throws Exception {
        String command = String.format(
                "openssl s_client -connect %s:%d </dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p'",
                host, port
        );

        // Execute the command using ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.redirectErrorStream(true); // Redirect error stream to standard output

        Process process = processBuilder.start();

        // Capture the output of the command
        String output;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            output = reader.lines().collect(Collectors.joining("\n"));
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Command execution failed with exit code " + exitCode);
        }

        // Write the certificate to the output file
        try (FileWriter fileWriter = new FileWriter(outputPath)) {
            fileWriter.write(output);
        }

        log.info("Certificate saved to {}", outputPath);
    }

}
