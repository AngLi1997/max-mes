import { t } from '@bmos/i18n';

export const getDicts = () => {
  // 是否
  const yesOrNoDict = [
    { value: 1, label: t('是') },
    { value: 0, label: t('否') },
  ];

  // 仓库列表
  const warehouseDict = [
    { label: t('仓库'), value: 1 },
    { label: t('南环仓库'), value: 2 },
  ];

  // 同步方式
  const SYNC_TYPE = [
    { label: t('浆站系统同步'), value: 1 },
    { label: t('手动导入同步'), value: 2 },
  ];

  // 血浆外观
  const plasmaAppearanceDict = [
    { label: t('正常'), value: 1 },
    { label: t('破袋'), value: 2 },
    { label: t('溶血'), value: 3 },
    { label: t('脂肪浆'), value: 4 },
    { label: t('标签脱落'), value: 5 },
  ];

  // 出库类别
  const outTypeDict = [
    { label: t('投料'), value: 1 },
    { label: t('销毁'), value: 4 },
    { label: t('科研'), value: 2 },
    { label: t('调用'), value: 3 },
  ];

  // 质量状态(血浆状态)
  const qualityStatusDict = [
    { value: 1, label: t('检疫期合格') },
    { value: 2, label: t('检疫期不合格&超期') },
    { value: 3, label: t('检疫期未通过') },
  ];

  // 验收状态
  const acceptanceStatusDict = [
    { value: 0, label: t('待验收') },
    { value: 1, label: t('已验收') },
  ];

  // 标本类型
  const sampleTypeDict = [
    { value: 1, label: t('公司复检标本') },
    { value: 2, label: t('公司留存标本') },
    { value: 3, label: t('回访检测标本') },
    { value: 4, label: t('回访留存标本') },
    { value: 5, label: t('浆站留存标本') },
    { value: 6, label: t('集中化检测标本') },
  ];

  // 验收结果
  const acceptanceResultDict = [
    { value: 0, label: t('待验收') },
    { value: 1, label: t('验收正常') },
    { value: 2, label: t('验收温度异常') },
  ];

  // 审核结果
  const auditResultDict = [
    { value: 0, label: t('待审核') },
    { value: 1, label: t('审核通过') },
    { value: 2, label: t('审核退回') },
  ];

  // 接收状态
  const receiveStatusDict = [
    { value: 0, label: t('待接收') },
    { value: 1, label: t('已接收') },
    { value: 2, label: t('已撤销') },
    { value: 3, label: t('已终止') },
  ];

  // 血浆类型
  const plasmaTypeDict = [
    { value: 1, label: t('普通浆') },
    { value: 2, label: t('免疫浆') },
  ];

  // 外观检验
  const appearanceCheckDict = [
    { value: 0, label: t('未完成') },
    { value: 1, label: t('已完成') },
  ];

  // 库存状态
  const inventoryStatusDict = [
    { value: 0, label: t('待入库') },
    { value: 1, label: t('入库中') },
    { value: 2, label: t('在库') },
    { value: 3, label: t('出库中') },
    { value: 4, label: t('出库') },
  ];

  // 标本库存状态
  const sampleInventoryStatusDict = [
    { value: 0, label: t('在库') },
    { value: 1, label: t('已销毁出库') },
    { value: 2, label: t('已检测出库') },
  ];

  // 维护状态
  const maintainStatusDict = [
    { value: 1, label: t('正常') },
    { value: 2, label: t('异常') },
    { value: 3, label: t('超期') },
  ];

  // 合格不合格
  const qualifiedStatusDict = [
    { value: 1, label: t('合格') },
    { value: 0, label: t('不合格') },
  ];

  // 血浆验收状态
  const plasmaAcceptanceStatusDict = [
    { value: 0, label: t('待验收') },
    { value: 1, label: t('验收合格') },
    { value: 2, label: t('验收不合格') },
  ];

  // 核对方式
  const checkTypeDict = [
    { value: 1, label: t('手动核对') },
    { value: 2, label: t('预融核对') },
  ];

  // 请验状态
  const pleaseVerifyStatusDict = [
    { value: 0, label: t('未请验') },
    { value: 1, label: t('已请验') },
  ];

  // 送检状态
  const censorshipStatusDict = [
    { value: 0, label: t('待同步出库') },
    { value: 1, label: t('已同步待出库') },
    { value: 2, label: t('已出库未同步') },
    { value: 3, label: t('待接收') },
    { value: 4, label: t('已接收') },
  ];

  // 对应类型
  const correspondingTypeDict = [
    { value: 1, label: t('检疫期符合') },
    { value: 2, label: t('回访符合') },
    { value: 3, label: t('无对应关系') },
    { value: 4, label: t('回访淘汰') },
    { value: 5, label: t('浆站淘汰') },
    { value: 6, label: t('检验不合格') },
    { value: 7, label: t('受检验影响不合格') },
    { value: 8, label: t('超一年无对应关系') },
    { value: 9, label: t('超过法定存储年限') },
  ];

  // 报告类型
  const reportTypeDict = [
    { value: 0, label: t('待创建') },
    { value: 1, label: t('已创建') },
    { value: 2, label: t('已提交') },
  ];

  // 检验次数
  const checkNumDict = [
    { value: 0, label: t('初检') },
    { value: 1, label: t('复检') },
  ];

  // 分拣状态
  const sortingStatusDict = [
    { value: 0, label: t('待分拣') },
    { value: 1, label: t('已分拣') },
  ];

  // 检疫期审核状态
  const quarantineAuditStatusDict = [
    { value: 3, label: t('审核通过') },
    { value: 4, label: t('审核退回') },
  ];

  // 检测类型
  const inspectTypeDict = [
    { value: 1, label: t('血样检测') },
    { value: 2, label: t('浆样检测') },
  ];

  // 不合格项目
  const unqualifiedProjectDict = [
    { value: 1, label: t('外观不合格') },
    { value: 2, label: t('蛋白含量不合格') },
    { value: 3, label: t('ALT不合格') },
    { value: 4, label: t('HBsAg阳性') },
    { value: 5, label: t('梅毒阳性') },
    { value: 6, label: t('HIV1和HIV2抗体阳性') },
    { value: 7, label: t('HCV抗体阳性') },
    { value: 8, label: t('HBV DNA阳性') },
    { value: 9, label: t('HCV RNA阳性') },
    { value: 10, label: t('HIV RNA阳性') },
    { value: 11, label: t('超一年无对应关系') },
    { value: 12, label: t('运输温度不合格') },
    { value: 13, label: t('超过血浆存储年限') },
  ];

  // 核酸检测
  const nucleicAcidFlagDict = [
    { value: 0, label: t('未检测') },
    { value: 1, label: t('已检测') },
  ];

  // 报告审核状态
  const reportAuditStatusDict = [
    { value: 1, label: t('待送审') },
    { value: 2, label: t('待审核') },
    { value: 3, label: t('审核通过') },
    { value: 4, label: t('审核退回') },
  ];

  // 处理状态
  const handleStatusDict = [
    { value: 1, label: t('待处理') },
    { value: 2, label: t('已处理') },
  ];

  // 企业检测状态
  const detectionStatusDict = [
    { value: 1, label: t('待检测') },
    { value: 2, label: t('已检测') },
  ];

  // 反馈状态
  const feedbackStatusDict = [
    { value: 0, label: t('待反馈') },
    { value: 1, label: t('已反馈') },
  ];

  // 血浆或标本
  const plasmaOrSampleDict = [
    { value: 1, label: t('血浆') },
    { value: 2, label: t('标本') },
  ];

  return {
    yesOrNoDict,
    warehouseDict,
    SYNC_TYPE,
    plasmaAppearanceDict,
    outTypeDict,
    qualityStatusDict,
    checkNumDict,
    sortingStatusDict,
    quarantineAuditStatusDict,
    inspectTypeDict,
    unqualifiedProjectDict,
    nucleicAcidFlagDict,
    reportAuditStatusDict,
    handleStatusDict,
    detectionStatusDict,
    feedbackStatusDict,
    plasmaOrSampleDict,
    acceptanceStatusDict,
    sampleTypeDict,
    acceptanceResultDict,
    auditResultDict,
    receiveStatusDict,
    plasmaTypeDict,
    appearanceCheckDict,
    inventoryStatusDict,
    sampleInventoryStatusDict,
    maintainStatusDict,
    qualifiedStatusDict,
    plasmaAcceptanceStatusDict,
    checkTypeDict,
    pleaseVerifyStatusDict,
    censorshipStatusDict,
    correspondingTypeDict,
    reportTypeDict,
  };
};
