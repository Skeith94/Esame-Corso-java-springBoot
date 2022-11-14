package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Customer;
import it.cgmconsulting.malato.entity.Inventory;
import it.cgmconsulting.malato.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired CustomerRepository customerRepository;

    public Optional<Customer> findById(Long customerId){
        return customerRepository.findById(customerId);
    }
}
