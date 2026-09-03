package com.devon.building.model.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BuildingResponseDTO extends AbstractDTO {
    private String name;
    private String street;
    private String ward;
    private String district;
    private Long numberOfBasement;
    private Long floorArea;
    private String level;
    private String typeCode;
    private String overTimeFee;
    private String electricityFee;
    private String deposit;
    private String payment;
    private String rentTime;
    private String decorationTime;
    private String rentPriceDescription;
    private String carFee;
    private String motoFee;
    private String waterFee;
    private String structure;
    private String direction;
    private String note;
    private String rentArea; // "100,200,300"
    private String managerName;
    private String managerPhoneNumber;
    private Long rentPrice;
    private String serviceFee;
    private Long brokerageFee;
}
