import { OperationType } from '../const';
import { log } from '../type';
const UnitsManagementHeadersEnum: Record<string, log> = {
  '100040001': {
    '/api/app/platform/unit/save/unit': {
      type: OperationType.add,
      business: '新增标准单位',
    },
    '/api/app/platform/unit/update/unit': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.state ? '启用标准单位' : !data.state && data.roundName ? '停用标准单位' : '编辑标准单位', //停用时比编辑多传了个roundName
        };
      },
    },
    '/api/app/platform/unit/delete/unit': {
      type: OperationType.delete,
      business: '删除标准单位',
    },
    '/api/app/platform/unit/save/unit/extend': {
      type: OperationType.add,
      business: '新增扩展单位',
    },
    '/api/app/platform/unit/update/unit/extend': {
      type: OperationType.edit,
      business: '编辑扩展单位',
    },
    '/api/app/platform/unit/update/extend/state': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.state ? '启用扩展单位' : '停用扩展单位',
        };
      },
    },
    '/api/app/platform/unit/delete/unit/extend': {
      type: OperationType.delete,
      business: '删除扩展单位',
    },
  },
};

export default UnitsManagementHeadersEnum;
