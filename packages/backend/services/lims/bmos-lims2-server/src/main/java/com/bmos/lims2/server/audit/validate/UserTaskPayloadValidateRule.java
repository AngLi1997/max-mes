package com.bmos.lims2.server.audit.validate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.audit.engine.core.element.base.BaseElement;
import com.bmos.audit.engine.core.element.enums.ElementTypeEnum;
import com.bmos.audit.engine.core.element.task.UserTask;
import com.bmos.audit.engine.core.exception.InfiniteGraphException;
import com.bmos.audit.engine.core.validator.rule.PayloadValidateRule;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.constants.FlowAuditConstant;
import com.bmos.lims2.common.enums.FlowAuditCodeEnum;
import com.bmos.lims2.server.audit.dto.CheckoutFlowAuditMegDTO;
import com.bmos.lims2.server.audit.dto.CheckoutFlowAuditUserDTO;
import com.bmos.lims2.server.audit.vo.SettingVO;

import java.util.List;
import java.util.Map;

public class UserTaskPayloadValidateRule implements PayloadValidateRule {

    private final List<CheckoutFlowAuditUserDTO> userList;

    private final List<CheckoutFlowAuditMegDTO> megUserList;

    public UserTaskPayloadValidateRule(List<CheckoutFlowAuditUserDTO> userList, List<CheckoutFlowAuditMegDTO> megUserList) {
        this.userList = userList;
        this.megUserList = megUserList;
    }

    @Override
    public boolean validate(List<BaseElement> element) {
        Map<String, List<CheckoutFlowAuditMegDTO>> megUserList = CollectionUtils.convertMultiMap(this.megUserList, CheckoutFlowAuditMegDTO::getMessageType);
        Map<String, List<CheckoutFlowAuditUserDTO>> userList = CollectionUtils.convertMultiMap(this.userList, CheckoutFlowAuditUserDTO::getAssigneeType);
        Map<String, List<CheckoutFlowAuditUserDTO>> userNodeMap = CollectionUtils.convertMultiMap(this.userList, CheckoutFlowAuditUserDTO::getNodeId);
        element.forEach(item -> {
            if (ElementTypeEnum.USER_TASK.getType().equals(item.getType())) {
                List<CheckoutFlowAuditUserDTO> userNodeList = userNodeMap.get(item.getKey());
                if (CollUtil.isEmpty(userNodeList)) {
                    throw new InfiniteGraphException("流程未找到人员");
                }
                UserTask userTask = (UserTask) item;
                Map<String, Object> payload = userTask.getPayload();
                if (CollUtil.isEmpty(payload) || ObjectUtil.isEmpty(payload.get(FlowAuditConstant.SETTINGS))) {
                    throw new InfiniteGraphException("流程参数不能为空");
                }
                String settings = (String) payload.get(FlowAuditConstant.SETTINGS);
                SettingVO setting = JsonUtils.parseObject(settings, SettingVO.class);

                if (CollUtil.isEmpty(setting.getButtons())) {
                    throw new InfiniteGraphException("流程参数不能为空");
                }
                if (setting.getButtons().contains(FlowAuditCodeEnum.MAKE.getValue())) {
                    if (CollUtil.isEmpty(megUserList.get(FlowAuditCodeEnum.MAKE.getValue()))) {
                        throw new InfiniteGraphException("未配置抄送人，校验不通过");
                    }
                }
                if (FlowAuditCodeEnum.COUNTERSIGN.getValue().equals(setting.getCompleteType())) {
                    if (setting.getStrategy().contains(FlowAuditCodeEnum.ALL_ROLE.getValue()) && CollUtil.isEmpty(userList.get(FlowAuditCodeEnum.ALL_ROLE.getValue()))) {
                        throw new InfiniteGraphException("未配置审核角色，不可配置角色会签策略");
                    }
                    if (setting.getStrategy().contains(FlowAuditCodeEnum.ALL_USER.getValue()) && CollUtil.isEmpty(userList.get(FlowAuditCodeEnum.ALL_USER.getValue()))) {
                        throw new InfiniteGraphException("未配置审核人员，不可配置人员会签策略");
                    }
                }
            }
        });
        return true;
    }
}
