package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.convertor.CustomerConvertor;
import com.devon.building.entity.Customer;
import com.devon.building.entity.User;
import com.devon.building.model.dto.CustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerResponseDTO;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerConvertor customerConvertor;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveCustomer(CustomerDTO customerDTO, String currentName) {
        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .email(customerDTO.getEmail())
                .companyName(customerDTO.getCompanyName())
                .phone(customerDTO.getPhone())
                .demand(customerDTO.getDemand())
                .status(customerDTO.getStatus())
                .active(true)
                .build();
        if (!currentName.isBlank()) {
            customer.setCreatedBy(currentName);
        } else {
            customer.setCreatedBy("Anonymous");
        }
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public PaginationResult<CustomerResponseDTO> getCustomers(CustomerSearchRequest customerSearchRequest, int page, int maxResult, int maxNavigationPage) {
        PaginationResult<Customer> customers = customerRepository.findCustomer(customerSearchRequest, page, maxResult, maxNavigationPage);
        List<CustomerResponseDTO> result = new ArrayList<>();
        for (Customer customer : customers.getList()) {
            if (customer.getActive() == true) {
                result.add(customerConvertor.toCustomerResponse(customer));
            }
        }
        PaginationResult<CustomerResponseDTO> paginationResult = new PaginationResult<>();
        paginationResult.setMaxResult(maxResult);
        paginationResult.setCurrentPage(customers.getCurrentPage());
        paginationResult.setTotalPages(customers.getTotalPages());
        paginationResult.setList(result);
        paginationResult.setNavigationPages(customers.getNavigationPages());
        paginationResult.setTotalRecords(customers.getTotalRecords());
        return paginationResult;
    }

    @Override
    public ResponseDTO loadStaffs(Long customerId) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<User> staffs = userRepository.findByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not be found"));
        Set<Long> staffAssignIds = customer.getStaffs().stream().map(User::getId).collect(Collectors.toSet());
        List<StaffResponseDTO> staffResponseDTOS = getStaffsDTO(staffs, staffAssignIds);
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("load staffs successfully");
        return responseDTO;
    }

    @Override
    public void assignmentCustomer(Long customerId, List<Long> staffIds) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not be found"));
        List<User> staffs = userRepository.findAllById(staffIds);
        customer.setStaffs(staffs);
        saveAll(customer);
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Not found"));
        return customerConvertor.toCustomerDTO(customer);
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Customer existcustomer = customerRepository.findById(customerDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        modelMapper.map(customerDTO, existcustomer);
        saveAll(existcustomer);
    }

    @Override
    public void deleteCustomer(List<Long> ids) {
        List<Customer> customers = customerRepository.findAllById(ids);
        if (customers.isEmpty()) {
            throw new EntityNotFoundException("Customers not be found");
        } else {
            for (Customer customer : customers) {
                customer.setActive(false);
                saveAll(customer);
            }
        }
    }

    private static List<StaffResponseDTO> getStaffsDTO(List<User> staffs, Set<Long> staffAssignIds) {
        return getStaffResponseDTOS(staffs, staffAssignIds);
    }

    static List<StaffResponseDTO> getStaffResponseDTOS(List<User> staffs, Set<Long> staffAssignIds) {
        List<StaffResponseDTO> staffResponseDTOs = new ArrayList<>();
        for (User staff : staffs) {
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setId(staff.getId());
            staffResponseDTO.setUserName(staff.getUsername());
            staffResponseDTO.setChecked("");
            if (staffAssignIds.contains(staff.getId())) {
                staffResponseDTO.setChecked("checked");
            }
            staffResponseDTOs.add(staffResponseDTO);
        }
        return staffResponseDTOs;
    }

    private void saveAll(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }
}
