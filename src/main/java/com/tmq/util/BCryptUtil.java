package com.tmq.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BCryptUtil {
    public static char[] encryptPassword(char[] password) {
        return BCrypt.withDefaults().hashToChar(12, password);
    }

    public static boolean checkPassword(char[] hashPassword, char[] password) {
        return BCrypt.verifyer().verify(password, hashPassword).verified;
    }
}
