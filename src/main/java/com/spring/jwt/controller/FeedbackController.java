<<<<<<< HEAD
package com.app.quvouch.controller;

import com.app.quvouch.Models.Feedback;
import com.app.quvouch.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        return service.saveFeedback(feedback);
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return service.getAllFeedback();
    }

    @GetMapping("/{id}")
    public Feedback getFeedback(@PathVariable UUID id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable UUID id) {
        service.deleteFeedback(id);
        return "Deleted Successfully";
=======
package com.spring.jwt.controller;

import com.spring.jwt.dto.FeedbackRequestDto;
import com.spring.jwt.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> createFeedback(
            @Valid @RequestBody FeedbackRequestDto request) {

        return ResponseEntity.ok(
                feedbackService.saveFeedback(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllFeedback() {
        return ResponseEntity.ok(
                feedbackService.getAllFeedback());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Deleted successfully");
>>>>>>> fec191664a493331f12a1e1fed807664e99cd9c4
    }
}
