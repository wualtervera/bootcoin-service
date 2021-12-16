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
    private BootcoinCrudService bootcoinCrudService;

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
        //BOOTCOIN-TRANSACION-SERVICE
        //1- Enviar solicitud al vendedor(consumidor)

        //BOOTCOIN-SERVICE
        //1- Recibir solicitud del comprador(productor)
        //2- Vendedor(consumidor) acepta la solicitud del comprador
        //3- Enviar confirmación/rechazo de la venta al comprador(consumidor)

        //BOOTCOIN-TRANSACION-SERVICE
        //3- recibe la cofirmación para realizar/rechazar transaccion


        //BUSCAR VENDEDOR POR ID
        bootcoinCrudService.findByNumberPhone(bootcoinTransactionDto.getPhoneSeller())
                .flatMap(boocoinSeller-> {
                    //Venededor tiene bootcoins en su cuenta BOOTCOIN?
                    if (boocoinSeller.getAmountBooCoins() >= bootcoinTransactionDto.getAmountCoins()) {

                        //Restamos los bootcoins de su BOOTCOIN
                        boocoinSeller.setAmountBooCoins((boocoinSeller.getAmountBooCoins() - bootcoinTransactionDto.getAmountCoins()));
                        bootcoinCrudService.save(boocoinSeller) //Update botcoin del vendedor

                                //BUSCAR COMPRADOR POR ID
                                .flatMap(bootcoinUpdate -> bootcoinCrudService.findByNumberPhone(bootcoinTransactionDto.getPhoneBuyer()))
                                .flatMap(boocoinBuyer -> {

                                    //Sumamos los bootcoins de su cuenta BOOTCOIN
                                    boocoinBuyer.setAmountBooCoins(boocoinBuyer.getAmountBooCoins() + bootcoinTransactionDto.getAmountCoins());
                                    return bootcoinCrudService.save(boocoinBuyer); //Update bootcoin de comprador

                                }).subscribe(w -> log.info("Updated All"));

                        bootcoinTransactionDto.setState(BootcoinTransactionDto.State.ACCEPTED);
                    } else {
                        //Transaccion rechazada
                        bootcoinTransactionDto.setState(BootcoinTransactionDto.State.REJECTED);
                    }
                    //CONFIRMAR VENTA
                    this.bootcoinProducer.producer(bootcoinTransactionDto);

                    return Mono.just(boocoinSeller);
                }).subscribe();
    }

}