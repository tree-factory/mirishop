package com.hh.mirishop.activity.like.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LikeTypeConverter implements AttributeConverter<LikeType, Integer> {

    // DB에 저장되는 값
    @Override
    public Integer convertToDatabaseColumn(LikeType attribute) {
        if (attribute == null)
            return null;
        return attribute.toDbValue();
    }

    // DB에서 Entity로 값을 넣을때 리턴되는 값
    @Override
    public LikeType convertToEntityAttribute(Integer dbData) {
        return LikeType.from(dbData);
    }
}
