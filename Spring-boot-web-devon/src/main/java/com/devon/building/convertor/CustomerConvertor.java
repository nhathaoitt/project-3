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

    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setActive(true);
        return customer;
    }

    public CustomerResponseDTO toCustomerResponse(Customer customer) {
        CustomerResponseDTO customerResponse = CustomerResponseDTO.builder()
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .demand(customer.getDemand())
                .status(customer.getStatus())
                .build();
        customerResponse.setId(customer.getId());
        customerResponse.setCreatedDate(customer.getCreatedDate());
        customerResponse.setCreatedBy(customer.getCreatedBy());
        return customerResponse;
    }

    public CustomerDTO toCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
