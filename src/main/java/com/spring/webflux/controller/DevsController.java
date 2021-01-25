package com.spring.webflux.controller;

import com.spring.webflux.model.Devs;
import com.spring.webflux.repository.DevsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class DevsController {

    @Autowired
    private DevsRepository devsRepository;

    @PostMapping("/dev")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Devs> createDev (@Valid @RequestBody Devs devs) {
        return devsRepository.save(devs);
    }

    @GetMapping("/dev")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Devs> getAllDev () {
        return devsRepository.findAll();
    }

    @GetMapping("/dev/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Devs>> getDevById (@PathVariable (value = "id") String devId) {
        return devsRepository.findById(devId)
                .map(dev -> ResponseEntity.ok(dev))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/dev/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Devs>> updateDev (
            @PathVariable (value = "id") String devId,
            @Valid @RequestBody Devs devs
    ) {
        return devsRepository.findById(devId)
                .flatMap(existDev -> {
                    existDev.setEmail(devs.getEmail());
                    existDev.setName(devs.getName());
                    existDev.setStack(devs.getStack());
                    return devsRepository.save(existDev);
                }).map(updateDev -> new ResponseEntity<>(updateDev, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/dev/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteDev (@PathVariable (value = "id") String devId) {
        return devsRepository.findById(devId)
                .flatMap(existDev ->
                        devsRepository.delete(existDev)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping(value = "/stream/devs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Devs> streamAllDevs() {
        return devsRepository.findAll();
    }
}
