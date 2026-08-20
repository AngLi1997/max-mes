package com.bmos.platform.service.system.menu.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum TypeEnum {

    /**
     * 终端类型
     */
    PLATFORM_SYSTEM_WEB_SIDE("1",100L),
    PRODUCTION_SYSTEM_WEB_SIDE("2",120L),
    PRODUCTION_SYSTEM_MOBILE_END("3",121L),
    AUDIT_SYSTEM_WEB_PAGE("4",111L);

    private String type;

    private Long typeId;

    public static TypeEnum convertByType(String type) {
        return Stream.of(values())
            .filter(bean -> bean.type.equals(type))
            .findAny()
            .orElse(null);
    }

    public static TypeEnum convertByTypeId(Long typeId) {
        return Stream.of(values())
            .filter(bean -> bean.typeId.equals(typeId))
            .findAny()
            .orElse(null);
    }
}
