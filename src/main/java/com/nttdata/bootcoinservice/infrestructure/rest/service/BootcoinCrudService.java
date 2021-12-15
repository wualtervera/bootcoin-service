package com.nttdata.bootcoinservice.infrestructure.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.bootcoinservice.application.model.BootcoinRepository;
import com.nttdata.bootcoinservice.domain.Bootcoin;
import com.nttdata.bootcoinservice.infrestructure.model.dao.BootcoinDao;
import com.nttdata.bootcoinservice.infrestructure.rest.repository.BootcoinCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class BootcoinCrudService implements BootcoinRepository {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BootcoinCrudRepository bootcoinCrudRepository;


    @Override
    public Flux<Bootcoin> findAll() {
        return bootcoinCrudRepository.findAll().map(this::toWallet);
    }

    @Override
    public Mono<Bootcoin> findById(String id) {
        return bootcoinCrudRepository.findById(id).map(this::toWallet);
    }

    @Override
    public Mono<Bootcoin> save(Bootcoin bootcoin) {
        if (bootcoin.getCreateAt() == null)
            bootcoin.setCreateAt(LocalDateTime.now());
        return bootcoinCrudRepository.save(this.toWalletDao(bootcoin)).map(this::toWallet);
    }

    @Override
    public Mono<Bootcoin> update(String id, Bootcoin bootcoin) {
        return bootcoinCrudRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(bootcoinDao -> {
                    bootcoin.setId(bootcoinDao.getId());
                    bootcoin.setCreateAt(bootcoinDao.getCreateAt());
                    return bootcoinCrudRepository.save(this.toWalletDao(bootcoin)).map(this::toWallet);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return bootcoinCrudRepository.deleteById(id);
    }

    @Override
    public Mono<Bootcoin> findByNumberPhone(String numberPhone) {
        return bootcoinCrudRepository.findByNumberPhone(numberPhone);
    }


    public Bootcoin toWallet(BootcoinDao bootcoinDao) {
        return objectMapper.convertValue(bootcoinDao, Bootcoin.class);
    }

    public BootcoinDao toWalletDao(Bootcoin bootcoin) {
        return objectMapper.convertValue(bootcoin, BootcoinDao.class);
    }



}
