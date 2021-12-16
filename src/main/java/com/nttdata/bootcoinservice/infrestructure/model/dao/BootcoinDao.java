package com.nttdata.bootcoinservice.infrestructure.model.dao;

import com.nttdata.bootcoinservice.domain.Bootcoin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("bootcoin")
public class BootcoinDao {
    @Id
    private String id;
    @NonNull
    private TypeDocument typeDcoument;
    @NonNull
    private String numberDocument;
    @NonNull
    private String numberPhone;
    @NonNull
    private String email;
    @NonNull
    private Double amountBooCoins;
    @NonNull
    private LocalDateTime createAt;

    public enum TypeDocument {
        DNI, CEX, Pasaporte
    }

}
