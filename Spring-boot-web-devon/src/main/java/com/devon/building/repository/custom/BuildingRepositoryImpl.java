package com.devon.building.repository.custom;

import com.devon.building.entity.Building;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.utils.StringNumberValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    static void queryJoin(BuildingSearchRequest buildingSearchRequest, StringBuilder sql) {
        Long staffId = buildingSearchRequest.getStaffId();
        Long rentAreaFrom = buildingSearchRequest.getRentAreaFrom();
        boolean hasFrom = !StringNumberValidation.isNull(rentAreaFrom);
        Long rentAreaTo = buildingSearchRequest.getRentAreaTo();
        boolean hasTo = !StringNumberValidation.isNull(rentAreaTo);
        if (!StringNumberValidation.isNull(staffId)) {
            sql.append(" JOIN assignmentbuilding ab ON ab.buildingid = b.id ");
            sql.append(" AND ab.staffid = ").append(staffId);
        }
        if (hasFrom || hasTo) {
            sql.append(" JOIN rentarea ra ON ra.buildingid = b.id ");
            if (hasFrom) {
                sql.append(" AND ra.value >= ").append(rentAreaFrom).append(" ");
            }
            if (hasTo) {
                sql.append(" AND ra.value <= ").append(rentAreaTo).append(" ");
            }
        }
    }

    static void normalQuery(BuildingSearchRequest buildingSearchRequest, StringBuilder sql) {
        try {
            Field[] fields = BuildingSearchRequest.class.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                if (!name.equals("staffId") && !name.startsWith("rentArea") && !name.startsWith("rentPrice") && !name.equals("typeCode")) {
                    field.setAccessible(true);
                    Object value = field.get(buildingSearchRequest);
                    if (value != null && !value.toString().trim().isEmpty()) {
                        if (value.toString().matches("^\\d+(\\.\\d+)?$") && name.equals("numberOfBasement")) {
                            sql.append(" AND b.").append(name.toLowerCase()).append(" = ").append(value);
                        } else {
                            sql.append(" AND b.").append(name.toLowerCase()).append(" LIKE '%").append(value)
                                    .append("%' ");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long rentPriceFrom = buildingSearchRequest.getRentPriceFrom();
        Long rentPriceTo = buildingSearchRequest.getRentPriceTo();
        if (!StringNumberValidation.isNull(rentPriceFrom)) {
            sql.append(" AND b.rentprice >= ").append(rentPriceFrom).append(" ");
        }
        if (!StringNumberValidation.isNull(rentPriceTo)) {
            sql.append(" AND b.rentprice <= ").append(rentPriceTo).append(" ");
        }
        List<String> typeCodes = buildingSearchRequest.getTypeCode();
        if (typeCodes != null && !typeCodes.isEmpty()) {
            sql.append(" AND (");
            sql.append(typeCodes.stream().map(typeCode -> " b.type LIKE '%" + typeCode + "%'").collect(Collectors.joining(" OR ")))
                    .append(") ");
        }
    }

    @Override
    public List<Building> findBuilding(BuildingSearchRequest buildingSearchRequest) {
        StringBuilder sql = new StringBuilder("select distinct b.* from Building b");
        queryJoin(buildingSearchRequest, sql);
        sql.append(" WHERE 1=1 ");
        normalQuery(buildingSearchRequest, sql);
        Query query = entityManager.createNativeQuery(sql.toString(), Building.class);
        return query.getResultList();
    }
}
