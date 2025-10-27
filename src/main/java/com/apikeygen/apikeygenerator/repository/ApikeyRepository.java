package com.apikeygen.apikeygenerator.repository;


import com.apikeygen.apikeygenerator.model.Key;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApikeyRepository extends MongoRepository<Key, String> {

}
