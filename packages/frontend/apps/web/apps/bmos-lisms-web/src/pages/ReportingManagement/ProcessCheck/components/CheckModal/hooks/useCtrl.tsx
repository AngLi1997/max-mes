enum CheckType {
  TYPE = 'type',
  POINT = 'point',
  CHECKED = 'checked',
  FIELD = 'field',
}

export const useCtrl = () => {
  const beforeList = reactive([
    {
      [CheckType.TYPE]: t('检测试剂'),
      rowSpan: 4,
      [CheckType.POINT]: t('经确认合格，质关科放行使用'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'reagentQualified',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('在有效期内，存放温度合格'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'reagentEffective',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('室温平衡30分钟'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'reagentBalance',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('冻干质控品融化充分后室温平衡30分钟'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'reagentQuality',
    },
    {
      [CheckType.TYPE]: t('待检标本'),
      rowSpan: 1,
      [CheckType.POINT]: t('标本送检记录填写完整，标本数量、标识等符合质量标准'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'sampleInspect',
    },
    {
      [CheckType.TYPE]: t('仪器设备'),
      rowSpan: 4,
      [CheckType.POINT]: t('经校验合格且在有效期内'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'deviceQualified',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('仪器状态完好'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'deviceStatus',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('开机预热'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'devicePreheat',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('温育设备内有经校验合格且在有效期内的温度计，显示温度合格'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'deviceTemperature',
    },
    {
      [CheckType.TYPE]: t('检验人员'),
      rowSpan: 1,
      [CheckType.POINT]: t('经上岗培训合格'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'trainQualified',
    },
  ]);

  const inProgressList = reactive([
    {
      [CheckType.TYPE]: t('SOP'),
      rowSpan: 1,
      [CheckType.POINT]: t('严格按照SOP操作'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'sop',
    },
    {
      [CheckType.TYPE]: t('洗液'),
      rowSpan: 1,
      [CheckType.POINT]: t('使用新鲜纯化水配制'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'lotion',
    },
    {
      [CheckType.TYPE]: t('加样'),
      rowSpan: 1,
      [CheckType.POINT]: t('微量移液器或全自动加样系统经校验合格且在有效期内'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'sample',
    },
    {
      [CheckType.TYPE]: t('洗板'),
      rowSpan: 1,
      [CheckType.POINT]: t('洗板次数、洗液量、洗板间隔均符合规定'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'washboard',
    },
    {
      [CheckType.TYPE]: t('温育'),
      rowSpan: 1,
      [CheckType.POINT]: t('温育温度或孵育塔温度合格'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'incubate',
    },
    {
      [CheckType.TYPE]: t('读值'),
      rowSpan: 1,
      [CheckType.POINT]: t('酶标仪均经校验合格且在有效期内'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'read',
    },
    {
      [CheckType.TYPE]: t('环境'),
      rowSpan: 1,
      [CheckType.POINT]: t('温湿度均符合规定'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'environment',
    },
  ]);

  const afterList = reactive([
    {
      [CheckType.TYPE]: t('有效性'),
      rowSpan: 2,
      [CheckType.POINT]: t('空白对照、阴阳性对照符合规定'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'effectiveControl',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('质控品在本批质控限范围内'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'effectiveQuality',
    },
    {
      [CheckType.TYPE]: t('检测结果'),
      rowSpan: 2,
      [CheckType.POINT]: t('实验有效'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'inspectEffective',
    },
    {
      rowSpan: 0,
      [CheckType.POINT]: t('有检验不合格（按反应性标本的复检和结果判定制度操作）'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'inspectReactivity',
    },
    {
      [CheckType.TYPE]: t('检验记录'),
      rowSpan: 1,
      [CheckType.POINT]: t('检验项目符合规定，检验人员、复核人员签名完全'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'inspectRecord',
    },
    {
      [CheckType.TYPE]: t('废弃标本'),
      rowSpan: 1,
      [CheckType.POINT]: t('检测完毕的标本经批准后高压消毒处理'),
      [CheckType.CHECKED]: 1,
      [CheckType.FIELD]: 'abandonSample',
    },
  ]);

  const initList = () => {
    for (let i = 0; i < beforeList.length; i++) {
      beforeList[i][CheckType.CHECKED] = 1;
    }
    for (let i = 0; i < inProgressList.length; i++) {
      inProgressList[i][CheckType.CHECKED] = 1;
    }
    for (let i = 0; i < afterList.length; i++) {
      afterList[i][CheckType.CHECKED] = 1;
    }
  };

  const handleChecked = () => {
    const checkRecordDetail = {} as any;
    for (let i = 0; i < beforeList.length; i++) {
      checkRecordDetail[beforeList[i][CheckType.FIELD]] = beforeList[i][CheckType.CHECKED];
    }
    for (let i = 0; i < inProgressList.length; i++) {
      checkRecordDetail[inProgressList[i][CheckType.FIELD]] = inProgressList[i][CheckType.CHECKED];
    }
    for (let i = 0; i < afterList.length; i++) {
      checkRecordDetail[afterList[i][CheckType.FIELD]] = afterList[i][CheckType.CHECKED];
    }
    return checkRecordDetail;
  };

  return {
    beforeList,
    inProgressList,
    afterList,
    initList,
    handleChecked,
  };
};
