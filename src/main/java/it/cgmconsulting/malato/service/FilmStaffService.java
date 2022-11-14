package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.repository.FilmStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmStaffService {
    @Autowired
    FilmStaffRepository filmStaffRepository;
}
