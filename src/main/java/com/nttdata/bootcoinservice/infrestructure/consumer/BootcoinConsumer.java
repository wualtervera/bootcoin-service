package com.nttdata.bootcoinservice.infrestructure.consumer;

import com.nttdata.bootcoinservice.infrestructure.model.dto.BootcoinTransactionDto;
import com.nttdata.bootcoinservice.infrestructure.producer.BootcoinProducer;
import com.nttdata.bootcoinservice.infrestructure.rest.service.BootcoinCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BootcoinConsumer {

    @Autowired
    private BootcoinCrudService walletCrudService;

    @Autowired
    private BootcoinProducer bootcoinProducer;


    @KafkaListener(
            topics = "${custom.kafka.topic-name}",
            groupId = "${custom.kafka.group-id}",
            containerFactory = "walletConcurrentKafkaListenerContainerFactory")
    public void consumer(@Payload BootcoinTransactionDto bootcoinTransactionDto, @Headers MessageHeaders headers) {
        log.info("message received [{}]", bootcoinTransactionDto);
        this.validData(bootcoinTransactionDto);
        System.out.println("Sent message!");
    }

    public void validData(BootcoinTransactionDto bootcoinTransactionDto) {
        walletCrudService.findByNumberPhone(bootcoinTransactionDto.getOriginNumberPhone())
                .flatMap(walletOrigin -> {
                    if (walletOrigin.getAmount() >= bootcoinTransactionDto.getAmount()) {
                        walletOrigin.setAmount((walletOrigin.getAmount() - bootcoinTransactionDto.getAmount()));
                        walletCrudService.save(walletOrigin) //Update wallet origin
                                .flatMap(walletUpdate -> walletCrudService.findByNumberPhone(bootcoinTransactionDto.getDestinyNumberPhone()))
                                .flatMap(walletDestiny -> {
                                    walletDestiny.setAmount(walletDestiny.getAmount() + bootcoinTransactionDto.getAmount());
                                    return walletCrudService.save(walletDestiny); //Update wallet destiny
                                }).subscribe(w -> log.info("Updated All"));
                        bootcoinTransactionDto.setState(BootcoinTransactionDto.State.SUCCESSFUL);
                    } else {
                        //Transaccion rechazada
                        bootcoinTransactionDto.setState(BootcoinTransactionDto.State.REJECTED);
                    }
                    this.bootcoinProducer.producer(bootcoinTransactionDto);

                    return Mono.just(walletOrigin);
                }).subscribe();
    }

}