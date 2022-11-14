package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Film;
import it.cgmconsulting.malato.entity.Inventory;
import it.cgmconsulting.malato.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    @Query("select i from Inventory i where i.storeId=:store and i.filmId=:film")
    List<Inventory> findInventories(@Param("film") Film film,@Param("store") Store store);

}
