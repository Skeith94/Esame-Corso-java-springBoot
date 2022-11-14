package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long>{
}
