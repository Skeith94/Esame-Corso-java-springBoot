package it.cgmconsulting.malato.controller;

import it.cgmconsulting.malato.repository.TraceMongoRepository;
import it.cgmconsulting.malato.service.TraceMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController("/tracemongo")
public class TraceMongoController {
    @Autowired
    TraceMongoService traceMongoService;
    @GetMapping("/getall")
    ResponseEntity<?>getAll(@NotNull @RequestParam(defaultValue = "2000/01/01")  String dateStartRequest, @NotNull @RequestParam(defaultValue = "2022/01/01")String dateEndRequest){
        String[] dateParts = dateStartRequest.split("/");
        LocalDateTime dateStart;
        LocalDateTime dateEnd;
        try {
            dateStart = LocalDateTime.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]), 0, 0,0);
            dateParts = dateEndRequest.split("/");
            dateEnd = LocalDateTime.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]), 0, 0,0);
        }catch (Exception e){
            return new ResponseEntity<>("wrong date format yyyy/dd/gg",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(traceMongoService.getAll(dateStart,dateEnd), HttpStatus.OK);
    }

    @GetMapping("/findTraceMongosByUri")
     ResponseEntity<?>findTraceMongosByUri( @NotNull @RequestParam String uri){
        return new ResponseEntity<>(traceMongoService.findTraceMongosByUri(uri),HttpStatus.OK);
    }



}
