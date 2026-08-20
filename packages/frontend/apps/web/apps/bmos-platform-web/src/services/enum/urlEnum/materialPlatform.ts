import { OperationType } from '../const';
import { log } from '../type';

const MaterialPlatformHeadersEnum: Record<string, log> = {
  '100040002': {
    '/api/app/platform/material/category/save': {
      type: OperationType.add,
      business: '新增物料分类',
    },
    '/api/app/platform/material/category/update': {
      type: OperationType.edit,
      business: '编辑物料分类',
    },
    '/api/app/platform/material/category/delete': {
      type: OperationType.delete,
      business: '删除物料分类',
    },
    '/api/app/platform/material/save': {
      type: OperationType.add,
      business: '新增物料',
    },
    '/api/app/platform/material/update': {
      type: OperationType.edit,
      business: '编辑物料',
    },
    '/api/app/platform/material/changeStatus': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.status ? '启用物料' : '停用物料',
        };
      },
    },
    '/api/app/platform/material/delete': {
      type: OperationType.delete,
      business: '删除物料',
    },
    '/api/app/platform/material/import/material': {
      type: OperationType.add,
      business: '导入物料',
    },
    '/api/app/platform/material/export/material': {
      type: OperationType.add,
      business: '导出物料',
    },
    '/api/app/platform/material/issue': {
      type: OperationType.edit,
      business: '下发物料',
    },
    '/api/app/platform/material/extendUnit/extendUnit/bind': {
      type: OperationType.edit,
      business: '单位配置',
    },
  },
};

export default MaterialPlatformHeadersEnum;
