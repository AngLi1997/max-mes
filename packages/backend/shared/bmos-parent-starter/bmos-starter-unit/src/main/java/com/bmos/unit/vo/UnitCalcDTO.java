package com.bmos.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 单位换算求和
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/25 10:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "单位换算求和")
public class UnitCalcDTO {

    /**
     * 求和列表
     */
    @ApiModelProperty(value = "求和列表")
    private List<UnitCalc> list = new ArrayList<>();

    /**
     * 求和目标转换单位id
     */
    @NotNull
    @ApiModelProperty(value = "求和目标转换单位id", example = "1", required = true)
    private Long targetUnitId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel(value = "单位换算求和参数")
    public static final class UnitCalc {

        /**
         * 单位id
         */
        @NotNull
        @ApiModelProperty(value = "单位id", example = "1", required = true)
        private Long unitId;

        /**
         * value
         */
        @NotBlank
        @ApiModelProperty(value = "值", example = "1.01", required = true)
        private String value;

        /**
         * 0
         */
        public static UnitCalc ZERO = UnitCalc.builder().value("0").build();
    }
}
