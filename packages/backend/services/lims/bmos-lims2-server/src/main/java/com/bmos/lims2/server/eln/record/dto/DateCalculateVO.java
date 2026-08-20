package com.bmos.lims2.server.eln.record.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName DateCalculateVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/12/10 11:58
 */
@Setter
@Getter
@ToString
public class DateCalculateVO {

    /**
     * 计算结果
     */
    private String calculateResult;

    /**
     * 秒
     */
    private String timeSeconds;
}
