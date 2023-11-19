package com.parthsarthiprasad.LogInjectorDyte.controller;

import com.parthsarthiprasad.LogInjectorDyte.model.KafkaMessage;
import com.parthsarthiprasad.LogInjectorDyte.service.KafkaProducerService;
import com.parthsarthiprasad.LogInjectorDyte.service.lucene.LuceneIndexer;
import com.parthsarthiprasad.LogInjectorDyte.service.lucene.LuceneSearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProduceMessageController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/produce")
    public void sendMessage(@RequestBody List<KafkaMessage> kafkaMessageList) {
        kafkaProducerService.sendMessage(kafkaMessageList);

    }
}