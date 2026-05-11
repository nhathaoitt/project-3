package com.devon.building.controller.admin.building;

import com.devon.building.entity.User;
import com.devon.building.enums.District;
import com.devon.building.enums.TypeCode;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final UserService userService;
    private final BuildingService buildingService;
    @GetMapping("/list")
    public String getBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {

        model.addAttribute("staffs", userService.getStaffs());
        model.addAttribute("district", District.getDistrict());
        model.addAttribute("typeCode", TypeCode.getTypeCode());
        //xuong service xu ly logic
        model.addAttribute("result", buildingService.getBuildings(buildingSearchRequest));
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
        BuildingDTO buildingDTO = buildingService.getBuildingById(id);
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }
}
