package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Film;
import it.cgmconsulting.malato.payload.response.FilmLanguageResponse;
import it.cgmconsulting.malato.payload.response.FilmStoreResponse;
import it.cgmconsulting.malato.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    @Autowired
    FilmRepository filmRepository;

    public Optional<Film> findById(Long filmId){
        return filmRepository.findById(filmId);
    }

    public List<FilmStoreResponse> getFilmStore(Long filmId){
        return filmRepository.getFilmStore(filmId);
    }

    public List<FilmLanguageResponse>getFilmByLanguage(Long languageId){
        return filmRepository.getFilmByLanguage(languageId);
    }
}
