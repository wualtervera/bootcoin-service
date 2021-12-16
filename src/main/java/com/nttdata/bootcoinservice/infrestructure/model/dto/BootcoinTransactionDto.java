package com.nttdata.bootcoinservice.infrestructure.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BootcoinTransactionDto {
    @Id
    private String id;
    private String phoneBuyer; //Celular del comprador
    private String phoneSeller; //Celular del vendedor
    private Double amountSoles;
    private Double amountCoins;
    private TypePayment typePayment;
    private State state;
    private LocalDateTime createAt;

    public enum State {
        PENDING,
        ACCEPTED,
        REJECTED,
        SUCCESSFUL
    }
    public enum TypePayment {
        YANKY,
        TRANSFERENCIA
    }

}
