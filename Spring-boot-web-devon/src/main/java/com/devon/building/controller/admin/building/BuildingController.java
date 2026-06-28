package com.devon.building.controller.admin.building;

import com.devon.building.enums.District;
import com.devon.building.enums.TypeCode;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final UserService userService;
    private final BuildingService buildingService;
    private static final String DISTRICT = "district";
    private static final String TYPECODE = "typeCode";

    @GetMapping("/list")
    public String getBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {

        model.addAttribute("staffs", userService.getStaffs());
        model.addAttribute(DISTRICT, District.getDistrict());
        model.addAttribute(TYPECODE, TypeCode.getTypeCode());
        //xuong service xu ly logic
        model.addAttribute("result", buildingService.getBuildings(buildingSearchRequest));
        return "admin/building/buildingList";
    }

    @GetMapping("/edit")
    public String editBuilding(@ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {
        model.addAttribute(DISTRICT, District.getDistrict());
        model.addAttribute(TYPECODE, TypeCode.getTypeCode());
        BuildingDTO buildingDTO = new BuildingDTO();
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }

    @GetMapping("/{id}/update")
    public String updateBuilding(@PathVariable Long id, Model model) {
        model.addAttribute(DISTRICT, District.getDistrict());
        model.addAttribute(TYPECODE, TypeCode.getTypeCode());
        BuildingDTO buildingDTO = buildingService.getBuildingById(id);
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }
}
