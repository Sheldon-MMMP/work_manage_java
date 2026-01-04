package com.example.okrmanagement.controller;

import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.service.AnniversaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/anniversaries")
public class AnniversaryController {
    @Autowired
    private AnniversaryService anniversaryService;

    @PostMapping
    public ResponseEntity<Anniversary> createAnniversary(@RequestBody Anniversary anniversary, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Anniversary newAnniversary = anniversaryService.createAnniversary(anniversary, user);
        return ResponseEntity.ok(newAnniversary);
    }

    @GetMapping
    public ResponseEntity<List<Anniversary>> getAnniversaries(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Anniversary> anniversaries = anniversaryService.getAnniversaries(user);
        return ResponseEntity.ok(anniversaries);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anniversary> updateAnniversary(@PathVariable Long id, @RequestBody Anniversary anniversary, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Anniversary updatedAnniversary = anniversaryService.updateAnniversary(id, anniversary, user);
        return ResponseEntity.ok(updatedAnniversary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnniversary(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        anniversaryService.deleteAnniversary(id, user);
        return ResponseEntity.noContent().build();
    }
}