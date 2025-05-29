package hr.fer.gymmanagment.membership.mapper;

import hr.fer.gymmanagment.membership.dto.MembershipDto;
import hr.fer.gymmanagment.membership.dto.MembershipTypeDto;
import hr.fer.gymmanagment.membership.entity.Membership;
import hr.fer.gymmanagment.membership.entity.MembershipType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MembershipMapper {
    @Mapping(target = "typeId", source = "membershipType.id")
    MembershipDto map(Membership membership);
    Membership map(MembershipDto membershipDto);

    MembershipType map(MembershipTypeDto membershipTypeDto);
    MembershipTypeDto map(MembershipType membershipType);
}
