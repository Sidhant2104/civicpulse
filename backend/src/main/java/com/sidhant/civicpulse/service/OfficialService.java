package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.dto.CreateOfficialRequestDto;
import com.sidhant.civicpulse.dto.CreateOfficialResponseDto;
import com.sidhant.civicpulse.dto.GetOfficialResponseDto;
import com.sidhant.civicpulse.dto.UpdateOfficialRequestDto;
import com.sidhant.civicpulse.exception.ConflictException;
import com.sidhant.civicpulse.exception.ForbiddenException;
import com.sidhant.civicpulse.exception.NotFoundException;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfficialService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private DepartmentRepository departmentRepository;
    public OfficialService(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, DepartmentRepository departmentRepository){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.departmentRepository=departmentRepository;
    }

    // 1: Create Official
    public CreateOfficialResponseDto createOfficial(CreateOfficialRequestDto request, String adminEmail){
        User admin = userRepo.findByEmail(adminEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if(admin.getRole() != Role.ADMIN){
            throw new ForbiddenException(
                    "Only admins can create officials"
            );
        }

        Optional<User> existingUser =
                userRepo.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            throw new ConflictException(
                    "User already exists"
            );
        }
        Department department =
                departmentRepository.findById(
                        request.getDepartmentId()
                ).orElseThrow(() ->
                        new NotFoundException("Department not found"));
        String encodedPassword =
                passwordEncoder.encode(
                        request.getPassword()
                );
        User official = new User();

        official.setId(UUID.randomUUID().toString());
        official.setName(request.getName());
        official.setEmail(request.getEmail());
        official.setPhoneNo(request.getPhoneNo());
        official.setPassword(encodedPassword);

        official.setDepartmentId(request.getDepartmentId());
        official.setLevel(request.getLevel());

        official.setRole(Role.OFFICIAL);
        official.setCreatedAt(LocalDateTime.now());
        User savedOfficial =
                userRepo.save(official);


        CreateOfficialResponseDto response =
                new CreateOfficialResponseDto();

        response.setId(savedOfficial.getId());
        response.setName(savedOfficial.getName());
        response.setEmail(savedOfficial.getEmail());
        response.setMessage("Official created successfully");
        return response;
    }

    // 2: Get Official
    public List<GetOfficialResponseDto> getAllOfficials(){
        List<User> officials = userRepo.findByRole(Role.OFFICIAL);
        return officials.stream()
                .map(user -> {
                    GetOfficialResponseDto dto =
                            new GetOfficialResponseDto();

                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setPhoneNo(user.getPhoneNo());
                    dto.setDepartmentId(user.getDepartmentId());
                    dto.setLevel(user.getLevel());
                    dto.setCreatedAt(user.getCreatedAt());

                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    //3: Update Official
    public CreateOfficialResponseDto updateOfficial(String officialId, UpdateOfficialRequestDto dto){
        User official =  userRepo.findById(officialId).orElseThrow(()->new NotFoundException("Official not found"));

        official.setName(dto.getName());
        official.setPhoneNo(dto.getPhoneNo());
        official.setLevel(dto.getLevel());

        Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(() ->
                        new NotFoundException("Department not found"));

        official.setDepartmentId(dto.getDepartmentId());

        User updatedOfficial = userRepo.save(official);

        CreateOfficialResponseDto response = new CreateOfficialResponseDto();

        response.setId(updatedOfficial.getId());
        response.setName(updatedOfficial.getName());
        response.setEmail(updatedOfficial.getEmail());
        response.setMessage("Official updated successfully");
        return response;
    }
}
