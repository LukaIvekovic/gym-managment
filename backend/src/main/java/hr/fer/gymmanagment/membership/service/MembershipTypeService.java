package hr.fer.gymmanagment.membership.service;

import hr.fer.gymmanagment.common.NotFoundException;
import hr.fer.gymmanagment.membership.dto.MembershipTypeDto;
import hr.fer.gymmanagment.membership.entity.MembershipType;
import hr.fer.gymmanagment.membership.mapper.MembershipMapper;
import hr.fer.gymmanagment.membership.repository.MembershipTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipTypeService {
    private final MembershipTypeRepository membershipTypeRepository;
    private final MembershipMapper membershipMapper;

    @Transactional
    public MembershipTypeDto createMembershipType(MembershipTypeDto dto) {
        MembershipType entity = membershipMapper.map(dto);
        MembershipType savedEntity = membershipTypeRepository.save(entity);
        return membershipMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<MembershipTypeDto> getAllMembershipTypes() {
        return membershipTypeRepository.findAll()
                .stream()
                .map(membershipMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<MembershipTypeDto> getMembershipTypeById(Integer id) {
        return membershipTypeRepository.findById(id)
                .map(membershipMapper::map);
    }

    @Transactional
    public MembershipTypeDto updateMembershipType(Integer id, MembershipTypeDto dto) {
        return membershipTypeRepository.findById(id)
                .map(existingType -> {
                    MembershipType updatedType = membershipMapper.map(dto);
                    existingType.setName(updatedType.getName());
                    existingType.setDescription(updatedType.getDescription());
                    existingType.setPrice(updatedType.getPrice());
                    existingType.setDuration(updatedType.getDuration());
                    return membershipMapper.map(membershipTypeRepository.save(existingType));
                })
                .orElseThrow(() -> new NotFoundException("Membership type not found with id: " + id));
    }

    @Transactional
    public void deleteMembershipType(Integer id) {
        membershipTypeRepository.deleteById(id);
    }
}
