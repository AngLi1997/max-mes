import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 配料称量——称量详情子节点
export const WEIGHING_INGREDIENTS_DETAILS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  WEIGHING_INGREDIENTS_DETAILS_NAME: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  WEIGHING_INGREDIENTS_DETAILS_CODE: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  WEIGHING_INGREDIENTS_DETAILS_SPECIFICATION: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料批号
  WEIGHING_INGREDIENTS_DETAILS_BATCHNO: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_BATCHNO',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料件号
  WEIGHING_INGREDIENTS_DETAILS_PART_NUMBER: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_PART_NUMBER',
    componentName: t('物料件号'),
    node_type: '',
    icon: 'TEXT',
  },
  // 净重
  WEIGHING_INGREDIENTS_DETAILS_NET_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_NET_WEIGHT',
    componentName: t('净重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 皮重
  WEIGHING_INGREDIENTS_DETAILS_TARE_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_TARE_WEIGHT',
    componentName: t('皮重'),
    node_type: '',
    icon: 'NUMBER',
  },
  //  毛重
  WEIGHING_INGREDIENTS_DETAILS_GROSS_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_GROSS_WEIGHT',
    componentName: t('毛重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  WEIGHING_INGREDIENTS_DETAILS_UNIT: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 称量人
  WEIGHING_INGREDIENTS_DETAILS_WEIGHER: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_WEIGHER',
    componentName: t('称量人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 复核人
  WEIGHING_INGREDIENTS_DETAILS_REVIEWER: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_REVIEWER',
    componentName: t('复核人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 称量时间
  WEIGHING_INGREDIENTS_DETAILS_WEIGHING_TIME: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_WEIGHING_TIME',
    componentName: t('称量时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 配料称量——物料汇总子节点
export const WEIGHING_INGREDIENTS_SUMMARY_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 物料名称
  WEIGHING_INGREDIENTS_SUMMARY_NAME: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料编码
  WEIGHING_INGREDIENTS_SUMMARY_CODE: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  // 物料规格
  WEIGHING_INGREDIENTS_SUMMARY_SPECIFICATION: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总净重
  WEIGHING_INGREDIENTS_SUMMARY_NET_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_NET_WEIGHT',
    componentName: t('总净重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 总皮重
  WEIGHING_INGREDIENTS_SUMMARY_TARE_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_TARE_WEIGHT',
    componentName: t('总皮重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 总毛重
  WEIGHING_INGREDIENTS_SUMMARY_GROSS_WEIGHT: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_GROSS_WEIGHT',
    componentName: t('总毛重'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  WEIGHING_INGREDIENTS_SUMMARY_UNIT: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 总件数
  WEIGHING_INGREDIENTS_SUMMARY_TOTAL_NUMBER: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_TOTAL_NUMBER',
    componentName: t('总件数'),
    node_type: '',
    icon: 'NUMBER',
  },
};

// 配料称量
export const WEIGHING_INGREDIENTS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  WEIGHING_INGREDIENTS_DETAILS: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS',
    componentName: t('称量详情'),
    node_type: '',
    children: objectToArray(WEIGHING_INGREDIENTS_DETAILS_CHILDREN) as BUSINESS_NODE[],
  },
  WEIGHING_INGREDIENTS_DETAILS_BUTTON: {
    componentType: 'WEIGHING_INGREDIENTS_DETAILS_BUTTON',
    componentName: t('新建称量详情组'),
    node_type: '',
    icon: 'Add',
  },
  WEIGHING_INGREDIENTS_SUMMARY: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY',
    componentName: t('物料汇总'),
    node_type: '',
    children: objectToArray(WEIGHING_INGREDIENTS_SUMMARY_CHILDREN) as BUSINESS_NODE[],
  },
  WEIGHING_INGREDIENTS_SUMMARY_BUTTON: {
    componentType: 'WEIGHING_INGREDIENTS_SUMMARY_BUTTON',
    componentName: t('新建物料汇总组'),
    node_type: '',
    icon: 'Add',
  },
  WEIGHING_INGREDIENTS_BUTTON: {
    componentType: 'WEIGHING_INGREDIENTS_BUTTON',
    componentName: t('配料称量按钮'),
    node_type: '',
    icon: 'TEXT',
  },
  ADD_WEIGHING_INGREDIENTS_BUTTON: {
    componentType: 'ADD_WEIGHING_INGREDIENTS_BUTTON',
    componentName: t('新增配料称量按钮'),
    node_type: '',
    icon: 'Add',
  },
};
