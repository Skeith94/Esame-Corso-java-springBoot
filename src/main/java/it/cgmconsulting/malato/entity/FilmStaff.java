package it.cgmconsulting.malato.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class FilmStaff {
   @EmbeddedId
   private FilmStaffId filmStaffId;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      FilmStaff filmStaff = (FilmStaff) o;

      return Objects.equals(filmStaffId, filmStaff.filmStaffId);
   }

   @Override
   public int hashCode() {
      return filmStaffId != null ? filmStaffId.hashCode() : 0;
   }
}
