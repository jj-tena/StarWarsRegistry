package com.jjtena.backend.controller;

import com.jjtena.backend.dto.CharacterDTO;
import com.jjtena.backend.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CharacterController {

    @Autowired
    private CharacterService service;

    @GetMapping("/swapi-proxy/person-info")
    public Mono<ResponseEntity<CharacterDTO>> getPersonInfo(@RequestParam String name) {
        return service.getCharacter(name)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }
}
