package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rentarea")
public class RentArea implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "value")
    Long value;

    @Column(name = "createddate")
    Date createdDate;
    @Column(name = "modifieddate")
    Date modifiedDate;
    @Column(name = "createdby")
    String createdBy;
    @Column(name = "modifiedby")
    String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "buildingid", nullable = false)
    Building building;
}
