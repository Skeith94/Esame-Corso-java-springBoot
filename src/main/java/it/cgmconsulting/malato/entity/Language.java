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
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long languageId;

    @Column(length=60)
    private String languageName;

    public Language(String languageName) {
        this.languageName = languageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        return Objects.equals(languageId, language.languageId);
    }

    @Override
    public int hashCode() {
        return languageId != null ? languageId.hashCode() : 0;
    }
}
