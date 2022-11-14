package it.cgmconsulting.malato.entity;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @EmbeddedId
    private RentalId rentalId;
    private LocalDateTime rentalReturn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rental rental = (Rental) o;

        return Objects.equals(rentalId, rental.rentalId);
    }

    @Override
    public int hashCode() {
        return rentalId != null ? rentalId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId +
                ", rentalReturn=" + rentalReturn +
                '}';
    }
}
