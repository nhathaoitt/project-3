package com.devon.building.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BuildingDTO extends AbstractDTO{
    @NotBlank(message = "Name building not be blank")
    private String name;
    private String street;
    private String ward;
    @NotBlank(message = "District building not be blank")
    private String district;
    @NotNull(message = "NumberOfBasement not be blank")
    private Long numberOfBasement;
    private Long floorArea;
    private String level;
    @NotEmpty(message = "Type code is required")
    private List<String> typeCode;
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
    @NotBlank(message = "RentArea not be blank")
    private String rentArea; // "100,200,300"
    private String managerName;
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must not be less than 10 digits")
    private String managerPhone;
    @NotNull(message = "RentPrice not be null")
    private Long price;
    private String serviceFee;
    private Long brokerageFee;
    private String base64Image;
    private String imageName;
    private byte[] image;
    private MultipartFile fileData;
}
