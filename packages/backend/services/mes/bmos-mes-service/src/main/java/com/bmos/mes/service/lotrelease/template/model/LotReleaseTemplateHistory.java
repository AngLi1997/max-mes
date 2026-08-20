package com.bmos.mes.service.lotrelease.template.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateOperateType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 批签发模板操作历史
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 14:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_release_template_history")
public class LotReleaseTemplateHistory extends BaseDO {

    /**
     * 批签发版本id
     */
    private Long templateVersionId;

    /**
     * 操作类型
     */
    private LotReleaseTemplateOperateType operateType;

    /**
     * 操作人id
     */
    private String operateUserId;

    /**
     * 操作人名称
     */
    private String operateUserName;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 操作备注
     */
    private String operateRemark;

    /**
     * 操作摘要
     */
    private String comment;

    /**
     * 审核节点名称
     */
    private String nodeName;

    /**
     * 扩展信息
     */
    private String ext;

    public static LotReleaseTemplateHistory create(Long lotReleaseTemplateVersionId, LotReleaseTemplateOperateType operateType, String operateRemark, String ext) {
        LotReleaseTemplateHistory history = new LotReleaseTemplateHistory();
        history.setTemplateVersionId(lotReleaseTemplateVersionId);
        history.setOperateType(operateType);
        history.setOperateUserId(SysUserHolder.getUser().getUserId());
        history.setOperateUserName(SysUserHolder.getUser().getUserName());
        history.setOperateTime(LocalDateTime.now());
        history.setOperateRemark(operateRemark);
        history.setExt(ext);
        return history;
    }
}
