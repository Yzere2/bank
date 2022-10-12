package com.example.bankservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    int id;
    String Name;
    String PrincipalName;
    String PasswordHash;
    String GuidToken;
    String Salt;


//    public String passwordHashing(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[16];
//        random.nextBytes(salt);
//        KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 1000, 128);
//        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        byte[] hash = f.generateSecret(spec).getEncoded();
//        Base64.Encoder enc = Base64.getEncoder();
//        System.out.printf("salt: %s%n", enc.encodeToString(salt));
//        System.out.printf("hash: %s%n", enc.encodeToString(hash));
//        System.out.printf("password: %s%n", password);
//        System.out.printf("spec: %s%n", spec);
//        System.out.printf("f: %s%n", f);
//        return "salt:"+enc.encodeToString(salt)+"  "+"hash:"+ enc.encodeToString(hash);
//    }

    public String passwordHashing(String password) {
            // salt 32 bytes
            // Hash length 64 bytes
            Argon2 argon2 = Argon2Factory.create(
                    Argon2Factory.Argon2Types.ARGON2id,
                    16,
                    32);

            char[] passwordTab = password.toCharArray();
            String hash = argon2.hash(3, // Number of iterations
                    64 * 1024, // 64mb
                    1, // how many parallel threads to use
                    passwordTab);
            System.out.println("Hash + salt of the password: "+hash);
            System.out.println("Password verification success: "+ argon2.verify(hash, password));
            System.out.println("Password verification success: "+ argon2.verify(hash, password+"aeca"));
        return hash;
    }
}
