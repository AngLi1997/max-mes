import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 中间品产出——产出详情子节点
export const OUTPUT_WEIGHING_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  OUTPUT_WEIGHING_DETAILS_NAME: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  OUTPUT_WEIGHING_DETAILS_CODE: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  OUTPUT_WEIGHING_DETAILS_SPECIFICATION: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  OUTPUT_WEIGHING_DETAILS_BATCHNO: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  OUTPUT_WEIGHING_DETAILS_PART_NUMBER: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_PART_NUMBER',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料量
  OUTPUT_WEIGHING_DETAILS_QUANTITY: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 净重
  OUTPUT_WEIGHING_DETAILS_NET_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_NET_WEIGHT',
    componentName: t('净重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 皮重
  OUTPUT_WEIGHING_DETAILS_TARE_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_TARE_WEIGHT',
    componentName: t('皮重'),
    node_type: '',
    icon: 'NUMBER',
  },
  //  毛重
  OUTPUT_WEIGHING_DETAILS_GROSS_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_GROSS_WEIGHT',
    componentName: t('毛重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  OUTPUT_WEIGHING_DETAILS_UNIT: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 产出人
  OUTPUT_WEIGHING_DETAILS_WEIGHER: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_WEIGHER',
    componentName: t('产出人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 复核人
  OUTPUT_WEIGHING_DETAILS_REVIEWER: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_REVIEWER',
    componentName: t('复核人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 产出时间
  OUTPUT_WEIGHING_DETAILS_WEIGHING_TIME: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_WEIGHING_TIME',
    componentName: t('产出时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 产出称量———物料汇总子节点
export const OUTPUT_WEIGHING_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  OUTPUT_WEIGHING_SUMMARY_NAME: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  OUTPUT_WEIGHING_SUMMARY_CODE: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  OUTPUT_WEIGHING_SUMMARY_SPECIFICATION: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料总量
  OUTPUT_WEIGHING_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_TOTAL_QUANTITY',
    componentName: t('物料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 总净重
  OUTPUT_WEIGHING_SUMMARY_NET_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_NET_WEIGHT',
    componentName: t('总净重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 总皮重
  OUTPUT_WEIGHING_SUMMARY_TARE_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_TARE_WEIGHT',
    componentName: t('总皮重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 总毛重
  OUTPUT_WEIGHING_SUMMARY_GROSS_WEIGHT: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_GROSS_WEIGHT',
    componentName: t('总毛重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  OUTPUT_WEIGHING_SUMMARY_UNIT: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  OUTPUT_WEIGHING_SUMMARY_TOTAL_NUMBER: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_TOTAL_NUMBER',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 中间品产出
export const OUTPUT_WEIGHING_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  OUTPUT_WEIGHING_DETAILS: {
    componentType: 'OUTPUT_WEIGHING_DETAILS',
    componentName: t('产出详情'),
    node_type: '',
    children: objectToArray(OUTPUT_WEIGHING_DETAILS_CHILDREN) as BUSINESS_NODE[],
  },
  OUTPUT_WEIGHING_DETAILS_BUTTON: {
    componentType: 'OUTPUT_WEIGHING_DETAILS_BUTTON',
    componentName: t('新建产出详情组'),
    node_type: '',
    icon: 'Add',
  },
  OUTPUT_WEIGHING_SUMMARY: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(OUTPUT_WEIGHING_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  OUTPUT_WEIGHING_SUMMARY_BUTTON: {
    componentType: 'OUTPUT_WEIGHING_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  OUTPUT_BUTTON_ASSEMBLY: {
    componentType: 'OUTPUT_BUTTON_ASSEMBLY',
    componentName: t('产出按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  OUTPUT_BUTTON: {
    componentType: 'OUTPUT_BUTTON',
    componentName: t('新增产出按钮'),
    node_type: '',
    icon: 'Add',
  },
};
