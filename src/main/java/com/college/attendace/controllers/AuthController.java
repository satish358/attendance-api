package com.college.attendace.controllers;

import com.college.attendace.dao.FacultyDAO;
import com.college.attendace.dao.StudentDAO;
import com.college.attendace.enums.UserRoleEnum;
import com.college.attendace.exceptions.ConfirmPasswordNotMatchedException;
import com.college.attendace.exceptions.UserAlreadyPresentException;
import com.college.attendace.models.Faculty;
import com.college.attendace.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.college.attendace.dao.UserDAO;
import com.college.attendace.dto.*;
import com.college.attendace.models.User;
import com.college.attendace.services.UserDetailsService;
import com.college.attendace.utils.JWTUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserDAO userDAO;
    @Autowired
    FacultyDAO facultyDAO;
    @Autowired
    StudentDAO studentDAO;


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<BasicResponseDTO<RegisterResponseDTO>> registerUser(
            @RequestBody RegisterRequestDTO r) {
        BasicResponseDTO<RegisterResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        basicResponseDTO.setData(null);
        basicResponseDTO.setSuccess(false);
        if (!r.getPassword().equals(r.getConfirmPassword())) {
            throw new ConfirmPasswordNotMatchedException();
        }
        if (userDAO.existsByEmail(r.getEmail())) {
           throw new UserAlreadyPresentException();
        }
        User user = new User();
        user.setFirstName(r.getFirstName());
        user.setLastName(r.getLastName());
        user.setEmail(r.getEmail());
        user.setRole(r.getRole());
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(r.getPassword()));
        user.setCreatedOn(new Date());
        userDAO.save(user);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        if(UserRoleEnum.STUDENT.equals(user.getRole() )){
            Student student = new Student(null, r.getCollege(), r.getPrn(), r.getAge(), r.getCourse(), user, new ArrayList<>());
            studentDAO.save(student);
        } else {
            Faculty faculty = new Faculty(null,user, r.getCollege(), LocalDate.now(), new ArrayList<>() );
            facultyDAO.save(faculty);
        }

        basicResponseDTO.setData(
                new RegisterResponseDTO(jwtUtil.generateToken(userDetails), user.getEmail(), user.getFirstName()));
        basicResponseDTO.setSuccess(true);
        return new ResponseEntity<>(basicResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<BasicResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        BasicResponseDTO<LoginResponseDTO> result = this.loginHelper(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword());
        return new ResponseEntity<>(result, result.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    public BasicResponseDTO<LoginResponseDTO> loginHelper(String email, String password) {
        BasicResponseDTO<LoginResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        Optional<User> _user = userDAO.findUserByEmail(email);
        if (_user.isEmpty()) {
            basicResponseDTO.setMessage("User not found");
            return basicResponseDTO;
        }
        User user = _user.get();
        if (!user.getActive()) {
            basicResponseDTO.setMessage("User not active");
            return basicResponseDTO;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password));
        } catch (BadCredentialsException e) {
            basicResponseDTO.setMessage("Credentials not matched");
            return basicResponseDTO;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(jwtUtil.generateToken(userDetails));
        loginResponseDTO.setUser(user);
        basicResponseDTO.setData(loginResponseDTO);
        basicResponseDTO.setSuccess(true);
        return basicResponseDTO;
    }

    @GetMapping("test")
    public ResponseEntity<BasicResponseDTO<String>> testConnection(){
        return ResponseEntity.ok(new BasicResponseDTO<>(false, "OK", "OK"));
    }

}
