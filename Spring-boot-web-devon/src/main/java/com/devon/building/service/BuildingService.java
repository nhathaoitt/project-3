package com.devon.building.service;

import com.devon.building.model.dto.AssignmentBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;

import java.util.List;

public interface BuildingService {
    ResponseDTO loadStaffs(Long buildingId);

    void assignBuilding(AssignmentBuildingDTO assignmentBuildingDTO);
    List<BuildingSearchResponse> getBuildings(BuildingSearchRequest request);

    BuildingDTO getBuildingById(long id);

    void saveBuilding(BuildingDTO buildingDTO);

    void updateBuilding(BuildingDTO buildingDTO);

    void deleteBuilding(List<Long> id);
}
