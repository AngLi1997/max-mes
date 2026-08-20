export enum CLIENT_ID {
  WEB = 'WEB',
  APP = 'APP',
}

export const MessageRouterMap: Map<string, { key: string; path: string }> = new Map([
  [
    '120020002',
    {
      key: '/app/bmos-mes/',
      path: 'product-config/record-review',
    },
  ],
  [
    '120020005',
    {
      key: '/app/bmos-mes/',
      path: 'product-config/formula-approval',
    },
  ],
  [
    '120020007',
    {
      key: '/app/bmos-mes/',
      path: 'product-config/process-approval',
    },
  ],
  [
    '120030002',
    {
      key: '/app/bmos-mes/',
      path: 'production-management/plan-approval',
    },
  ],
  [
    '120030004',
    {
      key: '/app/bmos-mes/',
      path: 'production-management/instruction-confirmation',
    },
  ],
  [
    '120040005',
    {
      key: '/app/bmos-mes/',
      path: 'batch-release/review',
    },
  ],
  [
    '120080003',
    {
      key: '/app/bmos-mes/',
      path: 'batch-records/review',
    },
  ],
  // 操作规程审批
  [
    '120020013',
    {
      key: '/app/bmos-mes/',
      path: 'product-config/operating-approval',
    },
  ],
  // 血源审批
  [
    '170020007', // 验收审核
    {
      key: '/app/bmos-bsms/',
      path: 'specimenManagement/acceptance-audit',
    },
  ],
  [
    '170020013', // 标本-外观不合格审核
    {
      key: '/app/bmos-bsms/',
      path: 'specimenManagement/appearance-unqualified-audit',
    },
  ],
  [
    '170040007', // 血浆-外观不合格审核
    {
      key: '/app/bmos-bsms/',
      path: 'plasmaManagement/appearance-unqualified-audit',
    },
  ],
  [
    '170050004', // 检疫期报告审核
    {
      key: '/app/bmos-bsms/',
      path: 'quarantineManagement/quarantine-report-audit',
    },
  ],
  [
    '170070003', // 不合格核查记录审核
    {
      key: '/app/bmos-bsms/',
      path: 'unqualifiedPlasmaMng/checkRecord-review',
    },
  ],
  [
    '170070006', // 不合格核查报告审核
    {
      key: '/app/bmos-bsms/',
      path: 'unqualifiedPlasmaMng/verification-report-review',
    },
  ],
  [
    '170100002', // 投料出库审核
    {
      key: '/app/bmos-bsms/',
      path: 'outboundMng/feeding-discharging-plan',
    },
  ],
  [
    '170100003', // 科研调用出库审核
    {
      key: '/app/bmos-bsms/',
      path: 'outboundMng/call-out-library-plan',
    },
  ],
  [
    '170100004', // 销毁出库审核
    {
      key: '/app/bmos-bsms/',
      path: 'outboundMng/destroy-warehouse-audit',
    },
  ],
  [
    '170100005', // 质量管理授权人批准
    {
      key: '/app/bmos-bsms/',
      path: 'outboundMng/authorizer-approved',
    },
  ],
  [
    '170060001', // 标本请验审核
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/specimen-examination-review',
    },
  ],
  [
    '170060003', // 放行单审核
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/release-review',
    },
  ],
  [
    '170060005', // 投料质保审核
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/feeding-audit',
    },
  ],
  [
    '170060006', // 科研调用质保审核
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/quality-assurance-audit',
    },
  ],
  [
    '170060007', // 销毁出库初审
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/destruction-trial',
    },
  ],
  [
    '170060008', // 销毁出库复审
    {
      key: '/app/bmos-bsms/',
      path: 'qualityAssuranceManagement/destruction-review',
    },
  ],
  [
    '170040008', // 血浆库存预警
    {
      key: '/app/bmos-bsms/',
      path: 'plasmaManagement/plasma-stock-warning',
    },
  ],
  [
    '170020008', // 标本库存预警
    {
      key: '/app/bmos-bsms/',
      path: 'specimenManagement/sample-stock-warning',
    },
  ],
  // 集中化lims
  [
    '210020002', // 接收审核
    {
      key: '/app/bmos-lisms/',
      path: 'specimenManagement/receive-review',
    },
  ],
  [
    '210020004', // 拒收审核
    {
      key: '/app/bmos-lisms/',
      path: 'specimenManagement/reject-review',
    },
  ],
  [
    '210030003', // 检验数据审核
    {
      key: '/app/bmos-lisms/',
      path: 'inspectionManagement/inspection-data-audit',
    },
  ],
  [
    '210040001', // 检验报告中心
    {
      key: '/app/bmos-lisms/',
      path: 'reportingManagement/center',
    },
  ],
  [
    '210050003', // 领用库消耗审核
    {
      key: '/app/bmos-lisms/',
      path: 'laboratoryResource/material-management/consumption-audit',
    },
  ],
  [
    '210050005', // 领用库报废审核
    {
      key: '/app/bmos-lisms/',
      path: 'laboratoryResource/material-management/scrap-audit',
    },
  ],
  [
    '210060007', // 抽检申请审核
    {
      key: '/app/bmos-lisms/',
      path: 'materialWarehouse/spot-check-audit',
    },
  ],
  [
    '210060009', // 物料领用审核
    {
      key: '/app/bmos-lisms/',
      path: 'materialWarehouse/collect-use-audit',
    },
  ],
  [
    '210060010', // 物料报废审核
    {
      key: '/app/bmos-lisms/',
      path: 'materialWarehouse/scrap-audit',
    },
  ],
  [
    '210060011', // 物料退货审核
    {
      key: '/app/bmos-lisms/',
      path: 'materialWarehouse/goods-return-audit',
    },
  ],
  [
    '210080006', // 文件模板审核
    {
      key: '/app/bmos-lisms/',
      path: 'configManagement/file-template/file-audit',
    },
  ],
  [
    '210060013', // 预警管理
    {
      key: '/app/bmos-lisms/',
      path: 'materialWarehouse/early-warning',
    },
  ],
]);
