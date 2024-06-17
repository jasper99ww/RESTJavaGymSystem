package edu.pja.sri.s32073.sri02.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionDto {
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Activity ID is required")
    private Long activityId;

    @NotNull(message = "Start date and time are required")
    private LocalDateTime startDateTime;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int durationInMinutes;

    @Min(value = 0, message = "Calories burned cannot be negative")
    private int caloriesBurned;
}