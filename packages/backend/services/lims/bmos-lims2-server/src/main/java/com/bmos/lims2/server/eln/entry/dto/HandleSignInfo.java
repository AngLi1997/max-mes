package com.bmos.lims2.server.eln.entry.dto;

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
