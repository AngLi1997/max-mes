package com.bmos.mes.mq.config;

import com.bmos.common.util.date.DateUtil;
import com.bmos.mes.mq.enums.MqDelayLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Author yigaohui
 * @Description mq 消息配置
 * @Date 2023/7/21 10:25
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MqConfig implements Serializable {

    /**
     * tag
     */
    private String tag;

    /**
     * 设置了同一hashKey的消息可保证消费顺序
     */
    private String hashKey;

    /**
     * 延迟消息的延迟级别
     */
    private MqDelayLevel delayLevel;

    /**
     * 消息交付时间
     */
    private Long startDeliverTime;

    /**
     * 设置延时时长，会根据当前时间 + 延时时长确定最终发送时间
     *
     * @param delayDuration 延时 时长
     */
    public void setDelayTime(Duration delayDuration) {
        this.setStartDeliverTime(System.currentTimeMillis() + delayDuration.toMillis());
    }

    public void setStartDeliverTime(LocalDateTime startDeliverTime) {
        setStartDeliverTime(DateUtil.toMill(startDeliverTime));
    }

    public void setStartDeliverTime(Long startDeliverTime) {
        this.startDeliverTime = startDeliverTime;
    }
}
