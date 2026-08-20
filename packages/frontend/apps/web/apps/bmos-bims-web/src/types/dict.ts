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
    { value: 1, label: t('待接收') },
    { value: 2, label: t('已接收') },
    // { value: 2, label: t('已撤销') },
    // { value: 3, label: t('已终止') },
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

  // 发布状态
  const publishStatusDict = [
    { value: 1, label: t('待发布') },
    { value: 2, label: t('已发布') },
  ];

  // 检验次数
  const checkNumDict = [
    { value: 0, label: t('初检') },
    { value: 1, label: t('复检') },
  ];

  // 供应商类型
  const supplierTypeDict = [
    { value: 1, label: t('生产商') },
    { value: 2, label: t('经销商') },
  ];

  // 单位
  const unitDict = [
    { value: 1, label: t('盒') },
    { value: 2, label: t('支') },
  ];

  // 物料分类
  const materialTypeDict = [
    { value: 1, label: t('蛋白质含量试剂') },
    { value: 2, label: t('蛋白质含量质控品') },
    { value: 3, label: t('ALT试剂') },
    { value: 4, label: t('ALT质控品') },
    { value: 5, label: t('HBsAg试剂') },
    { value: 6, label: t('HBsAg质控品') },
    { value: 7, label: t('抗-HCV试剂') },
    { value: 8, label: t('抗-HCV质控品') },
    { value: 9, label: t('抗-HIV试剂') },
    { value: 10, label: t('抗-HIV质控品') },
    { value: 11, label: t('抗-TP试剂') },
    { value: 12, label: t('抗-TP质控品') },
    { value: 13, label: t('PCR试剂') },
    { value: 14, label: t('HBV DNA质控品') },
    { value: 15, label: t('HCV RNA质控品') },
    { value: 16, label: t('HIV RNA质控品') },
    { value: 17, label: t('甲肝效价试剂') },
    { value: 18, label: t('甲肝效价质控品') },
    { value: 19, label: t('乙肝效价试剂') },
    { value: 20, label: t('乙肝效价质控品') },
    { value: 21, label: t('狂犬效价试剂') },
    { value: 22, label: t('狂犬效价质控品') },
    { value: 23, label: t('破伤风效价试剂') },
    { value: 24, label: t('破伤风效价质控品') },
  ];

  // 效价免疫类型选择
  const effectPriceImmuTypeDict = [
    { value: 1, label: t('甲肝') },
    { value: 2, label: t('乙肝') },
    { value: 3, label: t('狂犬') },
    { value: 4, label: t('破伤风') },
  ];

  // 报告状态
  const reportStatusDict = [
    { value: 0, label: t('待生成') },
    { value: 1, label: t('待审核') },
    { value: 2, label: t('已签发') },
  ];

  // pcr结果检验类型
  const pcrResultCheckTypeDict = [
    { value: 1, label: t('HBV') },
    { value: 2, label: t('HCV') },
    { value: 3, label: t('HIV') },
  ];

  // 检验结果发布状态
  const inspectionResultReleaseStatusDict = [
    { value: 1, label: t('待发布') },
    { value: 2, label: t('已发布') },
    { value: 3, label: t('待审核') },
  ];

  return {
    yesOrNoDict,
    warehouseDict,
    SYNC_TYPE,
    plasmaAppearanceDict,
    outTypeDict,
    qualityStatusDict,
    acceptanceStatusDict,
    sampleTypeDict,
    acceptanceResultDict,
    auditResultDict,
    receiveStatusDict,
    plasmaTypeDict,
    appearanceCheckDict,
    inventoryStatusDict,
    maintainStatusDict,
    qualifiedStatusDict,
    plasmaAcceptanceStatusDict,
    checkTypeDict,
    pleaseVerifyStatusDict,
    censorshipStatusDict,
    correspondingTypeDict,
    reportTypeDict,
    publishStatusDict,
    checkNumDict,
    supplierTypeDict,
    unitDict,
    materialTypeDict,
    effectPriceImmuTypeDict,
    reportStatusDict,
    pcrResultCheckTypeDict,
    inspectionResultReleaseStatusDict,
  };
};
