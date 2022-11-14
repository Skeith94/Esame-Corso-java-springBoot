package it.cgmconsulting.malato.payload.request;

import it.cgmconsulting.malato.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
public class FilmRequest {
    @Size(min=2,max = 100)
    private String title;

    @Size(min=5)
    private String description;

    private Short releaseYear;

    private Long languageId;

    private Long genreId;

}
