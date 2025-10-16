package com.timex.api.dto;

import com.timex.api.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TaskDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        private Task.Status status;

        @Min(value = 1, message = "Priority must be between 1 and 5")
        @Max(value = 5, message = "Priority must be between 1 and 5")
        private Integer priority;

        private Double estimatedHours;

        private Double actualHours;

        private LocalDateTime dueDate;

        private Long projectId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private Task.Status status;
        private Integer priority;
        private Double estimatedHours;
        private Double actualHours;
        private LocalDateTime dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long projectId;
        private String projectName;
    }
}