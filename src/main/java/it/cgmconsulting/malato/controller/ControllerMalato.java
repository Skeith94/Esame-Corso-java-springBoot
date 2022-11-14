package it.cgmconsulting.malato.controller;

import it.cgmconsulting.malato.entity.*;
import it.cgmconsulting.malato.payload.request.AddUpdateRentalRequest;
import it.cgmconsulting.malato.payload.request.FilmRequest;
import it.cgmconsulting.malato.payload.response.CustomerStoreResponse;
import it.cgmconsulting.malato.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
public class ControllerMalato {

    @Autowired FilmService filmService;
    @Autowired GenreService genreService;
    @Autowired LanguageService languageService;
    @Autowired StoreService storeService;
    @Autowired InventoryService inventoryService;
    @Autowired CustomerService customerService;
    @Autowired RentalService rentalService;




    @Transactional
    @PutMapping("/update-film/{filmId}")
    public ResponseEntity<?>updateFilm(@PathVariable Long filmId,@Valid @RequestBody FilmRequest request){
        Optional<Film> film = filmService.findById(filmId);
        short currentYears = (short) LocalDateTime.now().getYear();

        if(film.isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }

    if(!request.getTitle().isEmpty()){
        film.get().setTitle(request.getTitle());
    }

    if(!request.getDescription().isEmpty())  {
        film.get().setDescription(request.getDescription());
    }

    if(request.getReleaseYear()!=null && request.getReleaseYear()<currentYears){
        film.get().setReleaseYear(request.getReleaseYear());
    }

    Optional<Language> language = languageService.findById(request.getLanguageId());
        language.ifPresent(value -> film.get().setLanguageId(value));

    Optional<Genre>genre=genreService.findById(request.getGenreId());
        genre.ifPresent(value -> film.get().setGenreId(value));

  // solo i campi validi vengono aggiornati,quelli non vengono ignorati;visualizzo il risultato finale del aggiornamento
        return new ResponseEntity<>(film.get(),HttpStatus.OK);
    }

    @GetMapping("/find-film-in-store/{filmId}")
    public ResponseEntity<?>findFilmStore(@PathVariable Long filmId){

        if(filmService.findById(filmId).isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(filmService.getFilmStore(filmId),HttpStatus.OK);
    }

    @GetMapping("/find-films-by-language/{languageId}")
    public ResponseEntity<?>findFilmByLanguage(@PathVariable Long languageId ){
        if(languageService.findById(languageId).isEmpty()){
            return new ResponseEntity<>("language not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filmService.getFilmByLanguage(languageId),HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/add-film-to-store/{storeId}/{filmId}")
    public ResponseEntity<?>addFilmStore(@PathVariable Long storeId,@PathVariable Long filmId){
       Optional<Store>store=storeService.findByStore(storeId);

       if(store.isEmpty()){
           return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
       }

       Optional<Film>film=filmService.findById(filmId);

        if(film.isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }

        inventoryService.save(store.get(),film.get());
        return new ResponseEntity<>("film "+film.get().getTitle()+" added to "+store.get().getStoreName(),HttpStatus.OK);
    }

    @GetMapping("/count-customers-by-store/{storeName}")
    public ResponseEntity<?>countCustomerStore(@PathVariable String storeName){
        Optional<Store> store = storeService.findByStoreName(storeName);
        if(store.isEmpty()){
            return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<>(new CustomerStoreResponse(store.get().getStoreName(),storeService.countCustomerStore(store.get())),HttpStatus.OK);
    }
    @Transactional
    @PostMapping("/add-update-rental")
    public ResponseEntity<?>addUpdateRental(@Valid @RequestBody AddUpdateRentalRequest request){
        Optional<Store> store = storeService.findByStore(request.getStoreId());

        if(store.isEmpty()){
            return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
        }

        Optional<Customer> customer = customerService.findById(request.getCustomerId());

        if(customer.isEmpty()){
            return new ResponseEntity<>("customer not found",HttpStatus.NOT_FOUND);
        }

        Optional<Film>film=filmService.findById(request.getFilmId());

        if(film.isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }

        List<Inventory>inventories;
        inventories=inventoryService.findInventories(film.get(),store.get());

       for(Inventory i:inventories){
           List<Rental> rentals = rentalService.getRentalsByInventory(i);
           if(rentals.isEmpty() || rentals.get(0).getRentalReturn().isBefore(LocalDateTime.now())){ // in caso non e mai stato affittato noleggio il film al nuovo cliente oppure ultima volta che e stato affittato e stato gia consegna in quel caso il prodotto e disponibile
               Rental newRental=new Rental(new RentalId(customer.get(),i, LocalDateTime.now()),LocalDateTime.now().plusDays(request.getDaysRental()));
               rentalService.save(newRental);
               return new ResponseEntity<>("new rentals saved expired at: "+ newRental.getRentalReturn()+ " user:"+ newRental.getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }
           // il prodotto e gia noleggiato da qualcuno ,se e lo stesso di chi lo ha noleggiato in precedenza  aggiorno la data altrimenti continuo a cercare altri film nel inventario
           if(rentals.get(0).getRentalId().getCustomerId().equals(customer.get())){
               rentals.get(0).setRentalReturn(rentals.get(0).getRentalReturn().plusDays(request.getDaysRental()));
               return new ResponseEntity<>("rentals updated,expired at: "+rentals.get(0).getRentalReturn()+ " user:"+ rentals.get(0).getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }

       }

       return new ResponseEntity<>("film "+film.get().getTitle()+" not available at store "+store.get().getStoreName(),HttpStatus.OK);
    }


}
