package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.*;
import it.cgmconsulting.malato.payload.response.FilmMaxRentResponse;
import it.cgmconsulting.malato.payload.response.FilmRentResponse;
import it.cgmconsulting.malato.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class RentalService {
    @Autowired
    RentalRepository rentalRepository;

    public List<Rental> getRentalsByInventory(Inventory inventory){
        // simula una limit a 1 prendo la rentalReturn piu recente
        return rentalRepository.getRentalsByInventorys(inventory,PageRequest.of(0,1, Sort.by(Sort.Direction.valueOf("DESC"),"rentalReturn")));
    }

    public void save(Rental rental){
        rentalRepository.save(rental);
    }


    public Integer getRentalsBetween(Store store, LocalDateTime dateStart, LocalDateTime dateEnd){
        return rentalRepository.getRentalsBetween(store,dateStart,dateEnd);
    }

    public List<FilmRentResponse>getFilmRent(Customer customer){
        return rentalRepository.getFilmRent(customer);
    }

    public List<FilmMaxRentResponse>findFilmMaxRent(){
        return rentalRepository.findFilmMaxRent();
    }





}

