package hr.fer.gymmanagment.membership.service;

import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.membership.dto.MembershipDto;
import hr.fer.gymmanagment.membership.entity.Membership;
import hr.fer.gymmanagment.membership.mapper.MembershipMapper;
import hr.fer.gymmanagment.membership.repository.MembershipRepository;
import hr.fer.gymmanagment.membership.repository.MembershipTypeRepository;
import hr.fer.gymmanagment.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipTypeRepository membershipTypeRepository;

    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;

    @Transactional
    public MembershipDto createMembership(User user, MembershipDto dto) {
        var membershipType = membershipTypeRepository.findById(dto.typeId())
                .orElseThrow(() -> new NotFoundException("Membership type not found with id: " + dto.typeId()));

        Membership entity = membershipMapper.map(dto);

        entity.setUser(user);
        entity.setMembershipType(membershipType);

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
    public List<MembershipDto> getAllUserMemberships(User user) {
        return membershipRepository.findByUser(user)
                .stream()
                .map(membershipMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MembershipDto> getMembershipById(Integer id) {
        return membershipRepository.findById(id)
                .map(membershipMapper::map);
    }

    @Transactional
    public MembershipDto updateMembership(Integer id, User user, MembershipDto dto) {
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
