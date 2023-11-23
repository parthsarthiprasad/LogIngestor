package com.parthsarthiprasad.tradewind.service;

import com.parthsarthiprasad.tradewind.model.LogList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.apache.lucene.document.*;

import java.util.ArrayList;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "${kafka.group.id}")
    public void listen(@Payload ArrayList<LogList> logInjectorList) {

        log.info("Message received. level : {} ", logInjectorList);


        // Add a document to the index
        Document document = new Document();
        document.add(new TextField("name", "John Doe", Field.Store.YES));
        document.add(new TextField("address", "80 Summer Hill", Field.Store.YES));


    }
    // here we add lucene to index the data and add it to document
}
