package it.cgmconsulting.malato.controller;

import it.cgmconsulting.malato.entity.*;
import it.cgmconsulting.malato.payload.request.AddUpdateRentalRequest;
import it.cgmconsulting.malato.payload.request.FilmRequest;
import it.cgmconsulting.malato.payload.response.CustomerStoreResponse;
import it.cgmconsulting.malato.payload.response.FilmMaxRentResponse;
import it.cgmconsulting.malato.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Validated
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
           //ritorno solo la rentals di quel determinato inventario con la consegna piu recente grazie al pageable
           List<Rental> rentals = rentalService.getRentalsByInventory(i);
           // in caso non e mai stato noleggiato il film oppure ultima volta che e stato noleggiato e stato gia consegnato in quel caso il prodotto e disponibile
           if(rentals.isEmpty() || rentals.get(0).getRentalReturn().isBefore(LocalDateTime.now())){
               Rental newRental=new Rental(new RentalId(customer.get(),i, LocalDateTime.now()),LocalDateTime.now().plusDays(request.getDaysRental()));
               rentalService.save(newRental);
               return new ResponseEntity<>("new rentals saved expired at: "+ newRental.getRentalReturn()+ " user:"+ newRental.getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }
           // il prodotto e gia noleggiato da qualcuno ,se e a fare la richiesta e lo stesso di chi lo ha noleggiato in precedenza  aggiorno la data altrimenti continuo a cercare negli altri inventari
           if(rentals.get(0).getRentalId().getCustomerId().equals(customer.get())){
               rentals.get(0).setRentalReturn(rentals.get(0).getRentalReturn().plusDays(request.getDaysRental()));
               return new ResponseEntity<>("rentals updated,expired at: "+rentals.get(0).getRentalReturn()+ " user:"+ rentals.get(0).getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }
       }
       return new ResponseEntity<>("film "+film.get().getTitle()+" not available at store "+store.get().getStoreName(),HttpStatus.OK);
    }

    @GetMapping("/count-rentals-in-date-range-by-store/{storeId}")
    ResponseEntity<?>countRentalsDate(@PathVariable Long storeId, @RequestParam @NotNull String dateStartRequest,@NotNull @RequestParam String dateEndRequest){


        String dateParts[] = dateStartRequest.split("/");
        LocalDateTime dateStart;
        LocalDateTime dateEnd;
        try {
            dateStart = LocalDateTime.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]), 0, 0,0);
            dateParts = dateEndRequest.split("/");
            dateEnd = LocalDateTime.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]), 0, 0,0);
        }catch (Exception e){
            return new ResponseEntity<>("wrong date format dd/mm/yy",HttpStatus.BAD_REQUEST);
        }

        Optional<Store> store = storeService.findByStore(storeId);

        if(store.isEmpty()){
            return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
        }
        List<Inventory> inventories = inventoryService.findAllInventoryByStore(store.get());

        int rentals = rentalService.getRentalsBetween(inventories, dateStart, dateEnd);

        return new ResponseEntity<>(rentals,HttpStatus.OK);
    }

    @GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
    ResponseEntity<?>rentByOneCustomer(@PathVariable Long customerId){

        Optional<Customer> customer = customerService.findById(customerId);

        if(customer.isEmpty()){
            return new ResponseEntity<>("customer not found",HttpStatus.NOT_FOUND);
        }

    return  new ResponseEntity<>(rentalService.getFilmRent(customer.get()),HttpStatus.OK);

    }

    @GetMapping("/find-film-with-max-number-of-rent")
    ResponseEntity<?>findFilmMaxRent(){

        List<FilmMaxRentResponse> films = rentalService.findFilmMaxRent();
        List<FilmMaxRentResponse>maxRentfilms = new ArrayList<>();
        // la lista ritornando ordinata sono sicuro che il primo elemento e quello con piu rent,controllo se ce ne sono altri con lo stesso numero di rent
        maxRentfilms.add(films.get(0));
        films.remove(0);
        films.stream().filter(f-> Objects.equals(maxRentfilms.get(0).getRentTot(), f.getRentTot())).forEach(maxRentfilms::add);

        return new ResponseEntity<>(maxRentfilms,HttpStatus.OK);
    }

    @GetMapping("/find-films-by-actors")
    ResponseEntity<?>findFilmsByActors(@RequestParam @NotEmpty List<String>lastNames){
     filmService.getFilmByAuthors(lastNames);
    return new ResponseEntity<>(filmService.getFilmByAuthors(lastNames),HttpStatus.OK);

    }


}
