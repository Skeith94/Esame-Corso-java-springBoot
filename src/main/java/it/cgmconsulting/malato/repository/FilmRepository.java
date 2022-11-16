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


    @Query(value="select subquery1.filmId,count(filmId)as tot,f.title from  " +
            " (select  " +
            "    film0_.film_id as filmId  " +
            " from  " +
            "    film film0_  " +
            "        inner join  " +
            "    film_staff filmstaff1_  " +
            "    on ( " +
            "            film0_.film_id=filmstaff1_.film_id  " +
            "        ) " +
            "        inner join " +
            "    staff staff2_  " +
            "    on ( " +
            "            filmstaff1_.staff_id=staff2_.staff_id " +
            "        ) " +
            "        inner join  " +
            "    role role3_  " +
            "    on ( " +
            "            role3_.role_name='ACTOR'  " +
            "        ) cross   " +
            "            join  " +
            "    staff staff4_ " +
            " where " +
            "        filmstaff1_.staff_id=staff4_.staff_id " +
            "  and  " +
            "        staff4_.lastname in  " +
            "        (:lastNames) " +
            " " +
            " group by " +
            "    film0_.film_id, " +
            "    staff2_.lastname)subquery1 inner join film f on subquery1.filmId=f.film_id group by subquery1.filmId " +
            " having count(film_id)=:numActor",nativeQuery = true)
    List<FilmResponse> getFilmByAuthors(@Param("lastNames")List<String>lastNames,@Param("numActor")int numActor);



}
