package hr.fer.gymmanagment.groupclass.mapper;

import hr.fer.gymmanagment.groupclass.dto.GroupClassDto;
import hr.fer.gymmanagment.groupclass.entity.GroupClass;
import hr.fer.gymmanagment.groupclass.entity.GroupClassType;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupClassMapper {
    GroupClass map(GroupClassDto dto);

    @Mapping(target = "currentParticipants", source = ".", qualifiedByName = "toNumberOfCurrentParticipants")
    GroupClassDto map(GroupClass entity);

    default GroupClassType map(String type) {
        return GroupClassType.valueOf(type.toUpperCase());
    }

    default String map(GroupClassType type) {
        return type.name().toLowerCase();
    }

    @Named("toNumberOfCurrentParticipants")
    default Integer toNumberOfCurrentParticipants(GroupClass groupClass) {
        return groupClass.getParticipants() != null ? groupClass.getParticipants().size() : 0;
    }
}
