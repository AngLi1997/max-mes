package com.bmos.platform.service.system.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateSignaturePasswordResultVO {

    private String userId;

    private Boolean result;
}
