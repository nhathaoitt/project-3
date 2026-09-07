package com.devon.building.api.web;

import com.devon.building.model.dto.AssignmentCustomerDTO;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/web/customers")
public class CustomerAPI {
    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<Object> addCustomer(@RequestBody @Valid CustomerDTO customerDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to add customer");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName(); // lay username hien tai

        customerService.saveCustomer(customerDTO, currentName);
        responseDTO.setMessage("Successfully added customer");
        return ResponseEntity.ok().body(responseDTO);
    }
    @PostMapping("/assign")
    public ResponseEntity<Object> assignCustomer(@RequestBody @Valid AssignmentCustomerDTO assignmentCustomerDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to assign customer");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if(assignmentCustomerDTO.getCustomerId() != null && assignmentCustomerDTO.getStaffIds() != null){
            customerService.assignmentCustomer(assignmentCustomerDTO.getCustomerId(), assignmentCustomerDTO.getStaffIds());
            responseDTO.setMessage("Successfully assigned customer");
        }
        else {
            responseDTO.setMessage("Failed to assign customer");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @PutMapping
    public ResponseEntity<Object> updateCustomer(@RequestBody @Valid CustomerDTO customerDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to add customer");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if(customerDTO.getId() == null){
            responseDTO.setMessage("Failed to update customer");
            responseDTO.setDetail(List.of("Updated customer required id"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        customerService.updateCustomer(customerDTO);
        responseDTO.setMessage("Successfully updated customer");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @DeleteMapping("/{ids}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable("ids") List<Long> ids) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(!ids.isEmpty()){
            customerService.deleteCustomer(ids);
            responseDTO.setMessage("Successfully deleted customer");
        }else{
            responseDTO.setMessage("Failed to delete customer");
            responseDTO.setDetail(List.of("Deleted failed"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
