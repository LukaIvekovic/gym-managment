package hr.fer.gymmanagment.groupclass.service;

import hr.fer.gymmanagment.GymManagmentException;
import hr.fer.gymmanagment.groupclass.dto.GroupClassDto;
import hr.fer.gymmanagment.groupclass.entity.GroupClass;
import hr.fer.gymmanagment.groupclass.mapper.GroupClassMapper;
import hr.fer.gymmanagment.groupclass.repository.GroupClassRepository;
import hr.fer.gymmanagment.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupClassService {
    private final GroupClassRepository groupClassRepository;
    private final GroupClassMapper groupClassMapper;

    @Transactional
    public GroupClassDto createGroupClass(GroupClassDto dto) {
        GroupClass entity = groupClassMapper.map(dto);
        GroupClass savedEntity = groupClassRepository.save(entity);
        return groupClassMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<GroupClassDto> getAllGroupClasses() {
        return groupClassRepository.findAll()
                .stream()
                .map(groupClassMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<GroupClassDto> getGroupClassById(Integer id) {
        return groupClassRepository.findById(id)
                .map(groupClassMapper::map);
    }

    @Transactional
    public GroupClassDto updateGroupClass(Integer id, GroupClassDto dto) {
        return groupClassRepository.findById(id)
                .map(existingClass -> {
                    GroupClass updatedClass = groupClassMapper.map(dto);
                    existingClass.setName(updatedClass.getName());
                    existingClass.setDescription(updatedClass.getDescription());
                    existingClass.setType(updatedClass.getType());
                    existingClass.setDateTime(updatedClass.getDateTime());
                    existingClass.setDuration(updatedClass.getDuration());
                    existingClass.setMaxParticipants(updatedClass.getMaxParticipants());
                    return groupClassMapper.map(groupClassRepository.save(existingClass));
                })
                .orElseThrow(() -> new GymManagmentException("Group class not found with id: " + id));
    }

    @Transactional
    public void deleteGroupClass(Integer id) {
        groupClassRepository.deleteById(id);
    }

    @Transactional
    public GroupClassDto addParticipant(Integer classId, User participant) {
        GroupClass updatedClass = groupClassRepository.findById(classId)
                .map(groupClass -> {
                    groupClass.getParticipants().add(participant);
                    return groupClassRepository.save(groupClass);
                })
                .orElseThrow(() -> new GymManagmentException("Group class not found with id: " + classId));
        return groupClassMapper.map(updatedClass);
    }

    @Transactional
    public GroupClassDto removeParticipant(Integer classId, User participant) {
        GroupClass updatedClass = groupClassRepository.findById(classId)
                .map(groupClass -> {
                    groupClass.getParticipants().remove(participant);
                    return groupClassRepository.save(groupClass);
                })
                .orElseThrow(() -> new GymManagmentException("Group class not found with id: " + classId));
        return groupClassMapper.map(updatedClass);
    }
}

