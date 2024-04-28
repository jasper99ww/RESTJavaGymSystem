package edu.pja.sri.s32073.sri02.service;

import edu.pja.sri.s32073.sri02.model.Activity;
import edu.pja.sri.s32073.sri02.model.ActivityDto;
import edu.pja.sri.s32073.sri02.repository.ActivityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, ModelMapper modelMapper) {
        this.activityRepository = activityRepository;
        this.modelMapper = modelMapper;
    }

    public List<ActivityDto> findAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ActivityDto> findActivityById(Long id) {
        return activityRepository.findById(id)
                .map(this::convertToDto);
    }

    public ActivityDto saveActivity(ActivityDto activityDto) {
        Activity activity = convertToEntity(activityDto);
        activity = activityRepository.save(activity);
        return convertToDto(activity);
    }

    public Optional<Boolean> updateActivity(Long id, ActivityDto activityDto) {
        return activityRepository.findById(id)
                .map(activity -> {
                    activityDto.setId(id);
                    Activity updatedActivity = convertToEntity(activityDto);
                    activityRepository.save(updatedActivity);
                    return Optional.of(true);
                })
                .orElse(Optional.empty());
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    private ActivityDto convertToDto(Activity activity) {
        return modelMapper.map(activity, ActivityDto.class);
    }

    private Activity convertToEntity(ActivityDto activityDto) {
        return modelMapper.map(activityDto, Activity.class);
    }
}