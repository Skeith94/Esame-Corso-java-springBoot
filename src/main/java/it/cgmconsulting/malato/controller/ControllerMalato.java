package it.cgmconsulting.malato.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.cgmconsulting.malato.entity.*;
import it.cgmconsulting.malato.payload.request.AddUpdateRentalRequest;
import it.cgmconsulting.malato.payload.request.FilmRequest;
import it.cgmconsulting.malato.payload.response.*;
import it.cgmconsulting.malato.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired TraceMongoService traceMongoService;

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Transactional
    @PutMapping("/update-film/{filmId}")
    public ResponseEntity<?>updateFilm(@PathVariable Long filmId,@Valid @RequestBody FilmRequest request,HttpServletRequest requestHttp) throws JsonProcessingException {
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
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(), ow.writeValueAsString(request), ow.writeValueAsString(film.get()));
        return new ResponseEntity<>(film.get(),HttpStatus.OK);
    }

    @GetMapping("/find-film-in-store/{filmId}")
    public ResponseEntity<?>findFilmStore(@PathVariable Long filmId,HttpServletRequest requestHttp) throws JsonProcessingException {

        if(filmService.findById(filmId).isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }
        List<FilmStoreResponse> films = filmService.getFilmStore(filmId);
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(), ow.writeValueAsString(""), ow.writeValueAsString(films));
        return new ResponseEntity<>(films,HttpStatus.OK);
    }

    @GetMapping("/find-films-by-language/{languageId}")
    public ResponseEntity<?>findFilmByLanguage(@PathVariable Long languageId,HttpServletRequest requestHttp) throws JsonProcessingException {
        if(languageService.findById(languageId).isEmpty()){
            return new ResponseEntity<>("language not found",HttpStatus.NOT_FOUND);
        }
        List<FilmLanguageResponse> films = filmService.getFilmByLanguage(languageId);
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(), ow.writeValueAsString(""), ow.writeValueAsString(films));
        return new ResponseEntity<>(films,HttpStatus.OK);
    }


    @Transactional
    @PostMapping("/add-film-to-store/{storeId}/{filmId}")
    public ResponseEntity<?>addFilmStore(@PathVariable Long storeId,@PathVariable Long filmId,HttpServletRequest requestHttp) throws JsonProcessingException {
       Optional<Store>store=storeService.findByStore(storeId);

       if(store.isEmpty()){
           return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
       }

       Optional<Film>film=filmService.findById(filmId);

        if(film.isEmpty()){
            return new ResponseEntity<>("film not found", HttpStatus.NOT_FOUND);
        }
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(), ow.writeValueAsString(""), ow.writeValueAsString(""));
        inventoryService.save(store.get(),film.get());
        return new ResponseEntity<>("film "+film.get().getTitle()+" added to "+store.get().getStoreName(),HttpStatus.OK);
    }

    @GetMapping("/count-customers-by-store/{storeName}")
    public ResponseEntity<?>countCustomerStore(@PathVariable String storeName,HttpServletRequest requestHttp) throws JsonProcessingException {
        Optional<Store> store = storeService.findByStoreName(storeName);
        if(store.isEmpty()){
            return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
        }
        CustomerStoreResponse customerStoreResponse = new CustomerStoreResponse(store.get().getStoreName(), storeService.countCustomerStore(store.get()));
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(), ow.writeValueAsString(" "), ow.writeValueAsString(customerStoreResponse));
       return new ResponseEntity<>(customerStoreResponse,HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add-update-rental")
    public ResponseEntity<?>addUpdateRental(@Valid @RequestBody AddUpdateRentalRequest request,HttpServletRequest requestHttp) throws JsonProcessingException {
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
           //nel caso il rental return ritorna null,
           if(Objects.isNull(rentals.get(0).getRentalReturn())){
               continue;
           }
           // in caso non e mai stato noleggiato il film oppure ultima volta che e stato noleggiato e stato gia consegnato in quel caso il prodotto e disponibile
           if(rentals.get(0).getRentalReturn().isBefore(LocalDateTime.now())){
               Rental newRental=new Rental(new RentalId(customer.get(),i, LocalDateTime.now()),LocalDateTime.now().plusDays(request.getDaysRental()));
               rentalService.save(newRental);
               traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(request),ow.writeValueAsString(""));
               return new ResponseEntity<>("new rentals saved expired at: "+ newRental.getRentalReturn()+ " user:"+ newRental.getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }
           // il prodotto e gia noleggiato da qualcuno ,se e a fare la richiesta e lo stesso di chi lo ha noleggiato in precedenza  aggiorno la data altrimenti continuo a cercare negli altri inventari
           if(rentals.get(0).getRentalId().getCustomerId().equals(customer.get())){
               rentals.get(0).setRentalReturn(rentals.get(0).getRentalReturn().plusDays(request.getDaysRental()));
               traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(request),ow.writeValueAsString(""));
               return new ResponseEntity<>("rentals updated,expired at: "+rentals.get(0).getRentalReturn()+ " user:"+ rentals.get(0).getRentalId().getCustomerId().getEmail(),HttpStatus.OK);
           }
       }
       traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(request),ow.writeValueAsString("NoSaved"));
       return new ResponseEntity<>("film "+film.get().getTitle()+" not available at store "+store.get().getStoreName(),HttpStatus.OK);

    }

    @GetMapping("/count-rentals-in-date-range-by-store/{storeId}")
    ResponseEntity<?>countRentalsDate(@PathVariable Long storeId, @NotNull @RequestParam(defaultValue = "2000/01/01")  String dateStartRequest,@NotNull @RequestParam(defaultValue = "2008/02/03")  String dateEndRequest,HttpServletRequest requestHttp) throws JsonProcessingException {

        String[] dateParts = dateStartRequest.split("/");
        LocalDateTime dateStart;
        LocalDateTime dateEnd;
        try {
            dateStart = LocalDateTime.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]), 0, 0,0);
            dateParts = dateEndRequest.split("/");
            dateEnd = LocalDateTime.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]), 0, 0,0);
        }catch (Exception e){
            return new ResponseEntity<>("wrong date format yyyy/dd/gg",HttpStatus.BAD_REQUEST);
        }

        Optional<Store> store = storeService.findByStore(storeId);

        if(store.isEmpty()){
            return new ResponseEntity<>("store not found",HttpStatus.NOT_FOUND);
        }

        int rentals = rentalService.getRentalsBetween(store.get(), dateStart, dateEnd);
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(dateStartRequest+"||"+dateEndRequest),ow.writeValueAsString(rentals));
        return new ResponseEntity<>(rentals,HttpStatus.OK);
    }

    @GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
    ResponseEntity<?>rentByOneCustomer(@PathVariable Long customerId,HttpServletRequest requestHttp) throws JsonProcessingException {

        Optional<Customer> customer = customerService.findById(customerId);

        if(customer.isEmpty()){
            return new ResponseEntity<>("customer not found",HttpStatus.NOT_FOUND);
        }

        List<FilmRentResponse> filmRentResponses = rentalService.getFilmRent(customer.get());

        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(""),ow.writeValueAsString(filmRentResponses));

    return  new ResponseEntity<>(filmRentResponses,HttpStatus.OK);

    }

    @GetMapping("/find-film-with-max-number-of-rent")
    ResponseEntity<?>findFilmMaxRent(HttpServletRequest requestHttp) throws JsonProcessingException {
        List<FilmMaxRentResponse> films = rentalService.findFilmMaxRent();
        List<FilmMaxRentResponse>maxRentfilms = new ArrayList<>();
        // la lista ritornando ordinata sono sicuro che il primo elemento e quello con piu rent,controllo se ce ne sono altri con lo stesso numero di rent
        maxRentfilms.add(films.get(0));
        films.remove(0);
        films.stream().filter(f-> Objects.equals(maxRentfilms.get(0).getRentTot(), f.getRentTot())).forEach(maxRentfilms::add);
        traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(""),ow.writeValueAsString(maxRentfilms));
        return new ResponseEntity<>(maxRentfilms,HttpStatus.OK);
    }

    @GetMapping("/find-films-by-actors")
    ResponseEntity<?>findFilmsByActors(@RequestParam @NotEmpty List<String>lastNames,HttpServletRequest requestHttp) throws JsonProcessingException {
    List<FilmResponse> films = filmService.getFilmByAuthors(lastNames);
    traceMongoService.SaveTraceMongo(requestHttp.getRequestURI(),ow.writeValueAsString(lastNames),ow.writeValueAsString(films));
    return new ResponseEntity<>(films,HttpStatus.OK);
    }

}

