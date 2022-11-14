package it.cgmconsulting.malato.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class RentalId implements Serializable {
    @ManyToOne
    @JoinColumn(name="customerId", nullable = false)
    private Customer customerId;

    @ManyToOne
    @JoinColumn(name="inventoryId",nullable = false)
    private Inventory inventoryId;

    private LocalDateTime rentalDate;

    public RentalId(Customer customerId, Inventory inventoryId, LocalDateTime rentalDate) {
        this.customerId = customerId;
        this.inventoryId = inventoryId;
        this.rentalDate = rentalDate;
    }


}
