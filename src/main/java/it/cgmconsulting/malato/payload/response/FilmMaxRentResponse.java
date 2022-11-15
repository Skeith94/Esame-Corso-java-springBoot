package it.cgmconsulting.malato.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter  @NoArgsConstructor @AllArgsConstructor
public class FilmMaxRentResponse {
    Long filmId;
    String title;

    Long rentTot;

}
