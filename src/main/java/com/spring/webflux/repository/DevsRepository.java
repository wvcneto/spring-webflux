package com.spring.webflux.repository;

import com.spring.webflux.model.Devs;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevsRepository extends ReactiveMongoRepository<Devs, String> {

}
