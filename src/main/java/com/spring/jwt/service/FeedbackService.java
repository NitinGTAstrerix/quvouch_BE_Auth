package com.app.quvouch.service;

import com.app.quvouch.Models.Feedback;
import com.app.quvouch.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository repository;

    public Feedback saveFeedback(Feedback feedback) {
        return repository.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return repository.findAll();
    }

    public Feedback getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteFeedback(UUID id) {
        repository.deleteById(id);
    }
}
