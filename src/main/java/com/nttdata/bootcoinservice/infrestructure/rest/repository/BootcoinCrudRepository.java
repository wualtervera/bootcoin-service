package com.nttdata.bootcoinservice.infrestructure.rest.repository;


import com.nttdata.bootcoinservice.domain.Bootcoin;
import com.nttdata.bootcoinservice.infrestructure.model.dao.BootcoinDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BootcoinCrudRepository extends ReactiveCrudRepository<BootcoinDao, String> {
    Mono<Bootcoin> findByNumberPhone(String numberPhone);
}
