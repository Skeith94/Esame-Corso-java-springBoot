package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.Film;
import it.cgmconsulting.malato.payload.response.FilmLanguageResponse;
import it.cgmconsulting.malato.payload.response.FilmResponse;
import it.cgmconsulting.malato.payload.response.FilmStoreResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film,Long> {


    @Query(value="select new it.cgmconsulting.malato.payload.response.FilmStoreResponse(f.filmId,f.title,s.storeName)from Film f inner join Inventory i on f= i.filmId inner join Store s on i.storeId=s where f.filmId=:filmId")
    List<FilmStoreResponse>getFilmStore(@Param("filmId")Long filmId);

    @Query(value="select new it.cgmconsulting.malato.payload.response.FilmLanguageResponse(f.filmId,f.title,f.description,f.releaseYear,l.languageName)from Film f inner join Language l on f.languageId=l where l.languageId=:languageId")
    List<FilmLanguageResponse>getFilmByLanguage(@Param("languageId")Long languageId);


    @Query(value="select new it.cgmconsulting.malato.payload.response.FilmResponse(f.filmId,f.title,count(f.filmId) as tot) from Film f join FilmStaff fs on f=fs.filmStaffId.filmId where fs.filmStaffId.staffId.lastname in :lastNames group by f.filmId,f.title,f.filmId order by f.title")
    List<FilmResponse> getFilmByAuthors(@Param("lastNames")List<String>lastNames);



}
