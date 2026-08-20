import { MenuIdentifyEnum } from '@/pages/ConfigManagement/ParameterSettings/types';
import { BASE_URL } from '@/services/baseUrl';
import { OperationType } from '../../const';
import { log } from '../../type';

// 配置管理
const ConfigurationManagementEnum: Record<string, log> = {
  '210080001': {
    [`${BASE_URL}/config`]: {
      type: OperationType.delete,
      business: '删除',
    },
    [`${BASE_URL}/config/edit`]: {
      type: OperationType.edit,
      business: '编辑',
    },
    [`${BASE_URL}/config/create`]: {
      type: OperationType.add,
      business: '新增',
    },
    [`${BASE_URL}/config/sort-edit`]: {
      type: OperationType.edit,
      business: '排序',
    },
  },
  '210080002': {
    [`${BASE_URL}/config/station-edit`]: {
      type: OperationType.edit,
      business: '单采血浆站编辑',
    },
    [`${BASE_URL}/config/edit`]: {
      export: config => {
        const data = JSON.parse(config.data);
        let business = '';
        switch (data?.menuIdentify) {
          case MenuIdentifyEnum.GLOBAL_PARAMETER_SETTING:
            business = '全局参数编辑';
            break;
          case MenuIdentifyEnum.INSPECTION_PARAMETER_SETTING:
            business = '检验参数编辑';
            break;
          case MenuIdentifyEnum.RECEIVING_LIBRARY_SETTING:
            business = '领用库编辑';
            break;
          case MenuIdentifyEnum.MATERIAL_PARAMETER_SETTING:
            business = '物料参数编辑';
            break;
          case MenuIdentifyEnum.ROUNDING_RULE_SETTING:
            business = '修约规则编辑';
            break;
          case MenuIdentifyEnum.ROUNDING_PARAMETER_SETTING:
            business = '修约参数编辑';
            break;
          default:
            business = '';
            break;
        }
        return {
          type: OperationType.edit,
          business,
        };
      },
    },
    [`${BASE_URL}/config`]: {
      type: OperationType.delete,
      business: '领用库删除',
    },
    [`${BASE_URL}/config/create`]: {
      type: OperationType.add,
      business: '领用库新增',
    },
  },
  '210080003': {
    [`${BASE_URL}/config/inspect-edit`]: {
      type: OperationType.edit,
      business: '编辑',
    },
  },
  '210080004': {
    [`${BASE_URL}/config/inspect-rule-edit`]: {
      type: OperationType.edit,
      business: '编辑',
    },
  },
  '210080005': {
    [`${BASE_URL}/config/file/create`]: {
      type: OperationType.add,
      business: '新建',
    },
  },
  '210080006': {
    [`${BASE_URL}/config/file/audit`]: {
      type: OperationType.audit,
      business: '审核',
    },
  },
};

export default ConfigurationManagementEnum;
