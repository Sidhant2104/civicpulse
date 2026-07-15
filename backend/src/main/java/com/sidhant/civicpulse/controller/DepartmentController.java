package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.*;
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
    public ApiResponse<CreateDepartmentResponseDto> createDepartment(
            @RequestBody CreateDepartmentRequestDto dto) {

        return ApiResponse.success(
                "Department created successfully",
                departmentService.createDepartment(dto)
        );
    }

    // 2: Get all departments
    @GetMapping
    public ApiResponse<List<Department>> getAllDepartments() {

        return ApiResponse.success(
                "Departments fetched successfully",
                departmentService.getAllDepartments()
        );
    }

    // 3: Update Department
    @PutMapping("/{id}")
    public ApiResponse<UpdateDepartmentResponseDto> updateDepartment(
            @PathVariable String id,
            @RequestBody CreateDepartmentRequestDto dto) {

        return ApiResponse.success(
                "Department updated successfully",
                departmentService.updateDepartment(id, dto)
        );
    }

    // 4: Delete Department
    @DeleteMapping("/{id}")
    public ApiResponse<DeleteDepartmentResponseDto> deleteDepartment(
            @PathVariable String id) {

        return ApiResponse.success(
                "Department deleted successfully",
                departmentService.deleteDepartment(id)
        );
    }
}