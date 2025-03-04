package com.group2.handyman.service;

import com.group2.handyman.dto.request.JobCreateDto;
import com.group2.handyman.dto.request.JobUpdateDto;
import com.group2.handyman.model.Job;
import java.util.List;
import java.util.Set;

public interface JobService {
    List<Job> getAllJobs();
    Job getJobById(Long id);
    Job createJob(Long userId, JobCreateDto jobDto);
    Job updateJob(Long id, JobUpdateDto jobDto);
    void deleteJob(Long id);
    List<Job> findBySkills(Set<String> skills);
    List<Job> findByBudgetRange(double minBudget, double maxBudget);
    List<Job> findByLocation(String location);
    List<Job> findByUser(Long userId);
    List<Job> findByWorker(Long workerId);
    Job completeJob(Long jobId, Long workerId);
    Job rateJob(Long jobId, double rating);
    List<Job> searchJobs(String keyword);
    List<Job> getRecentJobs(int limit);
    List<Job> getCompletedJobs(Long userId);
    List<Job> getPendingJobs(Long userId);
    double calculateAverageRating(Long workerId);
    int countCompletedJobs(Long workerId);
}
