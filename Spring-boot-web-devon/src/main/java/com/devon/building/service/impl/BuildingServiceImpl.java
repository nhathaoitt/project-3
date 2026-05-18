package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.convertor.BuildingConvertor;
import com.devon.building.entity.AssignmentBuilding;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.entity.User;
import com.devon.building.model.dto.AssignmentBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.AssignmentBuildingRepository;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.RentAreaRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {
    private final AssignmentBuildingRepository assignmentBuildingRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final BuildingConvertor buildingConvertor;
    private final RentAreaRepository rentAreaRepository;

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<User> staffs = userRepository.findByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE); // get all staff
        Building building = buildingRepository.findById(buildingId).orElseThrow(() -> new RuntimeException("Không tìm thấy tòa nhà"));
        Set<Long> staffAssignedIds = building.getAssignmentBuildings().stream().map(assignment -> assignment.getUser().getId()).collect(Collectors.toSet()); // lay cac nhan vien dg quan ly toa nha co buildingId = id
        List<StaffResponseDTO> staffResponseDTOS = getStaffResponseDTOS(staffs, staffAssignedIds);
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("load staffs successfully");
        return responseDTO;
    }

    @Override
    public void assignBuilding(AssignmentBuildingDTO assignmentBuildingDTO) {
        assignmentBuildingRepository.deleteByBuildingId((assignmentBuildingDTO.getBuildingId()));
        Building building = buildingRepository.findById(assignmentBuildingDTO.getBuildingId()).orElseThrow(() -> new RuntimeException("không tìm thấy tòa nhà"));
        for (Long staffId : assignmentBuildingDTO.getStaffIds()) {
            User staff = userRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
            AssignmentBuilding assignmentBuilding = new AssignmentBuilding();
            assignmentBuilding.setBuilding(building);
            assignmentBuilding.setUser(staff);
            assignmentBuildingRepository.saveAndFlush(assignmentBuilding);
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("assign Building successfully");
    }

    @Override
    public List<BuildingSearchResponse> getBuildings(BuildingSearchRequest request) {
        List<Building> buildings = buildingRepository.findBuilding(request);
        List<BuildingSearchResponse> result = new ArrayList<>();
        for (Building building : buildings) {
            result.add(buildingConvertor.toBuildingSearchResponse(building));
        }
        return result;
    }

    @Override
    public BuildingDTO getBuildingById(long id) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new RuntimeException("Building not found"));
        return buildingConvertor.toBuildingDTO(building);
    }

    @Override
    public void saveBuilding(BuildingDTO buildingDTO) {
        Building building = buildingConvertor.toBuilding(buildingDTO);
        buildingRepository.saveAndFlush(building);
        String rentAreaStr = buildingDTO.getRentArea();
        List<RentArea> rentAreas = formatAndSaveRentAreas(rentAreaStr, building);
        building.setRentAreas(rentAreas);
        buildingConvertor.toBuildingResponseDTO(building);
    }

    @Override
    public void updateBuilding(BuildingDTO buildingDTO) {
        rentAreaRepository.deleteByBuildingId(buildingDTO.getId());
        Building building = buildingConvertor.toBuilding(buildingDTO);
        saveBuildingFinal(building);
        String rentAreaStr = buildingDTO.getRentArea();
        List<RentArea> rentAreas = formatAndSaveRentAreas(rentAreaStr, building);
        building.setRentAreas(rentAreas);
        buildingConvertor.toBuildingResponseDTO(building);
    }

    @Override
    public void deleteBuilding(List<Long> ids) {
        if (!ids.isEmpty()) {
            assignmentBuildingRepository.deleteByBuildingIdIn(ids);
            rentAreaRepository.deleteByBuildingIdIn(ids);
            buildingRepository.deleteByIdIn(ids);
        }
    }

    private static List<StaffResponseDTO> getStaffResponseDTOS(List<User> staffs, Set<Long> staffAssignedIds) {
        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for (User user : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(user.getId());
            staffResponseDTO.setUserName(user.getUserName());
            staffResponseDTO.setChecked("");
            if (staffAssignedIds.contains(user.getId())) {
                staffResponseDTO.setChecked("checked");
            }
            staffResponseDTOS.add(staffResponseDTO);
        }
        return staffResponseDTOS;
    }

    private void saveBuildingFinal(Building building) {
        buildingRepository.saveAndFlush(building);
    }

    private List<RentArea> formatAndSaveRentAreas(String rentAreaStr, Building building) {
        List<Long> listRentAreas = Arrays.stream(rentAreaStr.split(",")).map(String::trim).map(Long::parseLong).toList();
        return listRentAreas.stream().map(areas -> {
            RentArea rentArea = new RentArea();
            rentArea.setValue(areas);
            rentArea.setBuilding(building);
            return rentArea;
        }).toList();
    }
}
