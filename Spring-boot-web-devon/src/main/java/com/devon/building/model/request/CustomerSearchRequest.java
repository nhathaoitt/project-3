package com.devon.building.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerSearchRequest {
    String fullName;
    String email;
    String phone;
    Long staffId;
    String status;
    int page;
}
