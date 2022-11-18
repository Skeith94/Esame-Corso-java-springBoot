package it.cgmconsulting.malato.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Document @NoArgsConstructor @Setter @Getter
public class TraceMongo {
    private String uri;
    private LocalDateTime date;
    private String request;
    private String response;

    public TraceMongo(String uri, LocalDateTime date, String request, String response) {
        this.uri = uri;
        this.date = date;
        this.request = request;
        this.response = response;
    }

}
