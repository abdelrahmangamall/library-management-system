//package com.library.management.util;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
///**
// * Utility class to generate BCrypt password hashes
// */
//public class PasswordEncoderUtil {
//
//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        String password = "admin123";
//        String encodedPassword = encoder.encode(password);
//
//        System.out.println("Original password: " + password);
//        System.out.println("Encoded password: " + encodedPassword);
//
//        // Test if the encoded password matches
//        boolean matches = encoder.matches(password, encodedPassword);
//        System.out.println("Password matches: " + matches);
//
//        // Generate multiple hashes for different users
//        System.out.println("\n=== Password Hashes for SQL ===");
//        System.out.println("Admin password hash: " + encoder.encode("admin123"));
//        System.out.println("Librarian password hash: " + encoder.encode("admin123"));
//        System.out.println("Staff password hash: " + encoder.encode("admin123"));
//    }
//}