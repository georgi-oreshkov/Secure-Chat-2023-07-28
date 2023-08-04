package com.jorji.chat.authorization_service.services;

import com.jorji.chat.authorization_service.config.Argon2Properties;
import org.springframework.stereotype.Service;

import static com.kosprov.jargon2.api.Jargon2.*;

@Service
public class HashingService {
    private final Verifier verifier;

    public HashingService(Argon2Properties properties) {

        this.verifier = jargon2Verifier()
                .type(Type.ARGON2d)
                .memoryCost(properties.getMemoryKb())
                .timeCost(properties.getIterations())
                .parallelism(properties.getParallelism());
    }

    public boolean verify(byte[] hash, byte[] password, byte[] salt){
        return  verifier.hash(hash).password(password).salt(salt).verifyRaw();
    }
}
