package edu.bluejack21_2.subscriptly.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Crypt {
    public static String generateHash(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt());
    }

    public static boolean verifyHash(String text, String hash) {
        return BCrypt.checkpw(text, hash);
    }
}
