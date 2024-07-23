package com.jjtena.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CharacterDTO {
    private String name;
    private String birth_year;
    private String gender;
    private String planet_name;
    private String fastest_vehicle_driven;
    private List<FilmDTO> films;
}
