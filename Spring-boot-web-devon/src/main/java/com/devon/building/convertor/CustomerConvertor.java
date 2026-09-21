package com.devon.building.convertor;

import com.devon.building.entity.Customer;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.response.CustomerResponseDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerConvertor {
    private final ModelMapper modelMapper;

    public CustomerResponseDTO toCustomerResponse(Customer customer) {
        CustomerResponseDTO customerResponse = CustomerResponseDTO.builder()
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .demand(customer.getDemand())
                .status(customer.getStatus())
                .build();
        customerResponse.setId(customer.getId());
        customerResponse.setCreatedBy(customer.getCreatedBy());
        customerResponse.setCreatedDate(customer.getCreatedDate());
        customerResponse.setModifiedBy(customer.getModifiedBy());
        customerResponse.setModifiedDate(customer.getModifiedDate());
        return customerResponse;
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
