import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';
import { CUSTOM_FIELD_BUTTON } from './basic';

export const INSPECTION_RESULTS_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  INSPECT_PLEASE_CHECK_NO: {
    componentType: 'INSPECT_PLEASE_CHECK_NO',
    componentName: t('请验单号'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_MATERIAL_NAME: {
    componentType: 'INSPECT_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_MATERIAL_CODE: {
    componentType: 'INSPECT_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_MATERIAL_SPECIFICATION: {
    componentType: 'INSPECT_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_MATERIAL_BATCH_NUMBER: {
    componentType: 'INSPECT_MATERIAL_BATCH_NUMBER',
    componentName: t('物料批号'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_VERIFY_USER: {
    componentType: 'INSPECT_VERIFY_USER',
    componentName: t('请验人'),
    node_type: '',
    icon: 'TEXT',
  },
  INSPECT_VERIFY_DATE: {
    componentType: 'INSPECT_VERIFY_DATE',
    componentName: t('请验时间'),
    node_type: '',
    icon: 'DATE',
  },
};

// 批次检验结果信息
export const INSPECTION_RESULTS: BUSINESS_NODE_INFO_TYPE = {
  BATCH_INSPECTION_RESULTS: {
    componentType: 'BATCH_INSPECTION_RESULTS',
    componentName: t('批次检验结果'),
    node_type: '',
    children: [...objectToArray(INSPECTION_RESULTS_CHILDREN), CUSTOM_FIELD_BUTTON] as BUSINESS_NODE[],
  },
  BATCH_INSPECTION_RESULTS_BUTTON: {
    componentType: 'BATCH_INSPECTION_RESULTS_BUTTON',
    componentName: t('新建组'),
    node_type: '',
    icon: 'Add',
  },
};
