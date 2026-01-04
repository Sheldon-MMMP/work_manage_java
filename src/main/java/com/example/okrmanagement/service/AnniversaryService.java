package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.AnniversaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnniversaryService {
    @Autowired
    private AnniversaryRepository anniversaryRepository;

    public Anniversary createAnniversary(Anniversary anniversary, User user) {
        anniversary.setUser(user);
        return anniversaryRepository.save(anniversary);
    }

    public List<Anniversary> getAnniversaries(User user) {
        return anniversaryRepository.findByUserId(user.getId());
    }

    public Anniversary updateAnniversary(Long anniversaryId, Anniversary updatedAnniversary, User user) {
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(() -> new RuntimeException("Anniversary not found"));

        if (!anniversary.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this anniversary");
        }

        anniversary.setTitle(updatedAnniversary.getTitle());
        anniversary.setDescription(updatedAnniversary.getDescription());
        anniversary.setAnniversaryDate(updatedAnniversary.getAnniversaryDate());
        anniversary.setCoverUrl(updatedAnniversary.getCoverUrl());

        return anniversaryRepository.save(anniversary);
    }

    public void deleteAnniversary(Long anniversaryId, User user) {
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(() -> new RuntimeException("Anniversary not found"));

        if (!anniversary.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this anniversary");
        }

        anniversaryRepository.delete(anniversary);
    }
}