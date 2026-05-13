package com.devon.building.repository;

import com.devon.building.entity.Building;
import com.devon.building.repository.custom.BuildingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BuildingRepository extends JpaRepository<Building, Long>, BuildingRepositoryCustom {
    public void deleteByIdIn(List<Long> id);
}
