package com.devon.building.repository.custom;

import com.devon.building.entity.Building;
import com.devon.building.model.request.BuildingSearchRequest;

import java.util.List;

public interface BuildingRepositoryCustom {
    List<Building> findBuilding(BuildingSearchRequest buildingSearchRequest);
}
