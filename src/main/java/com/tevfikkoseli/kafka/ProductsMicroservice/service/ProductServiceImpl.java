package com.tevfikkoseli.kafka.ProductsMicroservice.service;

import com.tevfikkoseli.kafka.ProductsMicroservice.dto.ProductCreatedEvent;
import com.tevfikkoseli.kafka.ProductsMicroservice.models.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService{

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${topicname}")
    String topicName;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception{
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity());

        LOGGER.info("****** Before Publishing Product created event: ");

        // -------------------Synchronous--------------
        //to work it syncronously and adding throw to method signature
        SendResult<String,ProductCreatedEvent> result = kafkaTemplate.send(
                topicName, productId, productCreatedEvent).get();
        LOGGER.info("****** Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("****** Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("****** Offset: " + result.getRecordMetadata().offset());

        // -------------------Asynchronous--------------
        //to use kafka asynchronously create a CompletableFuture object just like below
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
//                kafkaTemplate.send(topicName, productId, productCreatedEvent);
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//               LOGGER.error("*****  Failed to send message: " + exception.getMessage());
//            } else {
//                LOGGER.info("*****  Message sent successfully: " + result.getRecordMetadata());
//            }
//
//        });
        //if you add join it wait and block the flow of app make it synchronously
        //future.join();

        LOGGER.info("****** Returning the product id. ...");
        return productId;
    }
}
