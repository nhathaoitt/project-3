package com.devon.building.repository;

import com.devon.building.entity.RentArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentAreaRepository extends JpaRepository<RentArea,Long> {
    public void deleteByBuildingId(Long id);
}
