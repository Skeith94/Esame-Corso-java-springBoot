package it.cgmconsulting.malato.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;
    @ManyToOne
    @JoinColumn(name="storeId", nullable = false)
    private Store storeId;
    @ManyToOne
    @JoinColumn(name="filmId", nullable = false)
    private Film filmId;

    public Inventory(Store storeId, Film filmId) {
        this.storeId = storeId;
        this.filmId = filmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inventory inventory = (Inventory) o;

        return Objects.equals(inventoryId, inventory.inventoryId);
    }

    @Override
    public int hashCode() {
        return inventoryId != null ? inventoryId.hashCode() : 0;
    }
}

