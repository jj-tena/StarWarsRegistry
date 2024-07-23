package com.jjtena.backend.service;

import com.jjtena.backend.dto.CharacterDTO;
import com.jjtena.backend.dto.FilmDTO;
import com.jjtena.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterServiceImpl implements CharacterService {

    private final WebClient webClient;

    @Autowired
    public CharacterServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://swapi.dev/api").build();
    }

    @Override
    public Mono<CharacterDTO> getCharacter(String name) {
        return webClient.get()
                .uri("/people/?search={name}", name)
                .retrieve()
                .bodyToMono(CharacterResponse.class)
                .flatMap(response -> {
                    if (response.getResults().isEmpty()) {
                        return Mono.error(new RuntimeException("Character not found"));
                    }
                    CharacterModel character = response.getResults().get(0);
                    return buildCharacterDto(character);
                });
    }

    private Mono<CharacterDTO> buildCharacterDto(CharacterModel character) {
        CharacterDTO characterInfo = new CharacterDTO();
        characterInfo.setName(character.getName());
        characterInfo.setBirth_year(character.getBirth_year());
        characterInfo.setGender(character.getGender());

        Mono<String> planetMono = webClient.get()
                .uri(character.getHomeworld())
                .retrieve()
                .bodyToMono(PlanetModel.class)
                .map(PlanetModel::getName)
                .onErrorReturn("Unknown");

        Mono<List<FilmDTO>> filmsMono = Flux.fromIterable(character.getFilms())
                .flatMap(filmUrl -> webClient.get()
                        .uri(filmUrl)
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                        .map(film -> new FilmDTO(film.getTitle(), film.getRelease_date())))
                .collectList()
                .onErrorResume(e -> Mono.just(Collections.emptyList()));

        Mono<String> fastestVehicleMono = Flux.concat(
                        Flux.fromIterable(character.getVehicles()),
                        Flux.fromIterable(character.getStarships()))
                .flatMap(vehicleUrl -> webClient.get()
                        .uri(vehicleUrl)
                        .retrieve()
                        .bodyToMono(VehicleModel.class))
                .collect(Collectors.maxBy(Comparator.comparingInt(v -> {
                    try {
                        return Integer.parseInt(v.getMax_atmosphering_speed());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })))
                .map(vehicle -> vehicle.map(VehicleModel::getName).orElse("Unknown"))
                .onErrorReturn("Unknown");

        return Mono.zip(planetMono, filmsMono, fastestVehicleMono)
                .map(tuple -> {
                    characterInfo.setPlanet_name(tuple.getT1());
                    characterInfo.setFilms(tuple.getT2());
                    characterInfo.setFastest_vehicle_driven(tuple.getT3());
                    return characterInfo;
                });
    }

}
