package com.devon.building.repository.custom;

import com.devon.building.entity.Customer;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.pagination.PaginationResult;
import com.devon.building.utils.StringNumberValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    static void queryBuilder(CustomerSearchRequest customerSearchRequest, StringBuilder sql) {
        Long staffId = customerSearchRequest.getStaffId();
        if(!StringNumberValidation.isNull(staffId)){
            sql.append(" JOIN assignmentcustomer ac ON ac.customerid = c.id ");
            sql.append(" AND ac.staffid = ").append(staffId).append(" ");
        }
        sql.append(" WHERE 1=1 AND c.is_active = 1");
        try {
            Field[] fields = CustomerSearchRequest.class.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                if(!name.equals("staffId") && !name.equals("page")){
                    field.setAccessible(true);
                    Object value = field.get(customerSearchRequest);
                    if (value != null && !value.toString().trim().isEmpty()){
                        if(value.toString().matches("^\\d+(\\.\\d+)?$") && !name.equals("phone")){
                            sql.append(" AND c.").append(name.toLowerCase()).append(" = ").append(value);
                        }else{
                            sql.append(" AND c.").append(name.toLowerCase()).append(" LIKE '%").append(value)
                                    .append("%' ");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public PaginationResult<Customer> findCustomer(CustomerSearchRequest customerSearchRequest, int page, int maxResult, int maxNavigationPage) {
        StringBuilder sql = new StringBuilder("select c.* from Customer c");
        queryBuilder(customerSearchRequest, sql);
        sql.append(" ORDER BY createddate DESC");
        Query query = entityManager.createNativeQuery(sql.toString(), Customer.class);
        return new PaginationResult<>(query, query.getResultList().size(), page, maxResult, maxNavigationPage);
    }
    // Implement custom methods here
}
