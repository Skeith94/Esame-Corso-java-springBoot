package it.cgmconsulting.malato.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class CustomerStoreResponse {
    private String storeName;
    private Long totalCustomer;

}
