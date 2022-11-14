package it.cgmconsulting.malato.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    @Column(length = 100)
    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition ="SMALLINT")
    private short releaseYear;

    @ManyToOne
    @JoinColumn(name="languageId", nullable = false)
    private Language languageId;

    @ManyToOne
    @JoinColumn(name="genreId", nullable = false)
    private Genre genreId;

    public Film(String title, String description, short releaseYear, Language languageId, Genre genreId) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.languageId = languageId;
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return Objects.equals(filmId, film.filmId);
    }

    @Override
    public int hashCode() {
        return filmId != null ? filmId.hashCode() : 0;
    }

}
