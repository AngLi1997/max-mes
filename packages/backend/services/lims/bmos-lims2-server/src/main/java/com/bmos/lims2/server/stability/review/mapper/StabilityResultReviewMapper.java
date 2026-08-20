package com.bmos.lims2.server.stability.review.mapper;

import com.bmos.lims2.server.stability.review.dto.StabilityAuditHeaderDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性结果审核Mapper
 */
@Mapper
public interface StabilityResultReviewMapper {

    /**
     * 查询 SAMPLE_AUDIT_PENDING 状态的稳定性检验单候选列表（用于与工作流待办合并）
     */
    List<StabilityResultReviewDTO> selectCandidatesForReview(@Param("query") StabilityResultReviewQueryDTO query);

    /**
     * 查询稳定性审核头部信息（请验信息面板所需全部字段）
     */
    StabilityAuditHeaderDTO selectStabilityAuditHeader(@Param("orderId") Long orderId);
}

