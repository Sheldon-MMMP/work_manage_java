package com.example.okrmanagement.service;

import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.AnniversaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.common.ErrorCode;

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
                .orElseThrow(() -> new BusinessException(ErrorCode.ANNIVERSARY_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !anniversary.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        anniversary.setTitle(updatedAnniversary.getTitle());
        anniversary.setDescription(updatedAnniversary.getDescription());
        anniversary.setAnniversaryDate(updatedAnniversary.getAnniversaryDate());
        anniversary.setCoverUrl(updatedAnniversary.getCoverUrl());

        return anniversaryRepository.save(anniversary);
    }

    public void deleteAnniversary(Long anniversaryId, User user) {
        Anniversary anniversary = anniversaryRepository.findById(anniversaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ANNIVERSARY_NOT_FOUND));

        Long userId = user.getId();
        if (userId == null || !anniversary.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }

        anniversaryRepository.delete(anniversary);
    }
}