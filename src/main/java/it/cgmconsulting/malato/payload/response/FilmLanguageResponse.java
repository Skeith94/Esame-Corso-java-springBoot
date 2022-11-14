package it.cgmconsulting.malato.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class FilmLanguageResponse {
    private Long filmId;
    private String title;
    private String storeName;
    private short releaseYear;
    private String languageName;

}
