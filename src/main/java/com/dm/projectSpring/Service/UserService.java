package com.dm.projectSpring.Service;

import com.dm.projectSpring.Entity.User;
import com.dm.projectSpring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User updateProfile(int userId, byte[] profilePic, String bio) {

        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        user.setProfilepic(profilePic);
        user.setBio(bio);

        return userRepository.save(user);
    }
}
