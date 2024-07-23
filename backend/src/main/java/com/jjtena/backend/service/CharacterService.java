package com.jjtena.backend.service;

import com.jjtena.backend.dto.CharacterDTO;
import reactor.core.publisher.Mono;

public interface CharacterService {
    Mono<CharacterDTO> getCharacter(String name);
}
