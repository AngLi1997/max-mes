package com.bmos.mes.service.plan.info.mq.topic;

import com.bmos.mes.mq.BaseMqTopic;
import com.bmos.mes.mq.annotation.Topic;
import com.bmos.mes.service.plan.info.mq.message.PlanStatusChangeMessage;

/**
 * @author yigaohui
 * @date 2024/7/3
 **/
@Topic("PLAN_STATUS_STATUS_TOPIC")
public interface PlanStatusChangeTopic extends BaseMqTopic<PlanStatusChangeMessage> {
}
