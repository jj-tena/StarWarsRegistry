package com.jjtena.backend.service;

import com.jjtena.backend.dto.CharacterDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CharacterServiceImpl implements CharacterService{

    @Override
    public Mono<CharacterDTO> getCharacter(String name) {
        return null;
    }
}
