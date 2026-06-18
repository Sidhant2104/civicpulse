package com.sidhant.civicpulse.controller;

import com.sidhant.civicpulse.dto.*;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.service.AuthService;
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
    public CreateOfficialResponseDto createOfficial(
            @RequestBody CreateOfficialRequestDto request){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String adminEmail = authentication.getName();

        return officialService.createOfficial(
                request,
                adminEmail
        );
    }

    //2: Get Official
    @GetMapping
    public List<GetOfficialResponseDto> getAllOfficials() {
        return officialService.getAllOfficials();
    }

    //3: Update Official
    @PutMapping("/{id}")
    public CreateOfficialResponseDto updateOfficial(@PathVariable String id,@RequestBody UpdateOfficialRequestDto dto){
        return officialService.updateOfficial(id, dto);
    }

}