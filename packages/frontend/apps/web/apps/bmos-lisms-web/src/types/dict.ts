import { t } from '@bmos/i18n';
import { InspectionStatusEnum, PassResultEnum, ProjectTypeEnum, SpotCheckStatusEnum } from './enum';

export const getDicts = () => {
  // 检验进程
  const inspectionProcessDict = [
    { value: 'SAMPLE_RECEIVING', label: t('标本待接收') },
    { value: 'INSPECTION_PENDING', label: t('检验待执行') },
    { value: 'INSPECTION_PENDED', label: t('检验执行中') },
    { value: 'DATA_ISSUING', label: t('数据待签发') },
    { value: 'REPORT_ISSUING', label: t('报告待签发') },
    { value: 'REPORT_ISSUED', label: t('报告已签发') },
  ];

  // 是否
  const yesOrNoDict = [
    { value: 'TRUE', label: t('是') },
    { value: 'FALSE', label: t('否') },
  ];
  // 是否
  const yesOrNoDictOther = [
    { value: 1, label: t('是') },
    { value: 0, label: t('否') },
  ];

  // 标本分类
  const sampleCategoryDict = [
    { value: 'PLASMA_SPECIMEN ', label: t('血浆标本') },
    { value: 'SERUM_SPECIMEN ', label: t('血清标本') },
  ];

  // 标本类型
  const sampleTypeDict = [
    { value: 'ORG_TEST_SAMPLE', label: t('集中化检测标本') },
    { value: 'COMPANY_TEST_SAMPLE', label: t('公司复检标本') },
    { value: 'FOLLOW_UP_TEST_SAMPLE', label: t('回访检测标本') },
  ];

  // 标本接收状态
  const receiveStatusDict = [
    { value: 'RECEIVING', label: t('待接收') },
    { value: 'RECEIVED', label: t('已接收') },
    { value: 'REJECTING', label: t('拒收待审核') },
    { value: 'REJECTED', label: t('已拒收') },
    { value: 'RECEIVE_AUDITING', label: t('接收待审核') },
  ];

  // 审核状态
  const auditStatusDict = [
    { value: 'TO_AUDIT', label: t('待审核') },
    { value: 'AUDITED', label: t('已审核') },
  ];

  // 审核结果
  const auditResultDict = [
    { value: 'APPROVE', label: t('通过') },
    { value: 'REJECT', label: t('退回') },
  ];

  // 运输状态
  const transportStatusDict = [
    { value: 'FREEZING', label: t('冷冻(≤-10℃)') },
    { value: 'REFRIGERATE', label: t('冷藏(2-8℃)') },
  ];

  // 检品状态
  const testArticleStatusDict = [
    { value: 'NORMAL', label: t('正常') },
    { value: 'REJECT', label: t('拒收') },
    { value: 'OVERTIME', label: t('超期') },
  ];

  // 物料类型
  const materialTypeDict = [
    { value: 'CORE_MATERIAL', label: t('关键物料') },
    { value: 'NORMAL_MATERIAL', label: t('普通物料') },
  ];

  // 关键物料品类
  const keyMaterialCategoryDict = [
    { value: 'REAGENT', label: t('试剂') },
    { value: 'QUALITY_CONTROL', label: t('质控品') },
    { value: 'OTHER', label: t('其他') },
  ];

  // 物料接收结果
  const materialReceiveResultDict = [
    { value: 'PASS', label: t('验收合格') },
    { value: 'NO_PASS', label: t('验收不合格') },
  ];

  // 物料仓库区域
  const materialWarehouseAreaDict = [
    { value: 'PASS', label: t('合格区') },
    { value: 'NOPASS', label: t('不合格区') },
    { value: 'WAITING', label: t('待检区') },
  ];

  // 签发状态
  const signStatusDict = [
    { value: 'WAIT_PUBLISH', label: t('待签发') },
    { value: 'PUBLISHED', label: t('已签发') },
  ];

  // 签发结果
  const signResultDict = [
    { value: 'RESULT_PASS', label: t('通过') },
    { value: 'RESULT_BACK', label: t('退回') },
  ];

  // 出库状态
  const outStatusDict = [
    { value: 'WAITING_DELIVERY', label: t('待出库') },
    { value: 'DELIVERY', label: t('已出库') },
  ];

  // 使用类别
  const useCategoryDict = [
    { value: 'SCRAP', label: t('报废') },
    { value: 'RETURN', label: t('退货') },
    // { value: 'EXPEND', label: t('消耗') },
    { value: 'RECEIVE', label: t('领用') },
    { value: 'SPOT', label: t('抽检') },
  ];

  // 抽检状态
  const spotStatusDict = [
    { value: SpotCheckStatusEnum.WAIT_SUBMIT, label: t('待提交') },
    { value: SpotCheckStatusEnum.SUBMITED, label: t('已提交') },
  ];

  // 检验状态
  const inspectionStatusDict = [
    { value: InspectionStatusEnum.TO_INSPECT, label: t('待检验'), icon: 'ToBeInspected', color: '#F69936' },
    { value: InspectionStatusEnum.INSPECTING, label: t('检验中'), icon: 'Inspecting', color: '#59BF78' },
    { value: InspectionStatusEnum.PUBLISHED, label: t('已完成'), icon: 'Inspected', color: '#2D70FF' },
  ];

  // 放行结果
  const passResultDict = [
    { value: PassResultEnum.PASS, label: t('准予放行') },
    { value: PassResultEnum.NO_PASS, label: t('不予放行') },
  ];

  // 检验项目
  const InspectionProjectDict = [
    { value: 'IP001', label: t('蛋白质含量'), title: t('蛋白质含量检验信息'), routeName: 'Protein' },
    { value: 'IP002', label: 'ALT', title: t('转氨酶检验信息'), routeName: 'ALT' },
    { value: 'IP003', label: 'HBsAg', title: t('ELISA乙肝检验信息'), routeName: 'HBsAg' },
    { value: 'IP004', label: t('抗-HCV'), title: t('ELISA丙肝检验信息'), routeName: 'HCV' },
    { value: 'IP005', label: 'HIV-Ag/Ab', title: t('ELISA艾滋检验信息'), routeName: 'HIV' },
    { value: 'IP006', label: t('抗-TP'), title: t('ELISA梅毒检验信息'), routeName: 'TP' },
    { value: 'IP010', label: t('蛋白电泳'), title: t('蛋白电泳检验信息'), routeName: 'ProteinElectrophoresis' },
  ];

  // 检验次数：INITIAL_INSPECT初检/RE_INSPECT复检
  const InspectionCountDict = [
    { value: 'INITIAL_INSPECT', label: t('初检') },
    { value: 'RE_INSPECT', label: t('复检') },
  ];

  // 发布状态:TO_PUBLISH待发布/TO_AUDIT待审核/PUBLISHED已发布/TO_INSPECT待检验(非待发布和已发布的数据)
  const PublishStatusDict = [
    { value: 'TO_INSPECT', label: t('待检验') },
    { value: 'TO_PUBLISH', label: t('待发布') },
    { value: 'TO_AUDIT', label: t('待审核') },
    { value: 'PUBLISHED', label: t('审核通过') },
  ];
  // 检验结果:QUALIFIED合格/UNQUALIFIED不合格
  const InspectionResultDict = [
    { value: 'QUALIFIED', label: t('合格') },
    { value: 'UNQUALIFIED', label: t('不合格') },
  ];
  // 检验结果:QUALIFIED 阴性/UNQUALIFIED阳性
  const InspectionResultDictOther = [
    { value: 'QUALIFIED', label: t('阴性') },
    { value: 'UNQUALIFIED', label: t('阳性') },
  ];
  // 检验任务状态:TO_UPLOAD待上传数据/TO_CHECK待复核/CHECKED已复核
  const InspectionTaskStatusDict = [
    { value: 'TO_UPLOAD', label: t('待检验') },
    { value: 'TO_CHECK', label: t('待发布') },
    { value: 'CHECKED', label: t('已发布') },
  ];

  // 是否在控 Y -》是；N-》否
  const controlStatusDict = [
    { value: 'Y', label: t('是') },
    { value: 'N', label: t('否') },
  ];
  // 页面枚举
  const pageDict = [
    { value: 'SAMPLE_RECEIVE', label: t('接收审核') },
    { value: 'SAMPLE_REJECT', label: t('拒收审核') },
    { value: 'INSPECT_DATA_PUBLISH', label: t('检验数据审核') },
    { value: 'INSPECT_REPORT', label: t('检验报告审核') },
    { value: 'SECOND_MATERIAL_CONSUME', label: t('领用库消耗审核') },
    { value: 'SECOND_MATERIAL_SCRAP', label: t('领用库报废审核') },
    { value: 'MATERIAL_SPOT_CHECK', label: t('抽检申请审核') },
    { value: 'MATERIAL_USE', label: t('物料领用审核') },
    { value: 'MATERIAL_SCRAP', label: t('物料报废审核') },
    { value: 'MATERIAL_RETURN', label: t('物料退货审核') },
    { value: 'DOC_TEMPLATE', label: t('报告模板审核') },
  ];

  /**
   * ------------------------------------------
   */

  // 项目类型
  const projectTypeDict = [
    { value: ProjectTypeEnum.FIXED_TIEM, label: t('固定项目') },
    { value: ProjectTypeEnum.SPECIAL_TIEM, label: t('特殊项目') },
  ];

  // 操作符 < ≤ = > ≥ ≠
  const operatorDict = [
    { value: '<', label: '<' },
    { value: '≤', label: '≤' },
    { value: '=', label: '=' },
    { value: '>', label: '>' },
    { value: '≥', label: '≥' },
    { value: '≠', label: '≠' },
  ];

  // THE_UNIT(0, "个位"),
  // ONE_UNIT(1, "1位小数"),
  // TWO_UNIT(2, "2位小数"),
  // THREE_UNIT(3, "3位小数");
  // 保留位数
  const decimalUnitDict = [
    { value: 0, label: t('个位') },
    { value: 1, label: t('1位小数') },
    { value: 2, label: t('2位小数') },
    { value: 3, label: t('3位小数') },
  ];

  // 批准状态
  const approveStatusDict = [
    { value: 'TO_APPROVE', label: t('待批准') },
    { value: 'APPROVED', label: t('已批准') },
  ];

  // 检查状态
  const checkStatusDict = [
    { value: 'WAIT_CHECK', label: t('待检查') },
    { value: 'CHECKED', label: t('已检查') },
  ];

  return {
    inspectionProcessDict,
    yesOrNoDict,
    yesOrNoDictOther,
    sampleCategoryDict,
    sampleTypeDict,
    auditStatusDict,
    auditResultDict,
    transportStatusDict,
    testArticleStatusDict,
    keyMaterialCategoryDict,
    materialReceiveResultDict,
    materialWarehouseAreaDict,
    signStatusDict,
    signResultDict,
    outStatusDict,
    inspectionStatusDict,
    InspectionProjectDict,
    InspectionResultDict,
    InspectionCountDict,
    PublishStatusDict,
    passResultDict,
    spotStatusDict,
    useCategoryDict,
    materialTypeDict,
    receiveStatusDict,
    controlStatusDict,
    InspectionResultDictOther,
    InspectionTaskStatusDict,
    pageDict,
    projectTypeDict,
    operatorDict,
    decimalUnitDict,
    approveStatusDict,
    checkStatusDict,
  };
};

// 转map
export const dictToMap = (dict: any[]) => {
  return (
    dict.reduce((acc, cur) => {
      acc[cur.value] = cur.label;
      return acc;
    }, {}) ?? {}
  );
};

// 根据value获取Dict
export const getDictByValue = (dict: any[], value: any) => dict.find((item: any) => item.value === value) ?? {};

// 根据label获取Dict
export const getDictByLabel = (dict: any[], label: any) => dict.find((item: any) => item.label === label) ?? {};
