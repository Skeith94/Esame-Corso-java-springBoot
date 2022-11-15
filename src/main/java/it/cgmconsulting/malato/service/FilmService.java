package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Film;
import it.cgmconsulting.malato.payload.response.FilmLanguageResponse;
import it.cgmconsulting.malato.payload.response.FilmResponse;
import it.cgmconsulting.malato.payload.response.FilmStoreResponse;
import it.cgmconsulting.malato.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<FilmResponse> getFilmByAuthors(List<String>lastNames){
        List<FilmResponse> filmsAuthor = filmRepository.getFilmByAuthors(lastNames);
        List<FilmResponse> filmsFinal=new ArrayList<>();
       filmsAuthor.stream().filter(i->i.getN()==lastNames.size()).forEach(filmsFinal::add);
       return filmsFinal;
    }
}
