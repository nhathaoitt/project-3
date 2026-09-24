package com.devon.building.service;

import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerResponseDTO;
import com.devon.building.pagination.PaginationResult;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDTO customerDTO);

    PaginationResult<CustomerResponseDTO> getCustomers(CustomerSearchRequest customerSearchRequest, int page, int maxResult, int maxNavigationPage);

    ResponseDTO loadStaffs(Long customerId);

    void assignmentCustomer(Long customerId, List<Long> staffIds);

    CustomerDTO getCustomerById(Long customerId);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(List<Long> ids);

}
