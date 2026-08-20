package com.bmos.lims2.server.inspect.scheme.validate;

import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检验方案名称唯一性校验器
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public class InspectionSchemeNameUniqueValidator implements ConstraintValidator<InspectionSchemeNameUnique, String> {

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Override
    public void initialize(InspectionSchemeNameUnique constraintAnnotation) {
        // 初始化，无需操作
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return true;
        }
        return inspectionSchemeMapper.checkNameDuplicate(name, null) == 0;
    }
} 