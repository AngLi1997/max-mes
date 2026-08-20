package com.bmos.mes.service.weigh.centre2.dashboard.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 今日工单分页查询DTO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/28 10:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("今日工单分页查询DTO")
public class TodayTicketQueryDTO extends BasePage {

} 