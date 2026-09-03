package com.devon.building.api.web;

import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
        customerService.saveCustomer(customerDTO);
        responseDTO.setMessage("Successfully added customer");
        return ResponseEntity.ok().body(responseDTO);
    }
}
