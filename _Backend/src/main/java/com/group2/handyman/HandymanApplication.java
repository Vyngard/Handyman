package com.group2.handyman;

import com.group2.handyman.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HandymanApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandymanApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository,
                                WorkerRepository workerRepository,
                                JobRepository jobRepository,
                                SkillRepository skillRepository,
                                PasswordEncoder passwordEncoder) {
        return (args) -> {
            // Create sample users
            // Create sample users
            User user1 = new User(
                "john_doe",
                "john@example.com",
                passwordEncoder.encode("password123"),
                "John Doe",
                "+1234567890",
                "New York"
            );
            userRepository.save(user1);

            User user2 = new User(
                "jane_smith",
                "jane@example.com",
                passwordEncoder.encode("password123"),
                "Jane Smith",
                "+1987654321",
                "Los Angeles"
            );
            userRepository.save(user2);

            // Create sample workers
            Worker worker1 = new Worker(
                "bob_builder",
                "bob@example.com",
                passwordEncoder.encode("password123"),
                "Bob Builder",
                "+1122334455",
                "Chicago",
                "Experienced handyman with 10 years of experience",
                "09:00-17:00"
            );
            worker1.setPreferredCommunication(Worker.PreferredCommunication.PHONE);

            Worker worker2 = new Worker(
                "mike_mechanic",
                "mike@example.com",
                passwordEncoder.encode("password123"),
                "Mike Mechanic",
                "+1567890123",
                "Houston",
                "Specialized in plumbing and electrical work",
                "08:00-16:00"
            );
            worker2.setPreferredCommunication(Worker.PreferredCommunication.EMAIL);

            // Create and save skills
            Skill plumbing = skillRepository.save(new Skill("Plumbing"));
            Skill electrical = skillRepository.save(new Skill("Electrical"));
            Skill carpentry = skillRepository.save(new Skill("Carpentry"));
            Skill painting = skillRepository.save(new Skill("Painting"));

            // Add skills to workers
            worker1.addSkill(carpentry);
            worker1.addSkill(painting);
            workerRepository.save(worker1);

            worker2.addSkill(plumbing);
            worker2.addSkill(electrical);
            workerRepository.save(worker2);

            // Create sample jobs
            Job job1 = new Job();
            job1.setClient(user1);
            job1.setWorker(worker1);
            job1.setTitle("Kitchen Renovation");
            job1.setDescription("Complete kitchen renovation including cabinets and countertops");
            job1.setBudget(5000.0);
            job1.setCompleted(true);
            job1.setRating(4.5);
            job1.getSkills().add(carpentry);
            job1.getSkills().add(painting);
            jobRepository.save(job1);

            Job job2 = new Job();
            job2.setClient(user2);
            job2.setWorker(worker2);
            job2.setTitle("Bathroom Plumbing");
            job2.setDescription("Fix leaking pipes and install new shower");
            job2.setBudget(2000.0);
            job2.setCompleted(false);
            job2.getSkills().add(plumbing);
            jobRepository.save(job2);

            // Update worker ratings
            worker1.addRating(4.5);
            worker2.addRating(4.8);
            workerRepository.save(worker1);
            workerRepository.save(worker2);
        };
    }
}
