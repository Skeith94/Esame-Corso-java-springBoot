package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.*;
import it.cgmconsulting.malato.payload.response.FilmMaxRentResponse;
import it.cgmconsulting.malato.payload.response.FilmRentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface RentalRepository extends JpaRepository<Rental, RentalId>{
    @Query(value = "select r from Rental r where r.rentalId.inventoryId=:inventory",countQuery = "select count(r) from Rental r")
    List<Rental> getRentalsByInventorys(@Param("inventory") Inventory inventory, Pageable paging);

    @Query(value = "select count(r) from Rental r where r.rentalId.rentalDate between :dateStart and :dateEnd and r.rentalId.inventoryId in :inventories ")
    Integer getRentalsBetween(@Param("inventories") List<Inventory>inventories, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd")LocalDateTime dateEnd);

   @Query(value="select new it.cgmconsulting.malato.payload.response.FilmRentResponse(r.rentalId.inventoryId.filmId.filmId,r.rentalId.inventoryId.filmId.title,r.rentalId.inventoryId.storeId.storeName)from Rental r where r.rentalId.customerId=:customer")
   List<FilmRentResponse>getFilmRent(@Param("customer") Customer customer);

   @Query(value = "select new it.cgmconsulting.malato.payload.response.FilmMaxRentResponse(r.rentalId.inventoryId.filmId.filmId,r.rentalId.inventoryId.filmId.title, count(r.rentalId.inventoryId)as tot) from Rental r group by r.rentalId.inventoryId.filmId.filmId,r.rentalId.inventoryId.filmId.title order by tot desc ")
   List<FilmMaxRentResponse>findFilmMaxRent();



}
