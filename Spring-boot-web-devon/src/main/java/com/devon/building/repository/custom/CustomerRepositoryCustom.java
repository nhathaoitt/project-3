package com.devon.building.repository.custom;

import com.devon.building.entity.Customer;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.pagination.PaginationResult;

public interface CustomerRepositoryCustom {
    PaginationResult<Customer> findCustomer(CustomerSearchRequest customerSearchRequest, int page, int maxResult, int maxNavigationPage);
}
