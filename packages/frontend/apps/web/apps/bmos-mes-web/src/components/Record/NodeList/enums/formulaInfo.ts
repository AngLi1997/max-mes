import { objectToArray } from '@bmos/utils';
import { BUSINESS_NODE, BUSINESS_NODE_INFO_TYPE } from '../enum';

// 生产BOM物料子节点
export const BUSINESS_FORMULA_INFO_MATERIAL_CHILDREN: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_FORMULA_INFO_MATERIAL_NAME: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_NAME',
    componentName: t('物料名称'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_MATERIAL_CODE: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_CODE',
    componentName: t('物料编码'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_MATERIAL_SPECIFICATION: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_SPECIFICATION',
    componentName: t('物料规格'),
    node_type: '',
    icon: 'TEXT',
  },
  BUSINESS_FORMULA_INFO_THEORETICAL_QUANTITY: {
    componentType: 'BUSINESS_FORMULA_INFO_THEORETICAL_QUANTITY',
    componentName: t('理论用量'),
    node_type: '',
    icon: 'NUMBER',
  },
  BUSINESS_FORMULA_INFO_UNIT: {
    componentType: 'BUSINESS_FORMULA_INFO_UNIT',
    componentName: t('单位'),
    node_type: '',
    icon: 'TEXT',
  },
};

// 生产BOM信息子节点
export const BUSINESS_FORMULA_INFO_MATERIAL: BUSINESS_NODE_INFO_TYPE = {
  BUSINESS_FORMULA_INFO_MATERIAL: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL',
    componentName: t('生产BOM物料'),
    node_type: '',
    children: objectToArray(BUSINESS_FORMULA_INFO_MATERIAL_CHILDREN) as BUSINESS_NODE[],
  },
  BUSINESS_FORMULA_INFO_MATERIAL_BUTTON: {
    componentType: 'BUSINESS_FORMULA_INFO_MATERIAL_BUTTON',
    componentName: t('新建组'),
    node_type: '',
    icon: 'Add',
  },
};
