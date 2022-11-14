package it.cgmconsulting.malato.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class FilmStaffId implements Serializable {
    @ManyToOne
    @JoinColumn(name="filmId", nullable = false)
    private Film filmId;
    @ManyToOne
    @JoinColumn(name="staffId", nullable = false)
    private Staff staffId;
    @ManyToOne
    @JoinColumn(name="roleId", nullable = false)
    private Role roleId;

}
