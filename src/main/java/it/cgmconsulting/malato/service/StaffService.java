package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
    @Autowired
    StaffRepository staffRepository;
}
