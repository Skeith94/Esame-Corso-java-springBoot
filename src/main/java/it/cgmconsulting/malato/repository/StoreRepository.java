package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Store;
import it.cgmconsulting.malato.payload.response.CustomerStoreResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

    @Query("select s from Store s where s.storeName=:storeName")
    Optional<Store>findByStoreName(@Param("storeName") String storeName);

    @Query("select count(distinct (c.customerId)) from Store s left join Inventory i on s=i.storeId left join Rental r on i=r.rentalId.inventoryId left join Customer c on r.rentalId.customerId=c where s=:store")
    Long countCustomerStore(@Param("store")Store store);
}
