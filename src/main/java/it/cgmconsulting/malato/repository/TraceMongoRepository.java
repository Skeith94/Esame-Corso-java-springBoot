package it.cgmconsulting.malato.repository;

import it.cgmconsulting.malato.entity.TraceMongo;
import it.cgmconsulting.malato.payload.response.DateMongoResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TraceMongoRepository extends MongoRepository<TraceMongo,Long> {

    List<DateMongoResponse> findByDateBetween(LocalDateTime dateStart, LocalDateTime dateEnd);
    List<TraceMongo> findByUriIsContaining(String uri);
}
