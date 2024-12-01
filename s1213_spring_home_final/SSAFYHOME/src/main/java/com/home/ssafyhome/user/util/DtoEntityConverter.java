package com.home.ssafyhome.user.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class DtoEntityConverter {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    // DTO -> Entity 변환
    public static <D, E> E convertToEntity(D dto, Class<E> entityClass) {
        if (dto == null) {
            return null; // null을 그대로 반환
        }
        return modelMapper.map(dto, entityClass);
    }

    // Entity -> DTO 변환
    public static <E, D> D convertToDto(E entity, Class<D> dtoClass) {
        if (entity == null) {
            return null; // null을 그대로 반환
        }
        return modelMapper.map(entity, dtoClass);
    }
}
