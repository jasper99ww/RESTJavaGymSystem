package edu.pja.sri.s32073.sri02.service;

import edu.pja.sri.s32073.sri02.model.Activity;
import edu.pja.sri.s32073.sri02.model.User;
import edu.pja.sri.s32073.sri02.model.WorkoutSession;
import edu.pja.sri.s32073.sri02.model.WorkoutSessionDto;
import edu.pja.sri.s32073.sri02.repository.ActivityRepository;
import edu.pja.sri.s32073.sri02.repository.UserRepository;
import edu.pja.sri.s32073.sri02.repository.WorkoutSessionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkoutSessionService(WorkoutSessionRepository workoutSessionRepository,
                                 ActivityRepository activityRepository,
                                 UserRepository userRepository,
                                 ModelMapper modelMapper) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<WorkoutSessionDto> findAllSessions() {
        List<WorkoutSession> sessions = workoutSessionRepository.findAll();
        return sessions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<WorkoutSessionDto> findSessionById(Long id) {
        return workoutSessionRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<WorkoutSessionDto> saveSession(WorkoutSessionDto sessionDto) {
        Optional<WorkoutSession> session = convertToEntity(sessionDto);
        if (session.isEmpty()) {
            return Optional.empty();
        }
        WorkoutSession savedSession = workoutSessionRepository.save(session.get());
        return Optional.of(convertToDto(savedSession));
    }

    public Optional<WorkoutSessionDto> updateSession(Long id, WorkoutSessionDto sessionDto) {
        if (!activityRepository.existsById(sessionDto.getActivityId()) || !userRepository.existsById(sessionDto.getUserId())) {
            return Optional.empty();
        }

        Optional<WorkoutSession> sessionOptional = workoutSessionRepository.findById(id);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }

        Optional<Activity> activityOptional = activityRepository.findById(sessionDto.getActivityId());
        Optional<User> userOptional = userRepository.findById(sessionDto.getUserId());
        if (activityOptional.isEmpty() || userOptional.isEmpty()) {
            return Optional.empty();
        }

        WorkoutSession session = sessionOptional.get();
        session.setActivity(activityOptional.get());
        session.setUser(userOptional.get());
        session.setStartDateTime(sessionDto.getStartDateTime());
        session.setDurationInMinutes(sessionDto.getDurationInMinutes());
        session.setCaloriesBurned(sessionDto.getCaloriesBurned());

        WorkoutSession updatedSession = workoutSessionRepository.save(session);
        WorkoutSessionDto updatedSessionDto = convertToDto(updatedSession);
        return Optional.of(updatedSessionDto);
    }

    public void deleteSession(Long id) {
        workoutSessionRepository.deleteById(id);
    }

    private WorkoutSessionDto convertToDto(WorkoutSession session) {
        return modelMapper.map(session, WorkoutSessionDto.class);
    }

    private Optional<WorkoutSession> convertToEntity(WorkoutSessionDto sessionDto) {
        if (!activityRepository.existsById(sessionDto.getActivityId()) || !userRepository.existsById(sessionDto.getUserId())) {
            return Optional.empty();
        }
        WorkoutSession session = new WorkoutSession();

        activityRepository.findById(sessionDto.getActivityId()).ifPresent(session::setActivity);
        userRepository.findById(sessionDto.getUserId()).ifPresent(session::setUser);

        modelMapper.map(sessionDto, session);
        return Optional.of(session);
    }
}