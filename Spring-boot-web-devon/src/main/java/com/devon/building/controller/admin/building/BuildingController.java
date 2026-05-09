package com.devon.building.controller.admin.building;

import com.devon.building.entity.User;
import com.devon.building.enums.District;
import com.devon.building.enums.TypeCode;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final UserService userService;
    @GetMapping("/list")
    public String getBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {

        model.addAttribute("staffs", userService.getStaffs());
        model.addAttribute("district", District.getDistrict());
        model.addAttribute("typeCode", TypeCode.getTypeCode());
        //xuong service xu ly logic
        List<BuildingSearchResponse> result = new ArrayList<>();
        BuildingSearchResponse building1 = new BuildingSearchResponse();
        building1.setId(1L);
        building1.setName("Hao Building");
        building1.setAddress("449 Le Van Viet, P9, Q9");
        building1.setNumberOfBasement(3L);
        building1.setManagerName("Khanh Pham");
        building1.setManagerPhoneNumber("0345675894");

        BuildingSearchResponse building2 = new BuildingSearchResponse();
        building2.setId(2L);
        building2.setName("Hai Building");
        building2.setAddress("448 Le Van Viet, P9, Q9");
        building2.setNumberOfBasement(3L);
        building2.setManagerName("Tan Pham");
        building2.setManagerPhoneNumber("0345675645");

        result.add(building1);
        result.add(building2);
        model.addAttribute("result", result);
        return "admin/building/buildingList";
    }
    @GetMapping("/edit")
    public String editBuilding(@ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {
        model.addAttribute("district", District.getDistrict());
        model.addAttribute("typeCode", TypeCode.getTypeCode());
        BuildingDTO buildingDTO = new BuildingDTO();
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }

    @GetMapping("/{id}/update")
    public String updateBuilding(@PathVariable Long id, Model model) {
        model.addAttribute("district", District.getDistrict());
        model.addAttribute("typeCode", TypeCode.getTypeCode());
        BuildingDTO buildingDTO = new BuildingDTO();
        buildingDTO.setId(id);
        buildingDTO.setName("Devon Building " + id);
        buildingDTO.setDistrict("QUAN_1");
        buildingDTO.setRentArea("100,200,300");
        buildingDTO.setNumberOfBasement(2L);
        buildingDTO.setRentPrice(15L);
        buildingDTO.setManagerName("Anh Long");
        buildingDTO.setTypeCode(Arrays.asList("TANG_TRET", "NOI_THAT"));
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }
}
