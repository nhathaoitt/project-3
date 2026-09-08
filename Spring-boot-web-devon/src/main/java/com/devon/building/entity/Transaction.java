package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
public class Transaction extends BaseEntity implements Serializable {
    @Column(name = "code")
    String code;
    @Column(name = "note")
    String note;
    @Column(name = "is_active")
    Boolean active;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "staffid",nullable = false)
    private User staff;
}
