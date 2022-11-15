package it.cgmconsulting.malato.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RentalId rentalId = (RentalId) o;

        if (!Objects.equals(customerId, rentalId.customerId)) return false;
        if (!Objects.equals(inventoryId, rentalId.inventoryId))
            return false;
        return Objects.equals(rentalDate, rentalId.rentalDate);
    }

    @Override
    public int hashCode() {
        int result = customerId != null ? customerId.hashCode() : 0;
        result = 31 * result + (inventoryId != null ? inventoryId.hashCode() : 0);
        return result;
    }
}
