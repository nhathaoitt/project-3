package com.devon.building.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "assignmentbuilding")
public class AssignmentBuilding extends BaseEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "buildingid", nullable = false)
    Building building;

    @ManyToOne
    @JoinColumn(name = "staffid", nullable = false)
    User user;
}
