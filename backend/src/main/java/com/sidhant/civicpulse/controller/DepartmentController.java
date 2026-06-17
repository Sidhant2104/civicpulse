package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.CreateDepartmentRequestDto;
import com.sidhant.civicpulse.dto.CreateDepartmentResponseDto;
import com.sidhant.civicpulse.dto.DeleteDepartmentResponseDto;
import com.sidhant.civicpulse.dto.UpdateDepartmentResponseDto;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/departments")
public class DepartmentController {
    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // 1: Create Department
    @PostMapping
    public CreateDepartmentResponseDto createDepartment(@RequestBody CreateDepartmentRequestDto dto){
        return departmentService.createDepartment(dto);
    }

    //2: Get all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    //3: Update Department:
    @PutMapping("/{id}")
    public UpdateDepartmentResponseDto updateDepartment(
            @PathVariable String id,
            @RequestBody CreateDepartmentRequestDto dto) {
        return departmentService.updateDepartment(id, dto);
    }

    //4: Delete Department
    @DeleteMapping("/{id}")
    public DeleteDepartmentResponseDto deleteDepartment(@PathVariable String id){
        return departmentService.deleteDepartment(id);
    }


}
