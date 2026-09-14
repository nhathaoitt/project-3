package com.devon.building.service.impl;

import com.devon.building.entity.Customer;
import com.devon.building.entity.Transaction;
import com.devon.building.entity.User;
import com.devon.building.model.dto.TransactionDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.TransactionRepository;
import com.devon.building.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;


    @Override
    public List<TransactionDTO> getTransactions(Long customerId, String code) {
        List<Transaction> transactions = transactionRepository.findByCustomer_IdAndCode(customerId, code);
        List<TransactionDTO> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getActive() == true) {
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
        }
        return result;
    }

    @Override
    public void createTransaction(TransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();
        Transaction transaction = Transaction.builder()
                .code(transactionRequest.getCode())
                .note(transactionRequest.getNote())
                .active(true)
                .build();
        Customer customer1 = new Customer();
        customer1.setId(customer.getId());
        transaction.setCustomer(customer1);

        User staff = new User();
        staff.setId(transactionRequest.getStaffId());
        transaction.setStaff(staff);
        transaction.setCreatedBy(currentName);
        transactionRepository.saveAndFlush(transaction);
    }

    @Override
    public void updateTransaction(TransactionRequest transactionRequest) {
        Transaction existTransaction = transactionRepository.findById(transactionRequest.getId()).orElseThrow(() -> new EntityNotFoundException("Not found"));
        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();
        existTransaction.setNote(transactionRequest.getNote());
        existTransaction.setModifiedDate(new Date());
        existTransaction.setModifiedBy(currentName);
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
