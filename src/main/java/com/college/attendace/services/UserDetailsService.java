package com.college.attendace.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.college.attendace.dao.UserDAO;
import com.college.attendace.models.User;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findUserByEmail(email);
        if (user.isPresent()) {
            User _user = user.get();
            return new org.springframework.security.core.userdetails.User(_user.getEmail(), _user.getPassword(),
                    new ArrayList<>());
        }
        return null;
    }
}
