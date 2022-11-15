package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Film;
import it.cgmconsulting.malato.entity.Inventory;
import it.cgmconsulting.malato.entity.Store;
import it.cgmconsulting.malato.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    InventoryRepository inventoryRepository;

    public void save(Store store, Film film){
        Inventory inventory=new Inventory(store,film);
        inventoryRepository.save(inventory);
    }

    public Optional<Inventory> findById(Long inventoryId){
        return inventoryRepository.findById(inventoryId);
    }

    public List<Inventory> findInventories(Film film,Store store){
        return inventoryRepository.findInventories(film,store);
    }

    public List<Inventory>findAllInventoryByStore(Store store){
        return inventoryRepository.findAllInventoryByStore(store);
    }



}
