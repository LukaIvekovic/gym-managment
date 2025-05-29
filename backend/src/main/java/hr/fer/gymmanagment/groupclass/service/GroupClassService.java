package hr.fer.gymmanagment.groupclass.service;

import hr.fer.gymmanagment.GymManagmentException;
import hr.fer.gymmanagment.groupclass.dto.GroupClassDto;
import hr.fer.gymmanagment.groupclass.entity.GroupClass;
import hr.fer.gymmanagment.groupclass.mapper.GroupClassMapper;
import hr.fer.gymmanagment.groupclass.repository.GroupClassRepository;
import hr.fer.gymmanagment.security.entity.pojo.DashboardUserDetails;
import hr.fer.gymmanagment.security.repository.UserRepository;
import hr.fer.gymmanagment.websocket.ParticipantCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupClassService {
    private final GroupClassRepository groupClassRepository;
    private final GroupClassMapper groupClassMapper;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public GroupClassDto createGroupClass(GroupClassDto dto) {
        GroupClass entity = groupClassMapper.map(dto);
        GroupClass savedEntity = groupClassRepository.save(entity);
        return groupClassMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<GroupClassDto> getAllGroupClasses() {
        return groupClassRepository.findAllIncoming()
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
    public GroupClassDto addParticipant(Integer classId, DashboardUserDetails participant) {
        var participantEntity = userRepository.findById(participant.getId())
                .orElseThrow(() -> new GymManagmentException("User not found with id: " + participant.getId()));

        GroupClass updatedClass = groupClassRepository.findById(classId)
                .map(groupClass -> {
                    if (groupClass.getParticipants().contains(participantEntity)) {
                        throw new GymManagmentException("User already registered for this class");
                    }

                    groupClass.getParticipants().add(participantEntity);
                    return groupClassRepository.save(groupClass);
                })
                .orElseThrow(() -> new GymManagmentException("Group class not found with id: " + classId));

        GroupClassDto result = groupClassMapper.map(updatedClass);
        broadcastParticipantCount(updatedClass);
        return result;
    }

    @Transactional
    public GroupClassDto removeParticipant(Integer classId, DashboardUserDetails participant) {
        var participantEntity = userRepository.findById(participant.getId())
                .orElseThrow(() -> new GymManagmentException("User not found with id: " + participant.getId()));

        GroupClass updatedClass = groupClassRepository.findById(classId)
                .map(groupClass -> {
                    groupClass.getParticipants().remove(participantEntity);
                    return groupClassRepository.save(groupClass);
                })
                .orElseThrow(() -> new GymManagmentException("Group class not found with id: " + classId));

        var result = groupClassMapper.map(updatedClass);
        broadcastParticipantCount(updatedClass);
        return result;
    }

    private void broadcastParticipantCount(GroupClass groupClass) {
        ParticipantCountDto countDto = new ParticipantCountDto(
                groupClass.getId(),
                groupClass.getParticipants().size()
        );
        messagingTemplate.convertAndSend(
                "/topic/group-class/" + groupClass.getId() + "/participants",
                countDto
        );
    }
}

