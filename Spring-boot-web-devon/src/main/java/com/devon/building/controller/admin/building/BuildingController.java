package com.devon.building.controller.admin.building;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.enums.District;
import com.devon.building.enums.TypeCode;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {
    private final UserService userService;
    private final BuildingService buildingService;
    private static final String DISTRICT = "district";
    private static final String TYPECODE = "typeCode";

    @GetMapping("/list")
    public String getBuildings(@RequestParam(value = "page", defaultValue = "1") String pagetr, @ModelAttribute BuildingSearchRequest buildingSearchRequest, Model model) {
        int page = 1;
        try{
            page = Integer.parseInt(pagetr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("staffs", userService.getStaffs());
        model.addAttribute(DISTRICT, District.getDistrict());
        model.addAttribute(TYPECODE, TypeCode.getTypeCode());
        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)) {
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            buildingSearchRequest.setStaffId(user.getId());
        }
        //xuong service xu ly logic
        PaginationResult<BuildingSearchResponse> paginationResult = buildingService.getBuildings(buildingSearchRequest, page, SystemConstant.MAX_RESULT, SystemConstant.MAX_NAVIGATION_PAGE);
        model.addAttribute("result", paginationResult);
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
        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)) {
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            boolean isAssigned = user.getBuildings().stream().anyMatch(building -> building.getId().equals(id));
            if(!isAssigned){
                return "404";
            }
        }
        model.addAttribute(DISTRICT, District.getDistrict());
        model.addAttribute(TYPECODE, TypeCode.getTypeCode());
        BuildingDTO buildingDTO = buildingService.getBuildingById(id);
        model.addAttribute("building", buildingDTO);
        return "admin/building/buildingEdit";
    }
    @GetMapping("/images")
    public void productImage(HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam(value = "id", required = false) Long id) throws IOException {
        BuildingDTO building = buildingService.getBuildingById(id);
        if (building != null && building.getImage() != null) {
            response.setContentType("image/jpeg");
            response.setContentType("image/png");
            response.getOutputStream().write(building.getImage());
        }
        response.getOutputStream().close();
    }
}
