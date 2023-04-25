package com.example.RHMApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("api/")
public class HashController {
    private final String GENERATE_HASH_STRING_URL = "http://localhost:8090/api/generate-hash-string";

    @GetMapping("generate-hash-string")
    public String generateHashString() throws NoSuchAlgorithmException, InterruptedException {
        Thread.sleep(1000);
        String uuid = UUID.randomUUID().toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(uuid.getBytes(StandardCharsets.UTF_8));
        return DatatypeConverter.printHexBinary(hash);
    }

    @GetMapping("get-hash")
    public ResponseEntity<String> getHash() {
        RestTemplate restTemplate = new RestTemplate();
        String hash = null;
        do {
            hash = restTemplate.getForObject(GENERATE_HASH_STRING_URL, String.class);
            System.out.println(hash);
        } while (!isLastCharOddNumber(hash));
        System.out.println("Got it");
        return ResponseEntity.ok("Success! Hash string: " + hash);
    }

    private boolean isLastCharOddNumber(String hash) {
        char lastChar = hash.charAt(hash.length() - 1);
        if (Character.isDigit(lastChar)) {
            int lastInt = Character.getNumericValue(lastChar);
            if (lastInt % 2 != 0) {
                return true;
            }
        }
        return false;
    }
}
