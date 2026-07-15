package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.service.OfficialService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/officials")
public class OfficialController {

    private OfficialService officialService;

    public OfficialController(OfficialService officialService) {
        this.officialService = officialService;
    }

    //1: Create Official
    @PostMapping
    public ApiResponse<CreateOfficialResponseDto> createOfficial(
            @RequestBody CreateOfficialRequestDto request){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String adminEmail = authentication.getName();

        return ApiResponse.success(
                "Official created successfully",
                officialService.createOfficial(
                        request,
                        adminEmail
                )
        );
    }

    //2: Get All Officials
    @GetMapping
    public ApiResponse<List<GetOfficialResponseDto>> getAllOfficials() {

        return ApiResponse.success(
                "Officials fetched successfully",
                officialService.getAllOfficials()
        );
    }

    //3: Update Official
    @PutMapping("/{id}")
    public ApiResponse<CreateOfficialResponseDto> updateOfficial(
            @PathVariable String id,
            @RequestBody UpdateOfficialRequestDto dto){

        return ApiResponse.success(
                "Official updated successfully",
                officialService.updateOfficial(id, dto)
        );
    }
}