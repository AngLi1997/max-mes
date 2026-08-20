package com.bmos.logging.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeaderKeyEnum {

    HEADER_KEY_OPERATION("Bmos-Operation"),
    HEADER_KEY_BUSINESS("Bmos-Operation-Business"),
    HEADER_KEY_MENU("Bmos-MenuId");
    private final String key;

}
