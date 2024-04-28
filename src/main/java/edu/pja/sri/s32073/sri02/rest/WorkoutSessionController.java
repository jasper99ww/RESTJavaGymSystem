package edu.pja.sri.s32073.sri02.rest;

import edu.pja.sri.s32073.sri02.model.WorkoutSessionDto;
import edu.pja.sri.s32073.sri02.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    @Autowired
    public WorkoutSessionController(WorkoutSessionService workoutSessionService) {
        this.workoutSessionService = workoutSessionService;
    }

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDto>> getAllWorkoutSessions() {
        List<WorkoutSessionDto> workoutSessionDto = workoutSessionService.findAllSessions();
        return new ResponseEntity<>(workoutSessionDto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionDto> getWorkoutSessionById(@PathVariable Long id) {
        return workoutSessionService.findSessionById(id)
                .map(sessionDto -> new ResponseEntity<>(sessionDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<WorkoutSessionDto> createWorkoutSession(@Valid @RequestBody WorkoutSessionDto sessionDto) {
        Optional<WorkoutSessionDto> savedSessionDto = workoutSessionService.saveSession(sessionDto);
        if(savedSessionDto.isPresent()) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedSessionDto.get().getId())
                    .toUri();
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(location);
            return new ResponseEntity<>(savedSessionDto.get(), headers, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSessionDto> updateWorkoutSession(@PathVariable Long id, @Valid @RequestBody WorkoutSessionDto sessionDto) {
        Optional<WorkoutSessionDto> updatedSession = workoutSessionService.updateSession(id, sessionDto);
        if (updatedSession.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutSession(@PathVariable Long id) {
        boolean exists = workoutSessionService.findSessionById(id).isPresent();
        if (exists) {
            workoutSessionService.deleteSession(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}