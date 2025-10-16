package com.timex.api.config;

import com.timex.api.model.Project;
import com.timex.api.model.Task;
import com.timex.api.model.User;
import com.timex.api.repository.ProjectRepository;
import com.timex.api.repository.TaskRepository;
import com.timex.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!prod") // Don't run in production
    public CommandLineRunner initializeData() {
        return args -> {
            // Only initialize if no users exist
            if (userRepository.count() == 0) {
                log.info("Initializing sample data...");

                // Create admin user
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@timex.com")
                        .fullName("Admin User")
                        .roles(Collections.singleton(User.Role.ROLE_ADMIN))
                        .build();
                userRepository.save(admin);

                // Create regular user
                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .email("user@timex.com")
                        .fullName("Regular User")
                        .roles(Collections.singleton(User.Role.ROLE_USER))
                        .build();
                userRepository.save(user);

                // Create projects for the regular user
                Project project1 = Project.builder()
                        .name("Website Redesign")
                        .description("Redesign of company website with modern UI/UX")
                        .user(user)
                        .build();
                projectRepository.save(project1);

                Project project2 = Project.builder()
                        .name("Mobile App Development")
                        .description("Develop a mobile app for both Android and iOS")
                        .user(user)
                        .build();
                projectRepository.save(project2);

                // Create tasks for Project 1
                Set<Task> project1Tasks = new HashSet<>();

                project1Tasks.add(Task.builder()
                        .title("Design mockups")
                        .description("Create wireframes and design mockups")
                        .status(Task.Status.COMPLETED)
                        .priority(1)
                        .estimatedHours(8.0)
                        .actualHours(10.0)
                        .dueDate(LocalDateTime.now().plusDays(1))
                        .project(project1)
                        .build());

                project1Tasks.add(Task.builder()
                        .title("Frontend implementation")
                        .description("Implement the frontend using HTML, CSS, and JavaScript")
                        .status(Task.Status.IN_PROGRESS)
                        .priority(2)
                        .estimatedHours(20.0)
                        .actualHours(10.0)
                        .dueDate(LocalDateTime.now().plusDays(7))
                        .project(project1)
                        .build());

                project1Tasks.add(Task.builder()
                        .title("Backend integration")
                        .description("Connect frontend to backend services")
                        .status(Task.Status.TODO)
                        .priority(3)
                        .estimatedHours(15.0)
                        .dueDate(LocalDateTime.now().plusDays(14))
                        .project(project1)
                        .build());

                // Create tasks for Project 2
                Set<Task> project2Tasks = new HashSet<>();

                project2Tasks.add(Task.builder()
                        .title("Requirements gathering")
                        .description("Gather and document requirements for mobile app")
                        .status(Task.Status.COMPLETED)
                        .priority(1)
                        .estimatedHours(10.0)
                        .actualHours(12.0)
                        .dueDate(LocalDateTime.now().minusDays(7))
                        .project(project2)
                        .build());

                project2Tasks.add(Task.builder()
                        .title("UI Design")
                        .description("Design app UI using Figma")
                        .status(Task.Status.IN_PROGRESS)
                        .priority(2)
                        .estimatedHours(15.0)
                        .actualHours(8.0)
                        .dueDate(LocalDateTime.now().plusDays(2))
                        .project(project2)
                        .build());

                project2Tasks.add(Task.builder()
                        .title("Android development")
                        .description("Implement the Android version of the app")
                        .status(Task.Status.TODO)
                        .priority(3)
                        .estimatedHours(40.0)
                        .dueDate(LocalDateTime.now().plusDays(30))
                        .project(project2)
                        .build());

                project2Tasks.add(Task.builder()
                        .title("iOS development")
                        .description("Implement the iOS version of the app")
                        .status(Task.Status.TODO)
                        .priority(3)
                        .estimatedHours(40.0)
                        .dueDate(LocalDateTime.now().plusDays(30))
                        .project(project2)
                        .build());

                taskRepository.saveAll(project1Tasks);
                taskRepository.saveAll(project2Tasks);

                log.info("Sample data initialization completed");
            }
        };
    }
}