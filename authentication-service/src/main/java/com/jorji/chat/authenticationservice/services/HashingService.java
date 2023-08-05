package com.jorji.chat.authenticationservice.services;

import com.jorji.chat.authenticationservice.config.Argon2Properties;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class HashingService {
    private final Argon2Parameters.Builder parametersBuilder;

    public HashingService(Argon2Properties properties) {
        this.parametersBuilder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_i)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(properties.getIterations())
                .withMemoryAsKB(properties.getMemoryKb())
                .withParallelism(properties.getParallelism());
    }

    public boolean verify(byte[] hash, byte[] password, byte[] salt){
        Argon2BytesGenerator bytesGenerator = new Argon2BytesGenerator();
        bytesGenerator.init(parametersBuilder.withSalt(salt).build());
        byte[] newHash = new byte[hash.length];
        bytesGenerator.generateBytes(password, newHash);
        return Arrays.equals(hash, newHash);
    }
}
