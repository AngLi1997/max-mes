import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
// 称量数据节点
export const WEIGHING_DATA_DETAIL_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  // 重量
  WEIGHING_DATA_DETAIL_WEIGHT: {
    componentType: 'WEIGHING_DATA_DETAIL_WEIGHT',
    componentName: t('重量'),
    node_type: '',
    icon: 'NUMBER',
  },
  // 单位
  WEIGHING_DATA_DETAIL_UNIT: {
    componentType: 'WEIGHING_DATA_DETAIL_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
  // 称量人
  WEIGHING_DATA_DETAIL_OPERATOR: {
    componentType: 'WEIGHING_DATA_DETAIL_OPERATOR',
    componentName: t('称量人'),
    node_type: '',
    icon: 'TEXT',
  },
  // 称量时间
  WEIGHING_DATA_DETAIL_TIME: {
    componentType: 'WEIGHING_DATA_DETAIL_TIME',
    componentName: t('称量时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 称量数据
export const WEIGHING_DATA: BUSINESS_NODE_INFO_TYPE = {
  WEIGHING_DATA_DETAIL: {
    componentType: 'WEIGHING_DATA_DETAIL',
    componentName: t('称量详情'),
    node_type: '',
    children: objectToArray(WEIGHING_DATA_DETAIL_CHILDREN) as BUSINESS_NODE[],
  },
  WEIGHING_DATA_DETAIL_BUTTON: {
    componentType: 'WEIGHING_DATA_DETAIL_BUTTON',
    componentName: t('新建组'),
    node_type: '',
    icon: 'Add',
  },
};
