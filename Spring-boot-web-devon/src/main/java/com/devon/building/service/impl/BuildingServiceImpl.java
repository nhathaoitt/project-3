package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.convertor.BuildingConvertor;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.entity.User;
import com.devon.building.model.dto.AssignmentBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.dto.UserDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final BuildingConvertor buildingConvertor;

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<User> staffs = userRepository.findByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE); // get all staff
        Building building = buildingRepository.findById(buildingId).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà"));
        Set<Long> staffAssignedIds = building.getStaffs().stream().map(User::getId).collect(Collectors.toSet()); // lay cac nhan vien dg quan ly toa nha co buildingId = id
        List<StaffResponseDTO> staffResponseDTOS = getStaffResponseDTOS(staffs, staffAssignedIds);
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("load staffs successfully");
        return responseDTO;
    }

    @Override
    public void assignBuilding(AssignmentBuildingDTO assignmentBuildingDTO) {
        Building building = buildingRepository.findById(assignmentBuildingDTO.getBuildingId()).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tòa nhà"));
        List<User> staffs = userRepository.findAllById(assignmentBuildingDTO.getStaffIds());
        building.setStaffs(staffs);
        saveBuildingFinal(building);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("assign Building successfully");
    }

    @Override
    public PaginationResult<BuildingSearchResponse> getBuildings(BuildingSearchRequest request, int page, int maxResult, int maxNavigationPage) {
        PaginationResult<Building> buildings = buildingRepository.findBuilding(request, page, maxResult, maxNavigationPage);
        List<BuildingSearchResponse> result = new ArrayList<>();
        for (Building building : buildings.getList()) {
            result.add(buildingConvertor.toBuildingSearchResponse(building));
        }
        PaginationResult<BuildingSearchResponse> paginationResult = new PaginationResult<>();
        paginationResult.setMaxResult(maxResult);
        paginationResult.setCurrentPage(buildings.getCurrentPage());
        paginationResult.setTotalPages(buildings.getTotalPages());
        paginationResult.setList(result);
        paginationResult.setNavigationPages(buildings.getNavigationPages());
        paginationResult.setTotalRecords(buildings.getTotalRecords());
        return paginationResult;
    }

    @Override
    public BuildingDTO getBuildingById(long id) {
        Building building = buildingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Building not found"));
        return buildingConvertor.toBuildingDTO(building);
    }

    @Override
    public void saveBuilding(BuildingDTO buildingDTO) {
        Building building = buildingConvertor.toBuilding(buildingDTO);
        String rentAreaStr = buildingDTO.getRentArea();
        List<RentArea> rentAreas = formatAndSaveRentAreas(rentAreaStr, building);
        building.setRentAreas(rentAreas);
        convertToByte(buildingDTO, building);
        buildingRepository.saveAndFlush(building);
        buildingConvertor.toBuildingResponseDTO(building);
    }

    @Override
    public void updateBuilding(BuildingDTO buildingDTO) {
        Building building = buildingRepository.findById(buildingDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Building not found"));
        buildingConvertor.toBuilding(buildingDTO);
        building.getRentAreas().clear();
        String rentAreaStr = buildingDTO.getRentArea();
        List<RentArea> rentAreas = formatAndSaveRentAreas(rentAreaStr, building);
        building.getRentAreas().addAll(rentAreas);
        convertToByte(buildingDTO, building);
        buildingRepository.saveAndFlush(building);
        buildingConvertor.toBuildingResponseDTO(building);
    }

    @Override
    public void deleteBuilding(List<Long> ids) {
        if (!ids.isEmpty()) {
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
    private void convertToByte(BuildingDTO buildingDTO, Building building) {
        try {
            if (buildingDTO.getBase64Image() != null && !buildingDTO.getBase64Image().isEmpty()) {
                String base64String = buildingDTO.getBase64Image();
                if (base64String.contains(",")) {
                    base64String = base64String.split(",")[1];
                }

                byte[] imageBytes = Base64.getDecoder().decode(base64String);
                building.setImage(imageBytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid image data", e);
        }
    }
}
