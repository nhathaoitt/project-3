package com.devon.building.repository;

import com.devon.building.entity.Customer;
import com.devon.building.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    public void deleteByIdIn(List<Long> ids);
}
