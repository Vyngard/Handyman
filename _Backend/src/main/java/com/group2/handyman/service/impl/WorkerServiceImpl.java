package com.group2.handyman.service.impl;

import com.group2.handyman.dto.request.WorkerCreateDto;
import com.group2.handyman.dto.request.WorkerUpdateDto;
import com.group2.handyman.exception.HandymanException;
import com.group2.handyman.exception.ResourceNotFoundException;
import com.group2.handyman.model.*;
import com.group2.handyman.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WorkerServiceImpl(WorkerRepository workerRepository,
                           SkillRepository skillRepository,
                           PasswordEncoder passwordEncoder) {
        this.workerRepository = workerRepository;
        this.skillRepository = skillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker", id.toString()));
    }

    @Override
    public Worker createWorker(WorkerCreateDto workerDto) {
        if (workerRepository.findByEmailOrUsername(workerDto.getEmail(), workerDto.getUsername()) != null) {
            throw new HandymanException("DUPLICATE_WORKER", "Worker with this email or username already exists");
        }

        Worker worker = new Worker();
        worker.setUsername(workerDto.getUsername());
        worker.setPassword(passwordEncoder.encode(workerDto.getPassword()));
        worker.setEmail(workerDto.getEmail());
        worker.setDescription(workerDto.getDescription());
        worker.setLocation(workerDto.getLocation());
        worker.setWorkingHours(workerDto.getWorkingHours());
        worker.setPhone(workerDto.getPhone());
        worker.setPreferredCommunication(Worker.PreferredCommunication.valueOf(workerDto.getPreferredCommunication()));

        Worker savedWorker = workerRepository.save(worker);

        if (workerDto.getSkillNames() != null) {
            Set<Skill> skills = new HashSet<>();
            for (String skillName : workerDto.getSkillNames()) {
                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill(skillName);
                            return skillRepository.save(newSkill);
                        });
                skills.add(skill);
                skill.getWorkers().add(savedWorker);
            }
            savedWorker.setSkills(skills);
        }

        return workerRepository.save(savedWorker);
    }

    @Override
    public Worker updateWorker(Long id, WorkerUpdateDto workerDto) {
        Worker worker = getWorkerById(id);

        Worker existingWorker = workerRepository.findByEmailOrUsername(workerDto.getEmail(), workerDto.getUsername());
        if (existingWorker != null && !existingWorker.getId().equals(id)) {
            throw new HandymanException("DUPLICATE_WORKER", "Email or username already in use");
        }

        worker.setUsername(workerDto.getUsername());
        if (workerDto.getPassword() != null && !workerDto.getPassword().isEmpty()) {
            worker.setPassword(passwordEncoder.encode(workerDto.getPassword()));
        }
        worker.setEmail(workerDto.getEmail());
        worker.setDescription(workerDto.getDescription());
        worker.setLocation(workerDto.getLocation());
        worker.setWorkingHours(workerDto.getWorkingHours());
        worker.setPhone(workerDto.getPhone());

        if (workerDto.getSkillNames() != null) {
            Set<Skill> oldSkills = new HashSet<>(worker.getSkills());
            for (Skill skill : oldSkills) {
                skill.getWorkers().remove(worker);
            }
            worker.getSkills().clear();

            Set<Skill> newSkills = new HashSet<>();
            for (String skillName : workerDto.getSkillNames()) {
                Skill skill = skillRepository.findByName(skillName)
                        .orElseGet(() -> {
                            Skill newSkill = new Skill(skillName);
                            return skillRepository.save(newSkill);
                        });
                newSkills.add(skill);
                skill.getWorkers().add(worker);
            }
            worker.setSkills(newSkills);
        }

        return workerRepository.save(worker);
    }

    @Override
    public void deleteWorker(Long id) {
        if (!workerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Worker", id.toString());
        }
        workerRepository.deleteById(id);
    }

    @Override
    public List<Worker> findBySkill(String skill) {
        return workerRepository.findBySkillsName(skill);
    }

    @Override
    public List<Worker> findByRating(double minRating) {
        return workerRepository.findByRating(minRating);
    }

    @Override
    public List<Worker> findByLocation(String location) {
        return workerRepository.findByLocationContaining(location);
    }

    @Override
    @Transactional
    public void updateRating(Long workerId, double rating) {
        Worker worker = getWorkerById(workerId);
        worker.addRating(rating);
        workerRepository.save(worker);
    }

    @Override
    @Transactional
    public void updateCredit(Long workerId, double amount) {
        Worker worker = getWorkerById(workerId);
        worker.setCredit(worker.getCredit() + amount);
        workerRepository.save(worker);
    }

    @Override
    public String getContactInfo(Long workerId) {
        Worker worker = getWorkerById(workerId);
        return worker.getPreferredCommunication() == Worker.PreferredCommunication.EMAIL ?
                "Email: " + worker.getEmail() : "Phone: " + worker.getPhone();
    }

    @Override
    public List<String> getTransactionHistory(Long workerId) {
        Worker worker = getWorkerById(workerId);
        return worker.getPreviousTransactions();
    }

    @Override
    public String getWorkingHours(Long workerId) {
        Worker worker = getWorkerById(workerId);
        return worker.getWorkingHours();
    }

    @Override
    public Set<Job> getWorkerJobs(Long workerId) {
        Worker worker = getWorkerById(workerId);
        return new HashSet<>(worker.getJobs());
    }

    @Override
    public Worker findByEmailOrUsername(String identifier) {
        return workerRepository.findByEmailOrUsername(identifier, identifier);
    }

    @Override
    public boolean validatePassword(Worker worker, String password) {
        return passwordEncoder.matches(password, worker.getPassword());
    }

    @Override
    public List<Worker> searchWorkers(String keyword) {
        return workerRepository.findByKeyword(keyword);
    }
}
