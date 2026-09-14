package com.devon.building.api.admin;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.TransactionRequest;
import com.devon.building.service.TransactionService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/transaction")
public class TransactionAPI {
    private final UserService userService;
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to add transaction");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE) || SecurityUtils.getAuthorities().contains(SystemConstant.MANAGER_ROLE)) {
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            transactionRequest.setStaffId(user.getId());
        }
        transactionService.createTransaction(transactionRequest);
        responseDTO.setMessage("Successfully added transaction");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @PutMapping
    public ResponseEntity<ResponseDTO> updateTransaction(@RequestBody @Valid TransactionRequest transactionRequest, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to update transaction");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if (SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE) || SecurityUtils.getAuthorities().contains(SystemConstant.MANAGER_ROLE)) {
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            transactionRequest.setStaffId(user.getId());
        }
        transactionService.updateTransaction(transactionRequest);
        responseDTO.setMessage("Successfully updated transaction");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteTransaction(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        if(id != null){
            transactionService.deleteTransaction(id);
            responseDTO.setMessage("Successfully deleted transaction");
        }
        else{
            responseDTO.setMessage("Failed to delete transaction");
            responseDTO.setDetail(List.of("Deleted failed"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
