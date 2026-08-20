package com.bmos.mes.service.record.business.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * 手写签名
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleSignInfo {

    /**
     * 签名url
     */
    private String signUrl;

    /**
     * 用户id
     */
    private String userId;

}
