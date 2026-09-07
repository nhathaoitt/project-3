package com.devon.building.controller.admin.customer;

import com.devon.building.constant.SystemConstant;
import com.devon.building.enums.Status;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerResponseDTO;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.service.CustomerService;
import com.devon.building.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping("/list")
    public String listCustomers(@RequestParam(value = "page", defaultValue = "1") String pagetr, @ModelAttribute CustomerSearchRequest customerSearchRequest, Model model) {
        int page = 1;
        try {
            page = Integer.parseInt(pagetr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        CustomerSearchRequest customerSearchRequest1 = new CustomerSearchRequest();
        model.addAttribute("customer", customerSearchRequest1);
        model.addAttribute("status", Status.getStatus());
        model.addAttribute("staffs", userService.getStaffs());

        PaginationResult<CustomerResponseDTO> customerResponse = customerService.getCustomers(customerSearchRequest, page, SystemConstant.MAX_RESULT, SystemConstant.MAX_NAVIGATION_PAGE);
        model.addAttribute("result", customerResponse);
        return "admin/customer/customerList"; // Return the view name for listing customers
    }

    @GetMapping
    public ResponseEntity getStaff(@RequestParam(value = "customerId", required = false) Long customerId) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.loadStaffs(customerId));
    }

    @GetMapping("/edit")
    public String editCustomer(@ModelAttribute CustomerResponseDTO customerResponseDTO, Model model) {
        CustomerResponseDTO customer = new CustomerResponseDTO();
        model.addAttribute("customer", customer);
        model.addAttribute("status", Status.getStatus());
        return "/admin/customer/customerEdit";
    }

    @GetMapping("{id}/update")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute CustomerDTO customerDTO, Model model) {
        CustomerDTO customer = customerService.getCustomerById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("status", Status.getStatus());
        return "/admin/customer/customerEdit";
    }
}
