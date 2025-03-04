package com.group2.handyman.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import com.group2.handyman.dto.request.WorkerCreateDto;
import com.group2.handyman.dto.request.WorkerUpdateDto;
import com.group2.handyman.exception.HandymanException;
import com.group2.handyman.model.Job;
import com.group2.handyman.model.Worker;
import com.group2.handyman.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workers")
@CrossOrigin(origins = "${handyman.cors.allowed-origins}")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentWorker(#id) or hasRole('USER')")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Long id) {
        Worker worker = workerService.getWorkerById(id);
        return ResponseEntity.ok(worker);
    }
    
    @GetMapping("/skills/{skill}")
    public ResponseEntity<List<Worker>> getWorkersBySkill(@PathVariable String skill) {
        List<Worker> workers = workerService.findBySkill(skill);
        if (workers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(workers);
    }
    
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<Worker>> getWorkerByRating(@PathVariable double rating) {
        List<Worker> workers = workerService.findByRating(rating);
        if (workers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(workers);
    }

    @PostMapping
    public ResponseEntity<Worker> createWorker(@Valid @RequestBody WorkerCreateDto workerDto) {
        Worker worker = workerService.createWorker(workerDto);
        return new ResponseEntity<>(worker, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Worker> rateWorker(@PathVariable Long id, @RequestParam double rating) {
        if (rating < 1 || rating > 5) {
            throw new HandymanException("INVALID_RATING", "Rating must be between 1 and 5");
        }
        workerService.updateRating(id, rating);
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isCurrentWorker(#id)")
    public ResponseEntity<Worker> updateWorker(
            @PathVariable Long id,
            @Valid @RequestBody WorkerUpdateDto workerDto) {
        Worker worker = workerService.updateWorker(id, workerDto);
        return ResponseEntity.ok(worker);
    }

    @GetMapping("/{id}/jobs")
    @PreAuthorize("@securityService.isCurrentWorker(#id)")
    public ResponseEntity<Set<Job>> getWorkerJobs(@PathVariable Long id) {
        Set<Job> jobs = workerService.getWorkerJobs(id);
        if (jobs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}/contact")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getWorkerContactInfo(@PathVariable Long id) {
        String contactInfo = workerService.getContactInfo(id);
        return ResponseEntity.ok(contactInfo);
    }

    @GetMapping("/{id}/transactions")
    @PreAuthorize("@securityService.isCurrentWorker(#id)")
    public ResponseEntity<List<String>> getWorkerTransactions(@PathVariable Long id) {
        List<String> transactions = workerService.getTransactionHistory(id);
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}/working-hours")
    public ResponseEntity<String> getWorkerWorkingHours(@PathVariable Long id) {
        String workingHours = workerService.getWorkingHours(id);
        if (workingHours == null || workingHours.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(workingHours);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Worker>> searchWorkers(@RequestParam String keyword) {
        List<Worker> workers = workerService.searchWorkers(keyword);
        if (workers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Worker>> getWorkersByLocation(@PathVariable String location) {
        List<Worker> workers = workerService.findByLocation(location);
        if (workers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(workers);
    }
}
