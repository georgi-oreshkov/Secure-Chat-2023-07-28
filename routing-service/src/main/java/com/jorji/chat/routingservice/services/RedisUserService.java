package com.jorji.chat.routingservice.services;

import com.jorji.chatutil.userutil.model.ResolverUserModel;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RedisUserService {
    RedisTemplate<UUID, ResolverUserModel> redisTemplate;

    public ResolverUserModel getUser(UUID uuid) throws NoSuchElementException{
        ResolverUserModel user = redisTemplate.opsForValue().get(uuid);
        if (user == null) throw new NoSuchElementException();
        return user;
    }

    public void saveUser(UUID uuid, ResolverUserModel user){
        redisTemplate.opsForValue().set(uuid, user);
    }

    public void deleteUser(UUID uuid){
        redisTemplate.delete(uuid);
    }
}
