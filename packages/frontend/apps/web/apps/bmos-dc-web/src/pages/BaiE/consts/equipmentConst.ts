// 数采点位
interface SamplingPoint {
  name: string;
  point: string;
}

interface Equipment {
  modelId: string;
  name: string;
  code: string;
  img: string;
  imgType?: string;
  //生产厂商
  manufacturer: string;
  // 工作容积
  workingVolume: string;
  // 工艺
  process: string[];
  // 数采点位
  samplingPoint: SamplingPoint[];
}

export const equipments: Array<[string, Equipment]> = [
  [
    'SM_1楼设备_95%乙醇储存罐（V0701_',
    {
      modelId: 'SM_1楼设备_95%乙醇储存罐（V0701_',
      name: '95%乙醇储存罐',
      code: 'V0701',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '8000L',
      process: ['-'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0701',
        },
      ],
    },
  ],
  [
    'SM_1楼设备_95%乙醇储存罐（V0702_',
    {
      modelId: 'SM_1楼设备_95%乙醇储存罐（V0702_',
      name: '95%乙醇储存罐',
      code: 'V0702',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '8000L',
      process: ['-'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0702',
        },
      ],
    },
  ],
  [
    'SM_2楼设备09_贴标机',
    {
      modelId: 'SM_2楼设备09_贴标机',
      name: '贴标机',
      code: '',
      img: '全自动贴标机',
      manufacturer: '永创智能',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备04_分配模块',
    {
      modelId: 'SM_2楼设备04_分配模块',
      name: '分配模块',
      code: '',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备10_多功能全自动装盒机',
    {
      modelId: 'SM_2楼设备10_多功能全自动装盒机',
      name: '多功能全自动装盒机',
      code: '',
      img: '多功能全自动装盒机',
      manufacturer: '永创智能',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备11_半自动捆扎机',
    {
      modelId: 'SM_2楼设备11_半自动捆扎机',
      name: '半自动捆扎机',
      code: '',
      img: '半自动捆扎机',
      manufacturer: '永创智能',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备01_纯水储罐',
    {
      modelId: 'SM_2楼设备01_纯水储罐',
      name: '纯水储罐',
      code: '',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备08_纯蒸汽发生器',
    {
      modelId: 'SM_2楼设备08_纯蒸汽发生器',
      name: '纯蒸汽发生器',
      code: '',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备07_多效蒸馏水机',
    {
      modelId: 'SM_2楼设备07_多效蒸馏水机',
      name: '多效蒸馏水机',
      code: '',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备05_注射水储罐',
    {
      modelId: 'SM_2楼设备05_注射水储罐',
      name: '注射水储罐',
      code: '',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备06_注射水储罐',
    {
      modelId: 'SM_2楼设备06_注射水储罐',
      name: '注射水储罐',
      code: '',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备03_纯化水预处理单元',
    {
      modelId: 'SM_2楼设备03_纯化水预处理单元',
      name: '纯化水预处理单元',
      code: '',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_2楼设备02_RO+EDI备单元',
    {
      modelId: 'SM_2楼设备02_RO+EDI备单元',
      name: 'RO+EDI备单元',
      code: '',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备01_脉动真空灭菌柜',
    {
      modelId: 'SM_3楼设备01_脉动真空灭菌柜',
      name: '脉动真空灭菌柜',
      code: '',
      img: '脉动真空灭菌器',
      manufacturer: '新华医疗',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备14_脉动真空灭菌柜',
    {
      modelId: 'SM_3楼设备14_脉动真空灭菌柜',
      name: '脉动真空灭菌柜',
      code: '',
      img: '脉动真空灭菌器',
      manufacturer: '新华医疗',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备03_洗瓶机__M3C04_',
    {
      modelId: 'SM_3楼设备03_洗瓶机__M3C04_',
      name: '洗瓶机',
      code: 'M3C04',
      img: '洗瓶机',
      manufacturer: '东富龙',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备04_灭菌隧道___M3C03_',
    {
      modelId: 'SM_3楼设备04_灭菌隧道___M3C03_',
      name: '灭菌隧道',
      code: 'M3C03',
      img: '灭菌隧道',
      manufacturer: '东富龙',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备02_移动CIP-6清洗碱罐（R0601_',
    {
      modelId: 'SM_3楼设备02_移动CIP-6清洗碱罐（R0601_',
      name: '移动CIP-6清洗碱罐',
      code: 'R0601',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '30L',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备_静免超滤罐（R0501）',
    {
      modelId: 'SM_3楼设备_静免超滤罐（R0501）',
      name: '静免超滤罐',
      code: 'R0501',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '300L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0502',
        },
        {
          name: '罐称重',
          point: 'WICR0502',
        },
        {
          name: 'BusValve',
          point: 'Q0524',
        },
        {
          name: '压力',
          point: 'PICR0505',
        },
        {
          name: '搅拌',
          point: 'M0504',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_麦芽糖配制罐（R0502）',
    {
      modelId: 'SM_3楼设备_麦芽糖配制罐（R0502）',
      name: '麦芽糖配制罐',
      code: 'R0502',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '200L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0501',
        },
        {
          name: '罐称重',
          point: 'WICR0501',
        },
        {
          name: 'BusValve',
          point: 'Q0504',
        },
        {
          name: '压力',
          point: 'PICR0501',
        },
        {
          name: '搅拌',
          point: 'M0503',
        },
      ],
    },
  ],
  [
    'SM_3楼设备10_超滤模块（X0506）',
    {
      modelId: 'SM_3楼设备10_超滤模块（X0506）',
      name: '超滤模块',
      code: 'X0506',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '压力',
          point: 'PICR0501',
        },
        {
          name: '压力',
          point: 'PICR0503',
        },
        {
          name: '电导',
          point: 'CICR0503',
        },
        {
          name: '压力',
          point: 'PICR0502',
        },
        {
          name: '压力',
          point: 'PICR0504',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_低ph灭活罐1_（R0503）',
    {
      modelId: 'SM_3楼设备_低ph灭活罐1_（R0503）',
      name: '低ph灭活罐1#',
      code: 'R0503',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '300L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0504',
        },
        {
          name: '温度',
          point: 'TICR0505',
        },
        {
          name: '罐称重',
          point: 'WICR0503',
        },
        {
          name: 'BusValve',
          point: 'Q0538',
        },
        {
          name: '压力',
          point: 'PICR0506',
        },
        {
          name: '搅拌',
          point: 'M0506',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_低ph灭活罐2_（R0504）',
    {
      modelId: 'SM_3楼设备_低ph灭活罐2_（R0504）',
      name: '低ph灭活罐2#',
      code: 'R0504',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '300L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0508',
        },
        {
          name: '温度',
          point: 'TICR0509',
        },
        {
          name: '罐称重',
          point: 'WICR0504',
        },
        {
          name: 'BusValve1_9',
          point: 'Q0567',
        },
        {
          name: '压力',
          point: 'PICR0507',
        },
        {
          name: '搅拌',
          point: 'M0510',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_低ph灭活罐3_（R0505）',
    {
      modelId: 'SM_3楼设备_低ph灭活罐3_（R0505）',
      name: '低ph灭活罐3#',
      code: 'R0505',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '300L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0512',
        },
        {
          name: '温度',
          point: 'TICR0513',
        },
        {
          name: '罐称重',
          point: 'WICR0505',
        },
        {
          name: 'BusValve1_7',
          point: 'Q0585',
        },
        {
          name: '压力',
          point: 'PICR0508',
        },
        {
          name: '搅拌',
          point: 'M0514',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_超滤罐（R0401）',
    {
      modelId: 'SM_3楼设备_超滤罐（R0401）',
      name: '超滤罐',
      code: 'R0401',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['人血白蛋白'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0404',
        },
        {
          name: '罐称重',
          point: 'WICR0404',
        },
        {
          name: 'BusValve1_45',
          point: 'Q0446',
        },
        {
          name: '压力',
          point: 'PICR0407',
        },
        {
          name: '搅拌',
          point: 'M0408',
        },
      ],
    },
  ],
  [
    'SM_3楼设备12_超滤模块（X0416）',
    {
      modelId: 'SM_3楼设备12_超滤模块（X0416）',
      name: '超滤模块',
      code: 'X0416',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['人血白蛋白'],
      samplingPoint: [
        {
          name: '压力',
          point: 'PICR0401',
        },
        {
          name: '压力',
          point: 'PICR0405',
        },
        {
          name: '电导',
          point: 'CICR0403',
        },
        {
          name: '压力',
          point: 'PICR0402',
        },
        {
          name: '压力',
          point: 'PICR0406',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_巴氏灭活罐（R0402）',
    {
      modelId: 'SM_3楼设备_巴氏灭活罐（R0402）',
      name: '巴氏灭活罐',
      code: 'R0402',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '400L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0405',
        },
        {
          name: '温度',
          point: 'TICR0406',
        },
        {
          name: '罐称重',
          point: 'WICR0405',
        },
        {
          name: 'BusValve1_45',
          point: 'Q0457',
        },
        {
          name: '压力',
          point: 'PICR0408',
        },
        {
          name: '搅拌',
          point: 'M0410',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_0_9%Nacl罐（R0403）',
    {
      modelId: 'SM_3楼设备_0_9%Nacl罐（R0403）',
      name: '0.9%Nacl罐',
      code: 'R0403',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '4500L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0401',
        },
        {
          name: '罐称重',
          point: 'WICR0401',
        },
        {
          name: 'BusValve1_3',
          point: 'Q0404',
        },
        {
          name: '压力',
          point: 'PICR0401',
        },
        {
          name: '搅拌',
          point: 'M0402',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_0_5%Nacl_WFI罐（R0404）',
    {
      modelId: 'SM_3楼设备_0_5%Nacl_WFI罐（R0404）',
      name: '0.5%Nacl/WFI罐',
      code: 'R0404',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '2500L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0402',
        },
        {
          name: '罐称重',
          point: 'WICR0402',
        },
        {
          name: 'BusValve1_14',
          point: 'Q0415',
        },
        {
          name: '压力',
          point: 'PICR0402',
        },
        {
          name: '搅拌',
          point: 'M0404',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_超滤清洗液罐（R0405）',
    {
      modelId: 'SM_3楼设备_超滤清洗液罐（R0405）',
      name: '超滤清洗液罐',
      code: 'R0405',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '罐称重',
          point: 'WICR0403',
        },
        {
          name: 'BusValve1_24',
          point: 'Q0425',
        },
        {
          name: '压力',
          point: 'PICR0403',
        },
        {
          name: '搅拌',
          point: 'M0406',
        },
      ],
    },
  ],
  [
    'SM_3楼设备05_灌装加塞机_M3D05_',
    {
      modelId: 'SM_3楼设备05_灌装加塞机_M3D05_',
      name: '灌装加塞机',
      code: 'M3D05',
      img: '灌装加塞机',
      imgType: 'png',
      manufacturer: '东富龙',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备07_轧盖机_M3D06_',
    {
      modelId: 'SM_3楼设备07_轧盖机_M3D06_',
      name: '轧盖机',
      code: 'M3D06',
      img: '轧盖机',
      imgType: 'png',
      manufacturer: '东富龙',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺', '人血白蛋白工艺'],
      samplingPoint: [],
    },
  ],
  [
    'SM_3楼设备_静免超滤CIP-5清洗碱罐（R0506_',
    {
      modelId: 'SM_3楼设备_静免超滤CIP-5清洗碱罐（R0506_',
      name: '静免超滤CIP-5清洗碱罐',
      code: 'R0506',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '400L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0519',
        },
        {
          name: '电导',
          point: 'CICR0501',
        },
        {
          name: '差压液位',
          point: 'LICR0501',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_静免超滤CIP-5水罐（R0507_',
    {
      modelId: 'SM_3楼设备_静免超滤CIP-5水罐（R0507_',
      name: '静免超滤CIP-5水罐',
      code: 'R0507',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '400L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0502',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_人白超滤灭活CIP-4清洗碱罐_R0406_',
    {
      modelId: 'SM_3楼设备_人白超滤灭活CIP-4清洗碱罐_R0406_',
      name: '人白超滤灭活CIP-4清洗碱罐',
      code: 'R0406',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '400L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0410',
        },
        {
          name: '电导',
          point: 'CICR0401',
        },
        {
          name: '差压液位',
          point: 'LICR0401',
        },
      ],
    },
  ],
  [
    'SM_3楼设备_人白超滤灭活CIP-4水罐_R0407_',
    {
      modelId: 'SM_3楼设备_人白超滤灭活CIP-4水罐_R0407_',
      name: '人白超滤灭活CIP-4水罐',
      code: 'R0407',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '400L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0402',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_脉动真空灭菌柜',
    {
      modelId: 'SM_4楼设备_脉动真空灭菌柜',
      name: '脉动真空灭菌柜',
      code: '',
      img: '脉动真空灭菌器',
      manufacturer: '新华医疗',
      workingVolume: '',
      process: ['-'],
      samplingPoint: [],
    },
  ],
  [
    'SM_4楼设备_0_9%NaCl_0_4M_NaOH配液罐（R0801）',
    {
      modelId: 'SM_4楼设备_0_9%NaCl_0_4M_NaOH配液罐（R0801）',
      name: '0.9%NaCl/0.4M NaOH配液罐',
      code: 'R0801',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '250L',
      process: ['-'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0801',
        },
        {
          name: '压力',
          point: 'PICR0801',
        },
        {
          name: '罐称重',
          point: 'WICR0801',
        },
        {
          name: '搅拌',
          point: 'M0802',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_融浆罐（R0101_',
    {
      modelId: 'SM_4楼设备_融浆罐（R0101_',
      name: '融浆罐',
      code: 'R0101',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '1200L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0101',
        },
        {
          name: '罐称重',
          point: 'WICR0101',
        },
        {
          name: 'BusValve1_5',
          point: 'Q0106',
        },
        {
          name: '搅拌',
          point: 'M0102',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_1_压滤机（X0208）',
    {
      modelId: 'SM_4楼设备_1_压滤机（X0208）',
      name: '1#压滤机',
      code: 'X0208',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0203',
        },
        {
          name: '压力',
          point: 'PICR0201',
        },
        {
          name: '温度',
          point: 'TICR0204',
        },
        {
          name: '压力',
          point: 'PICR0202',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_2_压滤机（X0308）',
    {
      modelId: 'SM_4楼设备_2_压滤机（X0308）',
      name: '2#压滤机',
      code: 'X0308',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0304',
        },
        {
          name: '压力',
          point: 'PICR0301',
        },
        {
          name: '温度',
          point: 'TICR0305',
        },
        {
          name: '压力',
          point: 'PICR0302',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_3_压滤机（X0224）',
    {
      modelId: 'SM_4楼设备_3_压滤机（X0224）',
      name: '3#压滤机',
      code: 'X0224',
      img: 'machine',
      manufacturer: '英德生物',
      workingVolume: '',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0208',
        },
        {
          name: '压力',
          point: 'PICR0204',
        },
        {
          name: '温度',
          point: 'TICR0209',
        },
        {
          name: '压力',
          point: 'PICR0205',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_FII溶解罐__R0303_',
    {
      modelId: 'SM_4楼设备_FII溶解罐__R0303_',
      name: 'FII溶解罐',
      code: 'R0303',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '1200L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR03017',
        },
        {
          name: '罐称重',
          point: 'WICR0303',
        },
        {
          name: 'BusValve3_39',
          point: 'Q0339',
        },
        {
          name: '搅拌',
          point: 'M0306',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_FII滤液暂存罐_配液罐2___R0304_',
    {
      modelId: 'SM_4楼设备_FII滤液暂存罐_配液罐2___R0304_',
      name: 'FII滤液暂存罐/配液罐2#',
      code: 'R0304',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '1200L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0308',
        },
        {
          name: '罐称重',
          point: 'WICR0304',
        },
        {
          name: 'BusValve4_5',
          point: 'Q0353',
        },
        {
          name: '压力',
          point: 'PICR0303',
        },
        {
          name: '搅拌',
          point: 'M0308',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_FV滤液暂存罐_配液罐1___R0204_',
    {
      modelId: 'SM_4楼设备_FV滤液暂存罐_配液罐1___R0204_',
      name: 'FV滤液暂存罐/配液罐1#',
      code: 'R0204',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '2000L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0210',
        },
        {
          name: '罐称重',
          point: 'WICR0205',
        },
        {
          name: 'BusValve2_44',
          point: 'Q0293',
        },
        {
          name: '压力',
          point: 'PICR0206',
        },
        {
          name: '搅拌',
          point: 'M0210',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_FV溶解罐__R0203_',
    {
      modelId: 'SM_4楼设备_FV溶解罐__R0203_',
      name: 'FV溶解罐',
      code: 'R0203',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '2000L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0207',
        },
        {
          name: '罐称重',
          point: 'WICR0204',
        },
        {
          name: 'BusValve2_16',
          point: 'Q0265',
        },
        {
          name: '搅拌',
          point: 'M0208',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_反应罐1___R0201_',
    {
      modelId: 'SM_4楼设备_反应罐1___R0201_',
      name: '反应罐1#',
      code: 'R0201',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3500L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0201',
        },
        {
          name: '罐称重',
          point: 'WICR0201',
        },
        {
          name: 'BusValve1_7',
          point: 'Q0208',
        },
        {
          name: '搅拌',
          point: 'M0202',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_反应罐2___R0202_',
    {
      modelId: 'SM_4楼设备_反应罐2___R0202_',
      name: '反应罐2#',
      code: 'R0202',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3500L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0205',
        },
        {
          name: '罐称重',
          point: 'WICR0202',
        },
        {
          name: 'BusValve1_31',
          point: 'Q0232',
        },
        {
          name: '搅拌',
          point: 'M0204',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_反应罐3___R0301_',
    {
      modelId: 'SM_4楼设备_反应罐3___R0301_',
      name: '反应罐3#',
      code: 'R0301',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3500L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0301',
        },
        {
          name: '罐称重',
          point: 'WICR0301',
        },
        {
          name: 'BusValve3_8',
          point: 'Q0308',
        },
        {
          name: '搅拌',
          point: 'M0302',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_反应罐4___R0302_',
    {
      modelId: 'SM_4楼设备_反应罐4___R0302_',
      name: '反应罐4#',
      code: 'R0302',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3500L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0306',
        },
        {
          name: '罐称重',
          point: 'WICR0302',
        },
        {
          name: 'BusValve3_28',
          point: 'Q0328',
        },
        {
          name: '搅拌',
          point: 'M0304',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_醋酸乙醇缓冲液罐_（R0205_',
    {
      modelId: 'SM_4楼设备_醋酸乙醇缓冲液罐_（R0205_',
      name: '醋酸乙醇缓冲液罐',
      code: 'R0205',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '100L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0206',
        },
        {
          name: '罐称重',
          point: 'WICR0203',
        },
        {
          name: 'BusValve1_44',
          point: 'Q0245',
        },
        {
          name: '搅拌',
          point: 'M0206',
        },
        {
          name: '压力',
          point: 'PICR0203',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_人白CIP-2清洗碱罐_R0206_',
    {
      modelId: 'SM_4楼设备_人白CIP-2清洗碱罐_R0206_',
      name: '人白CIP-2清洗碱罐',
      code: 'R0206',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '800L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0211',
        },
        {
          name: '电导',
          point: 'CICR0201',
        },
        {
          name: '差压液位',
          point: 'LICR0201',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_人白CIP-2水罐_R0207_',
    {
      modelId: 'SM_4楼设备_人白CIP-2水罐_R0207_',
      name: '人白CIP-2水罐',
      code: 'R0207',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0202',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_球白CIP-3清洗碱罐__R0305_',
    {
      modelId: 'SM_4楼设备_球白CIP-3清洗碱罐__R0305_',
      name: '球白CIP-3清洗碱罐',
      code: 'R0305',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '800L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0309',
        },
        {
          name: '电导',
          point: 'CICR0301',
        },
        {
          name: '差压液位',
          point: 'LICR0301',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_球白CIP-3水罐__R0306_',
    {
      modelId: 'SM_4楼设备_球白CIP-3水罐__R0306_',
      name: '球白CIP-3水罐',
      code: 'R0306',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['静注人免疫球蛋白工艺'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0302',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_融浆单元CIP-1清洗碱罐__R0102_',
    {
      modelId: 'SM_4楼设备_融浆单元CIP-1清洗碱罐__R0102_',
      name: '融浆单元CIP-1清洗碱罐',
      code: 'R0102',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0103',
        },
        {
          name: '电导',
          point: 'CICR0101',
        },
        {
          name: '差压液位',
          point: 'LICR0101',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_融浆单元CIP-1水罐__R0103_',
    {
      modelId: 'SM_4楼设备_融浆单元CIP-1水罐__R0103_',
      name: '融浆单元CIP-1水罐',
      code: 'R0103',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['人血白蛋白工艺'],
      samplingPoint: [
        {
          name: '差压液位',
          point: 'LICR0102',
        },
      ],
    },
  ],
  [
    'SM_4楼设备_CIP碱液配制罐__R0901_',
    {
      modelId: 'SM_4楼设备_CIP碱液配制罐__R0901_',
      name: 'CIP碱液配制罐',
      code: 'R0901',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '600L',
      process: ['-'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0901',
        },
        {
          name: '压力',
          point: 'PICR0901',
        },
        {
          name: '罐称重',
          point: 'WICR0901',
        },
        {
          name: '搅拌',
          point: 'M0902',
        },
        {
          name: 'BusValve',
          point: 'Q0908',
        },
      ],
    },
  ],
  [
    'SM_5楼设备_95%乙醇预冷罐1_V0703',
    {
      modelId: 'SM_5楼设备_95%乙醇预冷罐1_V0703',
      name: '95%乙醇预冷罐1#',
      code: 'V0703',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3000L',
      process: ['-'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0701',
        },
        {
          name: '罐称重',
          point: 'WICR0701',
        },
      ],
    },
  ],
  [
    'SM_5楼设备_95%乙醇预冷罐2_V0704',
    {
      modelId: 'SM_5楼设备_95%乙醇预冷罐2_V0704',
      name: '95%乙醇预冷罐2#',
      code: 'V0704',
      img: 'jars',
      manufacturer: '英德生物',
      workingVolume: '3000L',
      process: ['-'],
      samplingPoint: [
        {
          name: '温度',
          point: 'TICR0702',
        },
        {
          name: '罐称重',
          point: 'WICR0702',
        },
      ],
    },
  ],
];
export const equipmentMap = computed((): Map<string, Equipment> => {
  return new Map(equipments);
});
