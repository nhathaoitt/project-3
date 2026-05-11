package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding,Long> {
    public void deleteByBuildingId(Long id);
}
