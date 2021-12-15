package com.nttdata.bootcoinservice.infrestructure.redis;

import com.nttdata.bootcoinservice.domain.Bootcoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BootcoinRedisService {
    public static final String KEY = "wallet";

    @Autowired
    private ReactiveRedisTemplate<String, Bootcoin> redisTemplate;

    public Mono<Long> save(Bootcoin bootcoin) {
        System.out.println("wallet save in redis = " + bootcoin);
        return redisTemplate.opsForList().rightPush(KEY, bootcoin);
    }

    public Flux<Bootcoin> getAll() {
        System.out.println("wallet GetAll");
        return redisTemplate.opsForList().range(KEY, 0, -1);
    }
}
