package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Inventory;
import it.cgmconsulting.malato.entity.Rental;
import it.cgmconsulting.malato.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalService {
    @Autowired
    RentalRepository rentalRepository;

    public List<Rental> getRentalsByInventory(Inventory inventory){
        return rentalRepository.getRentalsByInventorys(inventory,PageRequest.of(0,1, Sort.by(Sort.Direction.valueOf("DESC"),"rentalReturn")));
    }

    public void save(Rental rental){
        rentalRepository.save(rental);
    }
}
