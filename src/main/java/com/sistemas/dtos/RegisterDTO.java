package com.sistemas.dtos;

import com.sistemas.entities.StablishmentEntity;
import com.sistemas.entities.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private Integer id;
    private String n_inventary;
    private String date_reception;
    private String n_memo;
    private String project;
    private String financing;//financiamiento
    private String n_acta_reception;//numero de acta de recepcion
    private String date_acta_reception; //fecha de acta de recepcion
    private String purchase_order; //orden de compra
    private String acquisition_value;//Valor de adquisicion
    private String provider; //proveedor
    private String rut_provider; // rut del proveedor
    private String n_fact;
    private String date_fact;
    private String amount_fact;
    private String n_dispatch_guide;
    private String date_dispatch_guide;
    private String amount_dispatch_guide;
    private String description_property;
    private String brand; // marca
    private String model;
    private String n_serie;
    private String n_res_info;// numero de resolucion informativa
    private String date_res_info;
     private StablishmentDTO stablishment;

    private String observation_state;
    private String n_res_gore;//Numero de resolucion del gore
    private String date_res_gore;
    private String n_res_accept;//N° RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
    private String date_res_accept;//FECHA RES. ACEPTACION Y TRANSFERENCIA DE DOMINIO
    private String state;

    private UserDTO user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
