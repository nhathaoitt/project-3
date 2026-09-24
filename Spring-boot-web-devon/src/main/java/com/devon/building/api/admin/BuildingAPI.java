package com.devon.building.api.admin;

import com.devon.building.model.dto.AssignmentBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("{api.prefix}/buildings")
@AllArgsConstructor
public class BuildingAPI {
    private BuildingService buildingService;

    @PostMapping
    public ResponseEntity<Object> addBuilding(@RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to add Building");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        buildingService.saveBuilding(buildingDTO);
        responseDTO.setMessage("Successfully added Building");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<Object> updateBuilding(@RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            responseDTO.setMessage("Failed to update Building");
            responseDTO.setDetail(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        if (buildingDTO.getId() == null) {
            responseDTO.setMessage("Failed to update Building");
            responseDTO.setDetail(List.of("Updated building required id"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        buildingService.updateBuilding(buildingDTO);
        responseDTO.setMessage("Successfully updated Building");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<Object> deleteBuilding(@PathVariable List<Long> ids) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (!ids.isEmpty()) {
            // xuong service xu ly
            buildingService.deleteBuilding(ids);
            responseDTO.setMessage("Successfully deleted Building");
        } else {
            responseDTO.setMessage("Failed to delete Building");
            responseDTO.setDetail(List.of("Deleted failed"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Object> getStaffs(@RequestParam(name = "buildingId", required = true) Long buildingId) {
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.loadStaffs(buildingId));
    }

    @PostMapping("/assign")
    public ResponseEntity<Object> assignBuilding(@RequestBody @Valid AssignmentBuildingDTO assignmentBuildingDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (assignmentBuildingDTO.getBuildingId() != null || assignmentBuildingDTO.getStaffIds() != null) {
            buildingService.assignBuilding(assignmentBuildingDTO);
            responseDTO.setMessage("Successfully assigned Building");
        } else {
            responseDTO.setMessage("Failed to assign Building");
            responseDTO.setDetail(List.of("Failed to assign Building"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
