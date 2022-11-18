package it.cgmconsulting.malato.payload.response;

import lombok.*;

@Getter @Setter  @NoArgsConstructor @AllArgsConstructor @ToString
public class FilmMaxRentResponse {
    Long filmId;
    String title;

    Long rentTot;

}
