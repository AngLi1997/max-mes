import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE, CUSTOM_FIELD_BUTTON } from '../enum';

//配液投入-投料详情子节点
export const LIQUID_PREPARATION_INPUT_DETAIL: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_INPUT_DETAIL_NAME: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_CODE: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_BATCH_NO: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_BATCH_NO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_PART_NO: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_PART_NO',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 批次配液量
  LIQUID_PREPARATION_INPUT_DETAIL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_QUANTITY',
    componentName: t('物料量'),
    node_type: '',
    icon: 'NUMBER',
  },
  //单位
  LIQUID_PREPARATION_INPUT_DETAIL_UNIT: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_IMPORTER: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_IMPORTER',
    componentName: t('投料人'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_INPUT_TIME',
    componentName: t('投料时间'),
    node_type: '',
    icon: 'DATE',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_CONTAINER_NAME: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_CONTAINER_NAME',
    componentName: t('设备名称'),
    node_type: '',
    icon: 'TEXT',
  },
  LIQUID_PREPARATION_INPUT_DETAIL_DEVICE_NO: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL_DEVICE_NO',
    componentName: t('设备编号'),
    node_type: '',
    icon: 'TEXT',
  },
  // // 原始编码
  // LIQUID_PREPARATION_INPUT_BATCH_ORIGINAL_CODE: {
  //   componentType: 'LIQUID_PREPARATION_INPUT_BATCH_ORIGINAL_CODE',
  //   componentName: t('原始编码'),
  //   node_type: '',
  //   icon: 'TEXT',
  // },
  // // 报告单编号
  // LIQUID_PREPARATION_INPUT_BATCH_REPORT_NO: {
  //   componentType: 'LIQUID_PREPARATION_INPUT_BATCH_REPORT_NO',
  //   componentName: t('报告单编号'),
  //   node_type: '',
  //   icon: 'TEXT',
  // },
  // // 放行单编号
  // LIQUID_PREPARATION_INPUT_BATCH_RELEASE_NO: {
  //   componentType: 'LIQUID_PREPARATION_INPUT_BATCH_RELEASE_NO',
  //   componentName: t('放行单编号'),
  //   node_type: '',
  //   icon: 'TEXT',
  // },
};

// 配液投入-物料汇总子节点
export const LIQUID_PREPARATION_INPUT_SUMMARY: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  LIQUID_PREPARATION_INPUT_SUMMARY_NAME: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  LIQUID_PREPARATION_INPUT_SUMMARY_CODE: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  LIQUID_PREPARATION_INPUT_SUMMARY_SPECIFICATION: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 投料总量
  LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_QUANTITY: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_QUANTITY',
    componentName: t('投料总量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  LIQUID_PREPARATION_INPUT_SUMMARY_UNIT: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_COUNT: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_TOTAL_COUNT',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 配液投入-配液投入子节点
export const LIQUID_PREPARATION_INPUT: BUSINESS_NODE_INFO_TYPE = {
  LIQUID_PREPARATION_INPUT_DETAIL: {
    componentType: 'LIQUID_PREPARATION_INPUT_DETAIL',
    componentName: t('投料详情'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_INPUT_DETAIL) as BUSINESS_NODE[], CUSTOM_FIELD_BUTTON],
  },
  LIQUID_PREPARATION_INPUT_BATCH_BUTTON: {
    componentType: 'LIQUID_PREPARATION_INPUT_BATCH_BUTTON',
    componentName: t('新建投料详情'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_PREPARATION_INPUT_SUMMARY: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: [...objectToArray(LIQUID_PREPARATION_INPUT_SUMMARY) as BUSINESS_NODE[]],
  },
  LIQUID_PREPARATION_INPUT_SUMMARY_BUTTON: {
    componentType: 'LIQUID_PREPARATION_INPUT_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  LIQUID_INPUT_BUTTON: {
    componentType: 'LIQUID_INPUT_BUTTON',
    componentName: t('投料按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_LIQUID_INPUT_BUTTON: {
    componentType: 'ADD_LIQUID_INPUT_BUTTON',
    componentName: t('新增投料按钮'),
    node_type: '',
    icon: 'Add',
  },
};
