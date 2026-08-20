package com.bmos.lims2.server.inspect.scheme.validate;

import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 检验方案版本号唯一性校验器
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
public class InspectionSchemeVersionNoUniqueValidator implements ConstraintValidator<InspectionSchemeVersionNoUnique, Object> {

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    private String schemeIdField;
    private String versionNoField;

    @Override
    public void initialize(InspectionSchemeVersionNoUnique constraintAnnotation) {
        this.schemeIdField = constraintAnnotation.schemeIdField();
        this.versionNoField = constraintAnnotation.versionNoField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        Long schemeId = (Long) beanWrapper.getPropertyValue(schemeIdField);
        String versionNo = (String) beanWrapper.getPropertyValue(versionNoField);

        if (schemeId == null || versionNo == null) {
            return true;
        }

        return inspectionSchemeVersionMapper.checkVersionNoDuplicate(schemeId, versionNo, null) == 0;
    }
} 