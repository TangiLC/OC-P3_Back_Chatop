package com.chatop.utils;

import java.util.Base64;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class GenerateJwtKey {

    public static void main(String[] args) {
        // Génère une clé sécurisée pour HMAC-SHA-512
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();

        // Encode la clé en Base64
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        // Affiche la clé encodée en Base64
        System.out.println("Generated Base64 Encoded Key: " + base64Key);

        // Note: Copiez cette clé dans votre fichier application.properties sous jwt.secret
    }
}
