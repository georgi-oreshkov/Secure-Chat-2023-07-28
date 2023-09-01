package com.jorji.chat.usermanagementservice.services;


import com.jorji.chat.usermanagementservice.config.Argon2Properties;
import com.jorji.chatutil.userutil.config.ContactIdProperties;
import com.jorji.chatutil.userutil.model.FullUser;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class HashingService {
    private final Argon2Parameters.Builder parametersBuilder;
    private final Argon2Properties argon2Properties;
    private final ContactIdProperties contactIdProperties;
    private final SecureRandom secureRandom;
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    public HashingService(Argon2Properties argon2Properties, ContactIdProperties contactIdProperties) throws NoSuchAlgorithmException {
        this.argon2Properties = argon2Properties;
        this.contactIdProperties = contactIdProperties;
        this.parametersBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(argon2Properties.getIterations())
                .withMemoryAsKB(argon2Properties.getMemoryKb())
                .withParallelism(argon2Properties.getParallelism());
        this.secureRandom = SecureRandom.getInstance("DRBG");
    }

    public FullUser.FullUserBuilder getUserBuilderWithPass(byte[] password) {

        byte[] salt = getSalt();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(this.parametersBuilder.withSalt(salt).build());
        byte[] hash = new byte[argon2Properties.getHashLengthBytes()];
        generator.generateBytes(password, hash);
        return FullUser.builder()
                .password(Base64.getEncoder().encodeToString(hash))
                .salt(Base64.getEncoder().encodeToString(salt));


    }

    private byte[] getSalt() {

        byte[] salt = new byte[argon2Properties.getSaltLengthBytes()];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public String generateContactId() {
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < contactIdProperties.getBeginningCharsLength(); i++) {
            int randomIndex = secureRandom.nextInt(LOWERCASE_LETTERS.length());
            passwordBuilder.append(LOWERCASE_LETTERS.charAt(randomIndex));
        }
        int digits = contactIdProperties.getLength() - contactIdProperties.getBeginningCharsLength();
        for (int i = 0; i < digits; i++) {
            int randomIndex = secureRandom.nextInt(DIGITS.length());
            passwordBuilder.append(DIGITS.charAt(randomIndex));
        }

        return passwordBuilder.toString();
    }
}
