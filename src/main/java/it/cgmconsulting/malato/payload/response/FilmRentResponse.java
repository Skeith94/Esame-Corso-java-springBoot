package it.cgmconsulting.malato.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter
public class FilmRentResponse {
    Long filmId;
    String title;
    String storeName;
}
