package com.bmos.mes.service.mcp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/6/12 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextView<T> {

    /**
     * 视图类型
     * 默认为 "chart"，表示图表视图
     */
    private String viewType = "text";

    /**
     * 图表数据
     */
    private T data;

    public TextView(T data) {
        this.data = data;
    }
}
