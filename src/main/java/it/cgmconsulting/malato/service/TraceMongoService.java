package it.cgmconsulting.malato.service;

import it.cgmconsulting.malato.entity.TraceMongo;
import it.cgmconsulting.malato.payload.response.DateMongoResponse;
import it.cgmconsulting.malato.repository.TraceMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TraceMongoService {
    @Autowired
    TraceMongoRepository traceMongoRepository;

    public void  SaveTraceMongo(String uri,String request,String response){
        traceMongoRepository.save(new TraceMongo(uri,LocalDateTime.now(),request,response));
    }



    public List<DateMongoResponse> getAll(LocalDateTime dateStart, LocalDateTime  dateEnd) {
      return traceMongoRepository.findByDateBetween(dateStart,dateEnd);
    }

    public List<TraceMongo> findTraceMongosByUri(String uri){
        return traceMongoRepository.findByUriIsContaining(uri);
    }
}
