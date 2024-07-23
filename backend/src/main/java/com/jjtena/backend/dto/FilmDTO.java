package com.jjtena.backend.dto;

import lombok.Data;

@Data
public class FilmDTO {
    private String name;
    private String release_date;

    public FilmDTO(String name, String release_date) {
        this.name = name;
        this.release_date = release_date;
    }
}
