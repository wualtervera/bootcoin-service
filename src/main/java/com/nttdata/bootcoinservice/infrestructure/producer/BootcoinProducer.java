package com.nttdata.bootcoinservice.infrestructure.producer;

import com.nttdata.bootcoinservice.infrestructure.model.dto.BootcoinTransactionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BootcoinProducer {

    @Value("${custom.kafka.topic-name-bootcoin}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, BootcoinTransactionDto> kafkaTemplate;

    public void producer(BootcoinTransactionDto bootcoinTransactionDto) {
        log.info("Sending message....");
        Message<BootcoinTransactionDto> message = MessageBuilder
                .withPayload(bootcoinTransactionDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();
        this.kafkaTemplate.send(message);
        log.info("Sent message!");
    }



}