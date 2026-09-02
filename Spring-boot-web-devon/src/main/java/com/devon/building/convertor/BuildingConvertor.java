package com.devon.building.convertor;

import com.devon.building.entity.Building;
import com.devon.building.enums.District;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.BuildingResponseDTO;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.pagination.PaginationResult;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BuildingConvertor {
    private final ModelMapper modelMapper;

    public BuildingSearchResponse toBuildingSearchResponse(Building building) {
        BuildingSearchResponse response = modelMapper.map(building, BuildingSearchResponse.class);
        String districtCode = building.getDistrict();
        String districtName = "";
        if (districtCode != null && !districtCode.isBlank()) {
            try {
                districtName = District.valueOf(districtCode).getDistrictName();
            } catch (IllegalArgumentException e) {
                districtName = districtCode;
            }
        }
        response.setAddress(building.getStreet() + ", " + building.getWard() + ", " + districtName);
        response.setRentArea(building.getRentAreas().stream().map(area -> area.getValue().toString()).collect(Collectors.joining(", ")));
        return response;
    }

    public Building toBuilding(BuildingDTO buildingDTO) {
        String typeCodeResult = buildingDTO.getTypeCode().stream().filter(typeCode -> typeCode != null && !typeCode.isBlank()).collect(Collectors.joining(", "));
        Building building = modelMapper.map(buildingDTO, Building.class);
        building.setTypeCode(typeCodeResult);
        return building;
    }

    public BuildingDTO toBuildingDTO(Building building) {
        List<String> typeCodeResult = Arrays.stream(building.getTypeCode().split(", ")).filter(typeCode -> !typeCode.isBlank()).collect(Collectors.toList());
        BuildingDTO buildingDTO = modelMapper.map(building, BuildingDTO.class);
        buildingDTO.setTypeCode(typeCodeResult);
        buildingDTO.setRentArea(building.getRentAreas().stream().map(area -> area.getValue().toString()).collect(Collectors.joining(", ")));
        return buildingDTO;
    }

    public void toBuildingResponseDTO(Building building) {
        BuildingResponseDTO responseDTO = modelMapper.map(building, BuildingResponseDTO.class);
        responseDTO.setRentArea(building.getRentAreas().stream().map(area -> area.getValue().toString()).collect(Collectors.joining(", ")));
    }
}

