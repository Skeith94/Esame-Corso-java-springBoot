package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.FilmStaff;
import it.cgmconsulting.malato.entity.FilmStaffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmStaffRepository extends JpaRepository<FilmStaff,FilmStaffId> {

}
