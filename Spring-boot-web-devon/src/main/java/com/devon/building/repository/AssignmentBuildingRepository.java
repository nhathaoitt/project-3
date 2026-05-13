package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding, Long> {
    public void deleteByBuildingId(Long buildingId);
    public void deleteByBuildingIdIn(List<Long> id);
}
