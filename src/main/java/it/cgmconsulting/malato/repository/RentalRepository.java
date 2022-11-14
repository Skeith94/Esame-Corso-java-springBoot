package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Inventory;
import it.cgmconsulting.malato.entity.Rental;
import it.cgmconsulting.malato.entity.RentalId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RentalRepository extends JpaRepository<Rental, RentalId>{
    @Query(value = "select r from Rental r where r.rentalId.inventoryId=:inventory",countQuery = "select count(r) from Rental r")
    List<Rental> getRentalsByInventorys(@Param("inventory") Inventory inventory, Pageable paging);
}
