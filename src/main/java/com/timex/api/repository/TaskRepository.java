package com.timex.api.repository;

import com.timex.api.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectIdAndStatus(Long projectId, Task.Status status);

    List<Task> findByDueDateBefore(LocalDateTime dateTime);

    List<Task> findByProjectIdAndDueDateBetween(Long projectId, LocalDateTime start, LocalDateTime end);
}