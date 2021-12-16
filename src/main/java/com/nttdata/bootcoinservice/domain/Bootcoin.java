package com.nttdata.bootcoinservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bootcoin implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private TypeDocument typeDcoument;
    private String numberDocument;
    private String numberPhone;
    private String email;
    private Double amountBooCoins;
    private LocalDateTime createAt;

    public enum TypeDocument {
        DNI, CEX, Pasaporte
    }
}

