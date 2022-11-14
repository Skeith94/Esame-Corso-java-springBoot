package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Store;
import it.cgmconsulting.malato.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;

    public Optional<Store> findByStore(Long storeId){
        return storeRepository.findById(storeId);
    }

    public Optional<Store>findByStoreName(String storeName){
        return storeRepository.findByStoreName(storeName);
    }

    public Long countCustomerStore(Store store){
        return storeRepository.countCustomerStore(store);
    }
}
