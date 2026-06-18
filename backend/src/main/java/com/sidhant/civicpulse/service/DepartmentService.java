package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.dto.CreateDepartmentRequestDto;
import com.sidhant.civicpulse.dto.CreateDepartmentResponseDto;
import com.sidhant.civicpulse.dto.DeleteDepartmentResponseDto;
import com.sidhant.civicpulse.dto.UpdateDepartmentResponseDto;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    private DepartmentRepository departmentRepository;
    private UserRepo userRepo;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepo userRepo){
        this.departmentRepository = departmentRepository;
        this.userRepo = userRepo;
    }

    // 1: Create Department
    public CreateDepartmentResponseDto createDepartment(CreateDepartmentRequestDto dto){
        Department existingDepartment = departmentRepository.findByName(dto.getName());
        if(existingDepartment != null){
            throw new RuntimeException("Department already exists");
        }

        Department department = new Department();

        department.setId(UUID.randomUUID().toString());
        department.setName(dto.getName());
        departmentRepository.save(department);

        //Response
        CreateDepartmentResponseDto response = new CreateDepartmentResponseDto();
        response.setId(department.getId());
        response.setName(department.getName());

        return response;
    }

    //2: Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // 3: Update Department:
    public UpdateDepartmentResponseDto updateDepartment(String departmentId, CreateDepartmentRequestDto dto){
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        dept.setName(dto.getName());
        dept.setName(dto.getName());

        UpdateDepartmentResponseDto response =
                new UpdateDepartmentResponseDto();
        response.setId(dept.getId());
        response.setName(dept.getName());
        return response;
    }

    //4: DeleteDepartment
    public DeleteDepartmentResponseDto deleteDepartment(String departmentId){
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        if(userRepo.existsByDepartmentId(departmentId)){
            throw new RuntimeException("Department has assigned users");
        }
        departmentRepository.delete(department);

        //response
        DeleteDepartmentResponseDto response = new DeleteDepartmentResponseDto();
        response.setMessage("Department deleted successfully");
        return response;
    }


}
