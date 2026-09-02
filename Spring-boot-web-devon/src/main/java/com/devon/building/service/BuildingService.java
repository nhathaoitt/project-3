package com.devon.building.service;

import com.devon.building.model.dto.AssignmentBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface BuildingService {
    ResponseDTO loadStaffs(Long buildingId);

    void assignBuilding(AssignmentBuildingDTO assignmentBuildingDTO);

    PaginationResult<BuildingSearchResponse> getBuildings(BuildingSearchRequest request, int page, int maxResult, int maxNavigationPage);

    BuildingDTO getBuildingById(long id);

    void saveBuilding(BuildingDTO buildingDTO);

    void updateBuilding(BuildingDTO buildingDTO);

    void deleteBuilding(List<Long> id);
}
