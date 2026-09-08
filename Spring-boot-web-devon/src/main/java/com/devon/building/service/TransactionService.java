package com.devon.building.service;

import com.devon.building.model.dto.TransactionDTO;
import com.devon.building.model.request.TransactionRequest;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactions(Long customerId, String code);

    void createTransaction(TransactionRequest transactionRequest);

    void updateTransaction(TransactionRequest transactionRequest);

    void deleteTransaction(Long id);
}
