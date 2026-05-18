package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "building")
public class Building implements Serializable {

    @Serial
    private static final long serialVersionUID = -1000119078147252957L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "street", length = 255, nullable = false)
    private String street;

    @Column(name = "ward", length = 255, nullable = false)
    private String ward;

    @Column(name = "district", length = 255, nullable = false)
    private String district;

    @Column(name = "structure")
    private String structure;

    @Column(name = "numberofbasement", nullable = false)
    private Long numberOfBasement;

    @Column(name = "floorarea", nullable = false)
    private Long floorArea;

    @Column(name = "direction")
    private String direction;

    @Column(name = "level")
    private String level;

    @Column(name = "rentprice", nullable = false)
    private Long price;

    @Column(name = "rentpricedescription")
    private String rentPriceDescription;
    @Column(name = "servicefee")
    private String serviceFee;
    @Column(name = "carfee")
    private String carfee;
    @Column(name = "motofee")
    private String motoFee;
    @Column(name = "overtimefee")
    private String overtimeFee;
    @Column(name = "waterfee")
    private String waterFee;
    @Column(name = "electricityfee")
    private String electricityFee;
    @Column(name = "deposit")
    private String deposit;
    @Column(name = "payment")
    private String payment;
    @Column(name = "renttime")
    private String rentTime;
    @Column(name = "decorationtime")
    private String decorationTime;
    @Column(name = "brokeragefee")
    private Double brokerageFee;
    @Column(name = "type", nullable = false)
    private String typeCode;
    @Column(name = "note")
    private String note;
    @Column(name = "linkofbuilding")
    private String linkOfBuilding;
    @Column(name = "map")
    private String map;
    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createddate", nullable = true)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifieddate", nullable = true)
    private Date modifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdby", nullable = true)
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedby", nullable = true)
    private String modifiedBy;

    @Column(name = "managername", length = 255)
    private String managerName;
    @Column(name = "managerphone", length = 255)
    private String managerPhone;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL,  orphanRemoval = true)
    List<RentArea> rentAreas;

    @OneToMany(mappedBy = "building")
    private List<AssignmentBuilding> assignmentBuildings;
}
