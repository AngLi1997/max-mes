package com.bmos.expression.util;

import com.bmos.expression.enums.RoundingEnum;
import com.bmos.expression.model.RoundingVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
public class RoundingUtil {

    public static List<RoundingVO> getRoundingList() {
        RoundingEnum[] values = RoundingEnum.values();
        return Arrays.stream(values).map(item -> {
            RoundingVO vo = new RoundingVO();
            vo.setLabel(item.getLabel());
            vo.setValue(item.getCode());
            return vo;
        }).collect(Collectors.toList());
    }
}
