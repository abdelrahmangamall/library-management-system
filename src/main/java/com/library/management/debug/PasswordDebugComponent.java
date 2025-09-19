//package com.library.management.debug;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
///**
// * Debug component to generate and test password hashes
// * This will run when the application starts
// */
//@Component
//public class PasswordDebugComponent implements CommandLineRunner {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("========== PASSWORD DEBUG ==========");
//
//        String rawPassword = "admin123";
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//
//        System.out.println("Raw Password: " + rawPassword);
//        System.out.println("Encoded Password: " + encodedPassword);
//
//        // Test the password
//        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
//        System.out.println("Password Matches: " + matches);
//
//        // Generate SQL update statement
//        System.out.println("\n========== SQL UPDATE STATEMENT ==========");
//        System.out.println("UPDATE users SET password_hash = '" + encodedPassword + "' WHERE username = 'admin';");
//        System.out.println("UPDATE users SET password_hash = '" + encodedPassword + "' WHERE username = 'librarian';");
//        System.out.println("UPDATE users SET password_hash = '" + encodedPassword + "' WHERE username = 'staff';");
//
//        // Test against some common hashes that might be in your database
//        String[] testHashes = {
//                "$2a$10$eIFWeyG2DE.DeqEhHRZWC.EHj6VKBXe9ZlN7EpWRZC2.f0LDZzWm.",
//                "$2a$10$N.zmdr9k7uOCRMb9B5J6VuBbTQ5UZ7ZQJhXeM7z7J8XzJjVYJ5XW2",
//                "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi."
//        };
//
//        System.out.println("\n========== TESTING EXISTING HASHES ==========");
//        for (int i = 0; i < testHashes.length; i++) {
//            boolean testMatch = passwordEncoder.matches(rawPassword, testHashes[i]);
//            System.out.println("Hash " + (i + 1) + " matches 'admin123': " + testMatch);
//            if (!testMatch) {
//                // Try with different passwords
//                String[] testPasswords = {"admin", "password", "123456", "admin123"};
//                for (String testPass : testPasswords) {
//                    if (passwordEncoder.matches(testPass, testHashes[i])) {
//                        System.out.println("  --> Hash " + (i + 1) + " matches password: '" + testPass + "'");
//                        break;
//                    }
//                }
//            }
//        }
//
//        System.out.println("========== END PASSWORD DEBUG ==========");
//    }
//}
