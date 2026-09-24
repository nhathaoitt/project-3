package com.devon.building.service.impl;

import com.devon.building.entity.Customer;
import com.devon.building.entity.Transaction;
import com.devon.building.entity.User;
import com.devon.building.model.dto.TransactionDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.TransactionRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionServiceImpl implements TransactionService {

    TransactionRepository transactionRepository;
    CustomerRepository customerRepository;
    UserRepository userRepository;


    @Override
    public List<TransactionDTO> getTransactions(Long customerId, String code) {
        List<Transaction> transactions = transactionRepository.findByCustomer_IdAndCodeAndActiveTrue(customerId, code);
        List<TransactionDTO> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .note(transaction.getNote())
                    .code(transaction.getCode())
                    .build();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setCreatedBy(transaction.getCreatedBy());
            transactionDTO.setCreatedDate(transaction.getCreatedDate());
            transactionDTO.setModifiedDate(transaction.getModifiedDate());
            transactionDTO.setModifiedBy(transaction.getModifiedBy());
            result.add(transactionDTO);
        }
        return result;
    }

    @Override
    public void createTransaction(TransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        User staff = userRepository.findByIdAndActiveTrue(transactionRequest.getStaffId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        Transaction transaction = Transaction.builder()
                .code(transactionRequest.getCode())
                .note(transactionRequest.getNote())
                .active(true)
                .customer(customer)
                .staff(staff)
                .build();
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void updateTransaction(TransactionRequest transactionRequest) {
        Transaction existTransaction = transactionRepository.findById(transactionRequest.getId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        existTransaction.setNote(transactionRequest.getNote());
        transactionRepository.saveAndFlush(existTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
        try {
            if (transaction != null) {
                transaction.setActive(false);
                transactionRepository.saveAndFlush(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
