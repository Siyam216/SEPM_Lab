package com.example.student_management_system.config;

import com.example.student_management_system.repository.StudentRepository;
import com.example.student_management_system.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // Tells Spring Security how to find users by email - searches Student then Teacher tables
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> studentRepository.findByEmail(username)
                .map(student -> (org.springframework.security.core.userdetails.UserDetails) student)
                .orElseGet(() -> teacherRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username)));
    }
}
