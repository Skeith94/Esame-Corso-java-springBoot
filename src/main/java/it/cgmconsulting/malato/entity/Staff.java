package it.cgmconsulting.malato.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
    @Column(length = 50)
    private String firstname;
    @Column(length = 50)
    private String lastname;
    private LocalDate dob;

    public Staff(String firstname, String lastname, LocalDate dob) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Staff staff = (Staff) o;

        return Objects.equals(staffId, staff.staffId);
    }

    @Override
    public int hashCode() {
        return staffId != null ? staffId.hashCode() : 0;
    }
}
