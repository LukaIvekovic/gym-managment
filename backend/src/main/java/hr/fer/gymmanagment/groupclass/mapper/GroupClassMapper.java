package hr.fer.gymmanagment.groupclass.mapper;

import hr.fer.gymmanagment.groupclass.dto.GroupClassDto;
import hr.fer.gymmanagment.groupclass.entity.GroupClass;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupClassMapper {
    GroupClass map(GroupClassDto dto);
    GroupClassDto map(GroupClass entity);
}
