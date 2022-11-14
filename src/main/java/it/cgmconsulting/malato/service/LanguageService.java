package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.Language;
import it.cgmconsulting.malato.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LanguageService {
   @Autowired
   LanguageRepository languageRepository;

   public Optional<Language> findById(Long languageId){
      return languageRepository.findById(languageId);
   }
}
