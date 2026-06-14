package com.sidhant.civicpulse.service;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private DepartmentRepository departmentRepository;
    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, DepartmentRepository departmentRepository){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.departmentRepository = departmentRepository;
    }


    // 1: SIGNUP/REGISTER USER
    public SignupResponseDto registerUser(SignupRequestDto request){
    Optional<User> userEmail =  userRepo.findByEmail((request.getEmail()));
    if(userEmail.isPresent()){
        throw new RuntimeException("User already exists");
    }

    String pass = request.getPassword();
    String encodedPassword = passwordEncoder.encode(pass);
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPhoneNo(request.getPhoneNo());
    user.setPassword(encodedPassword);
    user.setRole(Role.CITIZEN);
    user.setCreatedAt(LocalDateTime.now());
    User savedUser = userRepo.save(user);

    SignupResponseDto response = new SignupResponseDto();
    response.setId(savedUser.getId());
    response.setName(savedUser.getName());
    response.setEmail(savedUser.getEmail());
    response.setMessage("User registered successfully!");

    return response;
    }

    // 2: LOGIN USER:
    public LoginResponseDto LoginUser(LoginRequestDto request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Invalid email or password")); // just to be safe from  attackers
        // who can enumerate email

        String token = jwtService.generateToken(user);
        LoginResponseDto response = new LoginResponseDto();

        response.setToken(token);
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNo(user.getPhoneNo());
        response.setRole(user.getRole());
        response.setMessage("Login successful");

        return response;

    }

    public CreateOfficialResponseDto createOfficial(CreateOfficialRequestDto request, String adminEmail){
        User admin = userRepo.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(admin.getRole() != Role.ADMIN){
            throw new RuntimeException(
                    "Only admins can create officials"
            );
        }

        Optional<User> existingUser =
                userRepo.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            throw new RuntimeException(
                    "User already exists"
            );
        }
        Department department =
                departmentRepository.findById(
                        request.getDepartmentId()
                ).orElseThrow(() ->
                        new RuntimeException("Department not found"));
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

}
