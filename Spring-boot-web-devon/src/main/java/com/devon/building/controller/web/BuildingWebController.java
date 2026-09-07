package com.devon.building.controller.web;

import com.devon.building.enums.Status;
import com.devon.building.model.response.CustomerResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/buildings")
@RequiredArgsConstructor
public class BuildingWebController {

    @GetMapping("/contact")
    public String contactWeb(Model model) {
        model.addAttribute("status", Status.getStatus());
        CustomerResponseDTO customerDTO = new CustomerResponseDTO();
        model.addAttribute("customer", customerDTO);
        return "contact";
    }
}
