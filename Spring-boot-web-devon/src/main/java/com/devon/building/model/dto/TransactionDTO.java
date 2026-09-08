package com.devon.building.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionDTO extends AbstractDTO{
    String code;
    String note;
}
