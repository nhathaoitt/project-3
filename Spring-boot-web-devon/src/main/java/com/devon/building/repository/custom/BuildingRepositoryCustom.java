package com.devon.building.repository.custom;

import com.devon.building.entity.Building;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.pagination.PaginationResult;


public interface BuildingRepositoryCustom {
    PaginationResult<Building> findBuilding(BuildingSearchRequest buildingSearchRequest, int page, int maxResult, int maxNavigationPage);
}
