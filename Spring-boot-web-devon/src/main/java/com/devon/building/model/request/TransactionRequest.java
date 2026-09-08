package com.devon.building.model.request;

import com.devon.building.model.dto.AbstractDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest extends AbstractDTO {
    Long customerId;
    Long id;
    String code;
    @NotBlank(message = "Not be blank")
    String note;
    Long staffId;
}
