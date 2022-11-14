package it.cgmconsulting.malato.payload.request;



import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Getter
public class AddUpdateRentalRequest {
    @NotNull @Min(1)
    Long filmId;
    @NotNull @Min(1)
    Long storeId;
    @NotNull @Min(1)
    Long customerId;
    @NotNull @Min(1)
    Long daysRental;

}
