package com.devon.building.convertor;

import com.devon.building.entity.Customer;
import com.devon.building.model.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerConvertor {
    private final ModelMapper modelMapper;
    public Customer toCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
    }
}
