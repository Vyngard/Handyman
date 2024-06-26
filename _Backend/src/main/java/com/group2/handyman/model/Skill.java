package com.group2.handyman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    @JsonIgnore
    private Worker worker;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;

	public Skill() {}

	public Skill(String name) {
		this.name = name;
	}

	public Skill(String name, Worker worker) {
		this.name = name;
		this.worker = worker;
	}
	
	
	public Skill(String name, Job job ) {
		this.name = name;
		this.job = job;
		
	}
	
	public Skill(String name, Worker worker, Job job ) {
		this.name = name;
		this.worker = worker;
		this.job =job;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}
    
	 public Job getJob() {
			return job;
		}

	public void setJob(Job job) {
			this.job = job;
	}
    
}
