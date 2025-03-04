package com.group2.handyman.service.impl;

import com.group2.handyman.dto.request.JobCreateDto;
import com.group2.handyman.dto.request.JobUpdateDto;
import com.group2.handyman.exception.HandymanException;
import com.group2.handyman.exception.ResourceNotFoundException;
import com.group2.handyman.model.*;
import com.group2.handyman.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository,
                         UserRepository userRepository,
                         WorkerRepository workerRepository,
                         SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.workerRepository = workerRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", id.toString()));
    }

    @Override
    public Job createJob(Long userId, JobCreateDto jobDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        Job job = new Job();
        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setBudget(jobDto.getBudget());
        job.setClient(user);
        job.setCompleted(false);

        if (jobDto.getWorkerId() != null) {
            Worker worker = workerRepository.findById(jobDto.getWorkerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Worker", jobDto.getWorkerId().toString()));
            job.setWorker(worker);
        }

        if (jobDto.getRequiredSkills() != null && !jobDto.getRequiredSkills().isEmpty()) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : jobDto.getRequiredSkills()) {
                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName)));
                skills.add(skill);
            }
            job.setSkills(skills);
        }

        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, JobUpdateDto jobDto) {
        Job job = getJobById(id);

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setBudget(jobDto.getBudget());

        if (jobDto.getWorkerId() != null) {
            Worker worker = workerRepository.findById(jobDto.getWorkerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Worker", jobDto.getWorkerId().toString()));
            job.setWorker(worker);
        }

        if (jobDto.getRequiredSkills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : jobDto.getRequiredSkills()) {
                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName)));
                skills.add(skill);
            }
            job.setSkills(skills);
        }

        if (jobDto.isCompleted()) {
            job.setCompleted(true);
        }

        if (jobDto.getRating() != null) {
            job.setRating(jobDto.getRating());
        }

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job", id.toString());
        }
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> findBySkills(Set<String> skills) {
        return jobRepository.findBySkillsNameIn(skills);
    }

    @Override
    public List<Job> findByBudgetRange(double minBudget, double maxBudget) {
        return jobRepository.findByBudgetBetween(minBudget, maxBudget);
    }

    @Override
    public List<Job> findByLocation(String location) {
        return jobRepository.findByClientLocation(location);
    }

    @Override
    public List<Job> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));
        return jobRepository.findByClient(user);
    }

    @Override
    public List<Job> findByWorker(Long workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId.toString()));
        return jobRepository.findByWorker(worker);
    }

    @Override
    public Job completeJob(Long jobId, Long workerId) {
        Job job = getJobById(jobId);
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", workerId.toString()));

        if (!job.getWorker().getId().equals(workerId)) {
            throw new HandymanException("INVALID_WORKER", "This worker is not assigned to this job");
        }

        job.setCompleted(true);
        return jobRepository.save(job);
    }

    @Override
    public Job rateJob(Long jobId, double rating) {
        if (rating < 1 || rating > 5) {
            throw new HandymanException("INVALID_RATING", "Rating must be between 1 and 5");
        }

        Job job = getJobById(jobId);
        if (!job.isCompleted()) {
            throw new HandymanException("JOB_NOT_COMPLETED", "Cannot rate a job that is not completed");
        }

        job.setRating(rating);
        Job savedJob = jobRepository.save(job);

        // Update worker's average rating
        Worker worker = job.getWorker();
        if (worker != null) {
            double avgRating = calculateAverageRating(worker.getId());
            worker.setAverageRating(avgRating);
            workerRepository.save(worker);
        }

        return savedJob;
    }

    @Override
    public List<Job> searchJobs(String keyword) {
        return jobRepository.findByKeyword(keyword);
    }

    @Override
    public List<Job> getRecentJobs(int limit) {
        return jobRepository.findRecentJobs(limit);
    }

    @Override
    public List<Job> getCompletedJobs(Long userId) {
        return jobRepository.findByClientIdAndIsCompleted(userId, true);
    }

    @Override
    public List<Job> getPendingJobs(Long userId) {
        return jobRepository.findByClientIdAndIsCompleted(userId, false);
    }

    @Override
    public double calculateAverageRating(Long workerId) {
        List<Job> completedJobs = jobRepository.findByWorkerIdAndIsCompleted(workerId, true);
        if (completedJobs.isEmpty()) {
            return 0.0;
        }

        double totalRating = completedJobs.stream()
                .filter(job -> job.getRating() != null)
                .mapToDouble(Job::getRating)
                .sum();
        long ratedJobs = completedJobs.stream()
                .filter(job -> job.getRating() != null)
                .count();

        return ratedJobs > 0 ? totalRating / ratedJobs : 0.0;
    }

    @Override
    public int countCompletedJobs(Long workerId) {
        return jobRepository.countByWorkerIdAndIsCompleted(workerId, true);
    }
}
