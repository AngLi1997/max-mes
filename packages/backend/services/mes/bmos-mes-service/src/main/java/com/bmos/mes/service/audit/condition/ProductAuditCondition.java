package com.bmos.mes.service.audit.condition;

import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.mes.service.audit.vo.AuditMessageVO;
import com.bmos.mes.service.formula.model.ProductFormula;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.lotrelease.manage.service.ILotReleaseService;
import com.bmos.mes.service.operate.service.OperateRuleService;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveService;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAuditCondition extends AbstractAuditDataCondition {
    public ProductAuditCondition(PlatformApiAdaptor platformApiAdaptor, ProcessService processService, PlanService planService,
                                 BatchRecordVersionService versionService, ProductFormulaConfigureService productService,
                                 OperateRuleService operateRuleService, BatchRecordArchiveService batchRecordArchiveService, ILotReleaseService lotReleaseService) {
        super(platformApiAdaptor, processService, planService, versionService, productService,operateRuleService, batchRecordArchiveService, lotReleaseService);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return productFormulaConfigureService.getAuditBusinessKey(deptIdList);
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        ProductFormula productFormula = productFormulaConfigureService.getOneByVersionId(businessId);
        AuditMessageVO vo = new AuditMessageVO();
        vo.setBusinessId(productFormula.getId());
        vo.setBusinessText("生产BOM名称：" + productFormula.getName() + "，版本号：" + productFormula.getVersion());
        return vo;
    }
}
