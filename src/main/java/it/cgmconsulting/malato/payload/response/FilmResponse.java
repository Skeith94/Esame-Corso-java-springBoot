package it.cgmconsulting.malato.payload.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonIgnoreProperties(value = {"n"})
@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class FilmResponse {
    Long filmId;
    String title;
    Long n;


}
