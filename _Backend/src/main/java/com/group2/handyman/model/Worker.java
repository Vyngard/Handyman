package com.group2.handyman.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "workers")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "working_hours")
    private String workingHours;

    public enum PreferredCommunication {
        EMAIL, PHONE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_communication")
    private PreferredCommunication preferredCommunication = PreferredCommunication.EMAIL;

    private double credit;
    private double averageRating;
    private int totalJobs;
    private List<String> previousTransactions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "worker_skills",
        joinColumns = @JoinColumn(name = "worker_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Worker() {
    }

    public Worker(String username, String email, String password, String fullName, 
                 String phone, String location, String description, String workingHours) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.location = location;
        this.description = description;
        this.workingHours = workingHours;
        this.credit = 0.0;
        this.averageRating = 0.0;
        this.totalJobs = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(int totalJobs) {
        this.totalJobs = totalJobs;
    }

    public PreferredCommunication getPreferredCommunication() {
        return preferredCommunication;
    }

    public void setPreferredCommunication(PreferredCommunication preferredCommunication) {
        this.preferredCommunication = preferredCommunication;
    }

    public List<String> getPreviousTransactions() {
        return previousTransactions;
    }

    public void setPreviousTransactions(List<String> previousTransactions) {
        this.previousTransactions = previousTransactions;
    }

    public void addRating(double rating) {
        this.averageRating = ((this.averageRating * this.totalJobs) + rating) / (this.totalJobs + 1);
        this.totalJobs++;
    }

    public void addTransaction(String transaction) {
        this.previousTransactions.add(transaction);
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.getWorkers().add(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.getWorkers().remove(this);
    }

    public void addJob(Job job) {
        jobs.add(job);
        job.setWorker(this);
    }

    public void removeJob(Job job) {
        jobs.remove(job);
        job.setWorker(null);
    }

    public void addMessage(Message message) {
        messages.add(message);
        message.setWorker(this);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
        message.setWorker(null);
    }
}
