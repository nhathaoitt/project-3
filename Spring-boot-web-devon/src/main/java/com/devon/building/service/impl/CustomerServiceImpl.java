package com.devon.building.service.impl;

import com.devon.building.convertor.CustomerConvertor;
import com.devon.building.entity.Customer;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
   private final CustomerConvertor customerConvertor;
   private final CustomerRepository customerRepository;
    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .demand(customerDTO.getDemand())
                .status(customerDTO.getStatus())
                .active(true)
                .build();
        customer.setCreatedBy("Anonymous");
        customerRepository.saveAndFlush(customer);
    }
}
