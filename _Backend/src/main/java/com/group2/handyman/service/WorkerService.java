package com.group2.handyman.service;

import com.group2.handyman.dto.request.WorkerCreateDto;
import com.group2.handyman.dto.request.WorkerUpdateDto;
import com.group2.handyman.model.Job;
import com.group2.handyman.model.Worker;
import java.util.List;
import java.util.Set;

public interface WorkerService {
    List<Worker> getAllWorkers();
    Worker getWorkerById(Long id);
    Worker createWorker(WorkerCreateDto workerDto);
    Worker updateWorker(Long id, WorkerUpdateDto workerDto);
    void deleteWorker(Long id);
    List<Worker> findBySkill(String skill);
    List<Worker> findByRating(double minRating);
    List<Worker> findByLocation(String location);
    void updateRating(Long workerId, double rating);
    void updateCredit(Long workerId, double amount);
    String getContactInfo(Long workerId);
    List<String> getTransactionHistory(Long workerId);
    String getWorkingHours(Long workerId);
    Set<Job> getWorkerJobs(Long workerId);
    Worker findByEmailOrUsername(String identifier);
    boolean validatePassword(Worker worker, String password);
    List<Worker> searchWorkers(String keyword);
}
