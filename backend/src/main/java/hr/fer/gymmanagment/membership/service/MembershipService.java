package hr.fer.gymmanagment.membership.service;

import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.membership.dto.MembershipDto;
import hr.fer.gymmanagment.membership.entity.Membership;
import hr.fer.gymmanagment.membership.mapper.MembershipMapper;
import hr.fer.gymmanagment.membership.repository.MembershipRepository;
import hr.fer.gymmanagment.membership.repository.MembershipTypeRepository;
import hr.fer.gymmanagment.security.entity.pojo.DashboardUserDetails;
import hr.fer.gymmanagment.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipTypeRepository membershipTypeRepository;

    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;

    private final UserRepository userRepository;

    @Transactional
    public MembershipDto createMembership(DashboardUserDetails user, MembershipDto dto) {
        var membershipType = membershipTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("Membership type not found with id: " + dto.typeId()));
        var userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + user.getId()));

        Membership entity = membershipMapper.map(dto);

        entity.setUser(userEntity);
        entity.setMembershipType(membershipType);
        entity.setEndDate(dto.startDate().plusDays(membershipType.getDuration()));

        Membership savedEntity = membershipRepository.save(entity);
        return membershipMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<MembershipDto> getAllMemberships() {
        return membershipRepository.findAll()
                .stream()
                .map(membershipMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MembershipDto> getAllUserMemberships(DashboardUserDetails userDetails) {
        var user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userDetails.getId()));

        return membershipRepository.findByUser(user)
                .stream()
                .map(membershipMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public MembershipDto getActiveUserMembership(DashboardUserDetails userDetails) {
        var user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userDetails.getId()));

        return membershipRepository.findFirstByUserAndEndDateAfter(user, LocalDateTime.now())
                .map(membershipMapper::map)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<MembershipDto> getMembershipById(Integer id) {
        return membershipRepository.findById(id)
                .map(membershipMapper::map);
    }

    @Transactional
    public MembershipDto updateMembership(Integer id, DashboardUserDetails userDetails, MembershipDto dto) {
        var user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userDetails.getId()));

        return membershipRepository.findById(id)
                .map(existingMembership -> {
                    var membershipType = membershipTypeRepository.findById(dto.typeId())
                            .orElseThrow(() -> new NotFoundException("Membership type not found with id: " + dto.typeId()));

                    existingMembership.setUser(user);
                    existingMembership.setMembershipType(membershipType);
                    existingMembership.setStartDate(dto.startDate());

                    return membershipMapper.map(membershipRepository.save(existingMembership));
                })
                .orElseThrow(() -> new NotFoundException("Membership not found with id: " + id));
    }

    @Transactional
    public void deleteMembership(Integer id) {
        membershipRepository.deleteById(id);
    }
}
