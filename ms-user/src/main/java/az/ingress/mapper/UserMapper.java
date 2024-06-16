package az.ingress.mapper;

import az.ingress.model.dto.request.RegisterRequest;
import az.ingress.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "username", source = "email")
    User requestToEntity(RegisterRequest registerRequest);
}
