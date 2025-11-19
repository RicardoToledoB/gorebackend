package com.sistemas.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterSnapshotDTO {

    private String n_inventary;
    private String date_reception;
    private String n_memo;
    private String project;
    private String financing;
    private String n_acta_reception;
    private String date_acta_reception;
    private String purchase_order;
    private String acquisition_value;
    private String provider;
    private String rut_provider;
    private String n_fact;
    private String date_fact;
    private String amount_fact;
    private String n_dispatch_guide;
    private String date_dispatch_guide;
    private String amount_dispatch_guide;
    private String description_property;
    private String brand;
    private String model;
    private String n_serie;
    private String n_res_info;
    private String date_res_info;
    private String observation_state;
    private String n_res_gore;
    private String date_res_gore;
    private String n_res_accept;
    private String date_res_accept;
    private String state;
}
