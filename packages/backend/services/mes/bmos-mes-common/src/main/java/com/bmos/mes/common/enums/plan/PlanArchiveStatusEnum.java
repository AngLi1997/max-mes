package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author yigaohui
 * @date 2024/7/3
 **/
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PlanArchiveStatusEnum implements CommonEnum<String> {

    //待归档
    WAIT_ARCHIVE("待归档", "WAIT_ARCHIVE"),
    //归档中
    ARCHIVE_ING("归档中", "ARCHIVE_ING"),
    //已归档
    ARCHIVE_SUCCESS("已归档", "ARCHIVE_SUCCESS"),
    //归档失败
    ARCHIVE_FAIL("归档失败", "ARCHIVE_FAIL");


    private final String name;

    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }


    @JsonCreator
    public static PlanArchiveStatusEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(PlanArchiveStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }
}
