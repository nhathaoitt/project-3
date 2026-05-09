package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final UserRepository userRepository;
    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<User> staffs = userRepository.findByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE); // get all staff
//        Set<Long> staffAssignedIds = buildingRepository.findById(buildingId).getId; // lay cac nhan vien dg quan ly toa nha co buildingId = id
        List<StaffResponseDTO>  staffResponseDTOS = new ArrayList<>();
        for (User user : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(user.getId());
            staffResponseDTO.setUserName(user.getUserName());
            staffResponseDTO.setChecked("");
//            if(staffAssignedIds.contains(user.getId())){
//                staffResponseDTO.setChecked("checked");
//            }
            staffResponseDTOS.add(staffResponseDTO);
        }
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("load staffs successfully");
        return responseDTO;
    }
}
