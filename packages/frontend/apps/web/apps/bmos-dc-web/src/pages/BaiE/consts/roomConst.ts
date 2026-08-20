interface Room {
  name: string;
  code: string;
  clearLevel: string;
  modelId: string;
}

export const rooms: Array<[string, Room]> = [
  [
    'SM_1楼_乙醇供应站1A12',
    {
      modelId: 'SM_1楼_乙醇供应站1A12',
      name: '乙醇供应站',
      code: '1A12',
      clearLevel: '',
    },
  ],
  [
    'SM_1楼_包材储存1A10',
    {
      modelId: 'SM_1楼_包材储存1A10',
      name: '包材储存',
      code: '1A10',
      clearLevel: '',
    },
  ],
  [
    'SM_1楼_原辅料储存1A11',
    {
      modelId: 'SM_1楼_原辅料储存1A11',
      name: '原辅料储存',
      code: '1A11',
      clearLevel: '',
    },
  ],
  [
    'SM_1楼_血浆储存1A13',
    {
      modelId: 'SM_1楼_血浆储存1A13',
      name: '血浆储存',
      code: '1A13',
      clearLevel: '',
    },
  ],
  [
    'SM_2楼_灯检2B03',
    {
      modelId: 'SM_2楼_灯检2B03',
      name: '灯检',
      code: '2B03',
      clearLevel: '',
    },
  ],
  [
    'SM_2楼_待包装品暂存2B04',
    {
      modelId: 'SM_2楼_待包装品暂存2B04',
      name: '待包装品暂存',
      code: '2B04',
      clearLevel: '',
    },
  ],
  [
    'SM_2楼_成品暂存2B07',
    {
      modelId: 'SM_2楼_成品暂存2B07',
      name: '成品暂存',
      code: '2B07',
      clearLevel: '',
    },
  ],
  [
    'SM_2楼_外包2B09',
    {
      modelId: 'SM_2楼_外包2B09',
      name: '外包',
      code: '2B09',
      clearLevel: '',
    },
  ],
  [
    'SM_2楼_制水间1A09',
    {
      modelId: 'SM_2楼_制水间1A09',
      name: '制水间',
      code: '1A09',
      clearLevel: '',
    },
  ],
  [
    'SM_3楼_培育间3A09',
    {
      modelId: 'SM_3楼_培育间3A09',
      name: '培育间',
      code: '3A09',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_3楼_清洗灭菌间3C10',
    {
      modelId: 'SM_3楼_清洗灭菌间3C10',
      name: '清洗灭菌间',
      code: '3C10',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_清洗灭菌间3B04',
    {
      modelId: 'SM_3楼_清洗灭菌间3B04',
      name: '清洗灭菌间',
      code: '3B04',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_洁具间3C09',
    {
      modelId: 'SM_3楼_洁具间3C09',
      name: '洁具间',
      code: '3C09',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_洁具间3B12',
    {
      modelId: 'SM_3楼_洁具间3B12',
      name: '洁具间',
      code: '3B12',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_消毒液配制间3C08',
    {
      modelId: 'SM_3楼_消毒液配制间3C08',
      name: '消毒液配制间',
      code: '3C08',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_消毒液接收间3D08',
    {
      modelId: 'SM_3楼_消毒液接收间3D08',
      name: '消毒液配制间',
      code: '3D08',
      clearLevel: 'B级',
    },
  ],
  [
    'SM_3楼_接收间3D11',
    {
      modelId: 'SM_3楼_接收间3D11',
      name: '接收间',
      code: '3D11',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_3楼_暂存间3C04',
    {
      modelId: 'SM_3楼_暂存间3C04',
      name: '暂存间',
      code: '3C04',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_灭后间3D07',
    {
      modelId: 'SM_3楼_灭后间3D07',
      name: '灭后间',
      code: '3D07',
      clearLevel: 'B级',
    },
  ],
  [
    'SM_3楼_中间检测间3B13',
    {
      modelId: 'SM_3楼_中间检测间3B13',
      name: '中间检测间',
      code: '3B13',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_称量间3B11',
    {
      modelId: 'SM_3楼_称量间3B11',
      name: '称量间',
      code: '3B11',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_洗烘间3C07',
    {
      modelId: 'SM_3楼_洗烘间3C07',
      name: '洗烘间',
      code: '3C07',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_静免超滤间3B10',
    {
      modelId: 'SM_3楼_静免超滤间3B10',
      name: '静免超滤间',
      code: '3B10',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_低ph灭活间3B09',
    {
      modelId: 'SM_3楼_低ph灭活间3B09',
      name: '低ph灭活间',
      code: '3B09',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_人白超滤灭活间3B07',
    {
      modelId: 'SM_3楼_人白超滤灭活间3B07',
      name: '人白超滤灭活间',
      code: '3B07',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_配液间3B08',
    {
      modelId: 'SM_3楼_配液间3B08',
      name: '配液间',
      code: '3B08',
      clearLevel: 'C级',
    },
  ],
  [
    'SM_3楼_灌装间3D09',
    {
      modelId: 'SM_3楼_灌装间3D09',
      name: '灌装间',
      code: '3D09',
      clearLevel: 'B级',
    },
  ],
  [
    'SM_3楼_轧盖间3D10',
    {
      modelId: 'SM_3楼_轧盖间3D10',
      name: '轧盖间',
      code: '3D10',
      clearLevel: 'B级',
    },
  ],
  [
    'SM_3楼_CIP间3A11',
    {
      modelId: 'SM_3楼_CIP间3A11',
      name: 'CIP间',
      code: '3A11',
      clearLevel: '',
    },
  ],
  [
    'SM_4楼_暂存间4C08',
    {
      modelId: 'SM_4楼_暂存间4C08',
      name: '暂存间',
      code: '4C08',
      clearLevel: '',
    },
  ],
  [
    'SM_4楼_废弃物灭后间4B11',
    {
      modelId: 'SM_4楼_废弃物灭后间4B11',
      name: '废弃物灭后间',
      code: '4B11',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_4楼_废弃物灭后间4C07',
    {
      modelId: 'SM_4楼_废弃物灭后间4C07',
      name: '废弃物灭后间',
      code: '4C07',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_4楼_废弃物灭活间4C06',
    {
      modelId: 'SM_4楼_废弃物灭活间4C06',
      name: '废弃物灭活间',
      code: '4C06',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_洁具间4C10',
    {
      modelId: 'SM_4楼_洁具间4C10',
      name: '洁具间',
      code: '4C10',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_洁具间4B08',
    {
      modelId: 'SM_4楼_洁具间4B08',
      name: '洁具间',
      code: '4B08',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_称量间4C09',
    {
      modelId: 'SM_4楼_称量间4C09',
      name: '称量间',
      code: '4C09',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_清洗间4C15',
    {
      modelId: 'SM_4楼_清洗间4C15',
      name: '清洗间',
      code: '4C15',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_沉淀库4C14',
    {
      modelId: 'SM_4楼_沉淀库4C14',
      name: '沉淀库',
      code: '4C14',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_4楼_配液检测间4C11',
    {
      modelId: 'SM_4楼_配液检测间4C11',
      name: '配液检测间',
      code: '4C11',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_清洗融浆间4B07',
    {
      modelId: 'SM_4楼_清洗融浆间4B07',
      name: '清洗融浆间（乙醇使用场所）',
      code: '4B07',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_反应分离间4C12',
    {
      modelId: 'SM_4楼_反应分离间4C12',
      name: '反应分离间（乙醇使用场所）',
      code: '4C12',
      clearLevel: 'D级',
    },
  ],
  [
    'SM_4楼_CIP间4C16',
    {
      modelId: 'SM_4楼_CIP间4C16',
      name: 'CIP间',
      code: '4C16',
      clearLevel: 'K级',
    },
  ],
  [
    'SM_5楼_乙醇预冷间',
    {
      modelId: 'SM_5楼_乙醇预冷间',
      name: '乙醇预冷间',
      code: '5A08',
      clearLevel: 'K级',
    },
  ],
];
export const roomMap = computed((): Map<string, Room> => {
  return new Map(rooms);
});
