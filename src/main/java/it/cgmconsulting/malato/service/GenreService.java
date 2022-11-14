package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Genre;
import it.cgmconsulting.malato.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;
    public Optional<Genre>findById(Long genreId){
        return genreRepository.findById(genreId);
    }

}
