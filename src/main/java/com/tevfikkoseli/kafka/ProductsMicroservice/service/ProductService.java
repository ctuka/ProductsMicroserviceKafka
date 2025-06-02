package com.tevfikkoseli.kafka.ProductsMicroservice.service;

import com.tevfikkoseli.kafka.ProductsMicroservice.models.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel createProductRestModel) throws Exception;
}
