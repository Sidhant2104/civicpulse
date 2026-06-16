package com.sidhant.civicpulse.config;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sidhant.civicpulse.model.Department;
import com.sidhant.civicpulse.model.Level;
import com.sidhant.civicpulse.model.Role;
import com.sidhant.civicpulse.model.User;
import com.sidhant.civicpulse.repository.DepartmentRepository;
import com.sidhant.civicpulse.repository.UserRepo;

//@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final DepartmentRepository departmentRepository;

    public DataInitializer(UserRepo userRepo, DepartmentRepository departmentRepository) {
        this.userRepo = userRepo;
        this.departmentRepository = departmentRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() > 0) {
            return;
        }

        List<Department> departments = departmentRepository.findAll();
        for (Department dept : departments) {

            for (Level level : Level.values()) {

                boolean exists = userRepo.existsByDepartmentIdAndLevelAndRole(
                        dept.getId(), level, Role.OFFICIAL);

                if (exists)
                    continue;

                User user = new User();

                user.setId(UUID.randomUUID().toString());
                user.setName(dept.getName() + " " + level);
                user.setRole(Role.OFFICIAL);
                user.setDepartmentId(dept.getId());
                user.setLevel(level);

                userRepo.save(user);

            }
        }
    }
}
