package com.devon.building.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer")
public class Customer extends BaseEntity implements Serializable {

    @Column(name = "fullname", length = 255, nullable = false)
    String fullName;
    @Column(name = "phone", length = 255, nullable = false)
    String phone;
    @Column(name = "email", length = 255, nullable = false)
    String email;
    @Column(name = "companyname")
    String companyName;
    @Column(name = "demand", length = 255, nullable = false)
    String demand;
    @Column(name = "status", length = 255, nullable = false)
    String status;
    @Column(name = "is_active")
    Boolean active;


}
