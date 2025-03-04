package com.group2.handyman.controller;

import com.group2.handyman.dto.request.JobCreateDto;
import com.group2.handyman.dto.request.JobUpdateDto;
import com.group2.handyman.model.Job;
import com.group2.handyman.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "${handyman.cors.allowed-origins}")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Job> createJob(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody JobCreateDto jobDto) {
        Job job = jobService.createJob(userId, jobDto);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.isJobOwner(#id)")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobUpdateDto jobDto) {
        Job job = jobService.updateJob(id, jobDto);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@securityService.isJobOwner(#id)")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Job>> getJobsBySkills(@RequestParam Set<String> skills) {
        List<Job> jobs = jobService.findBySkills(skills);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/budget")
    public ResponseEntity<List<Job>> getJobsByBudgetRange(
            @RequestParam double minBudget,
            @RequestParam double maxBudget) {
        List<Job> jobs = jobService.findByBudgetRange(minBudget, maxBudget);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Job>> getJobsByLocation(@PathVariable String location) {
        List<Job> jobs = jobService.findByLocation(location);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<Job>> getJobsByUser(@PathVariable Long userId) {
        List<Job> jobs = jobService.findByUser(userId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/worker/{workerId}")
    @PreAuthorize("@securityService.isCurrentWorker(#workerId)")
    public ResponseEntity<List<Job>> getJobsByWorker(@PathVariable Long workerId) {
        List<Job> jobs = jobService.findByWorker(workerId);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("@securityService.isJobWorker(#id)")
    public ResponseEntity<Job> completeJob(
            @PathVariable Long id,
            @RequestAttribute("workerId") Long workerId) {
        Job job = jobService.completeJob(id, workerId);
        return ResponseEntity.ok(job);
    }

    @PostMapping("/{id}/rate")
    @PreAuthorize("@securityService.isJobOwner(#id)")
    public ResponseEntity<Job> rateJob(
            @PathVariable Long id,
            @RequestParam double rating) {
        Job job = jobService.rateJob(id, rating);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String keyword) {
        List<Job> jobs = jobService.searchJobs(keyword);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Job>> getRecentJobs(@RequestParam(defaultValue = "10") int limit) {
        List<Job> jobs = jobService.getRecentJobs(limit);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/user/{userId}/completed")
    @PreAuthorize("@securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<Job>> getCompletedJobs(@PathVariable Long userId) {
        List<Job> jobs = jobService.getCompletedJobs(userId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/user/{userId}/pending")
    @PreAuthorize("@securityService.isCurrentUser(#userId)")
    public ResponseEntity<List<Job>> getPendingJobs(@PathVariable Long userId) {
        List<Job> jobs = jobService.getPendingJobs(userId);
        return ResponseEntity.ok(jobs);
    }
}
