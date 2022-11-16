package it.cgmconsulting.malato.payload.response;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface FilmResponse {
    Long getFilmId();
    @JsonIgnore
    Long gettot();
    String getTitle();
}
