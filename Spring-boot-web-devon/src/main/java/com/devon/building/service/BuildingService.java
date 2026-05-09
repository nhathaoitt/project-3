package com.devon.building.service;

import com.devon.building.model.dto.ResponseDTO;

public interface BuildingService {
    ResponseDTO loadStaffs(Long buildingId);
}
