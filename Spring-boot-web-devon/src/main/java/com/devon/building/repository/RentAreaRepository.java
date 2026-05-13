package com.devon.building.repository;

import com.devon.building.entity.RentArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentAreaRepository extends JpaRepository<RentArea,Long> {
    public void deleteByBuildingIdIn(List<Long> id);

    void deleteByBuildingId(Long buildingId);
}
