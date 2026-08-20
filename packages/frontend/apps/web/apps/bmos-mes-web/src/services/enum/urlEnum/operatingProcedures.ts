//操作规程
import { OperationType } from '../const';
import { log } from '../type';

const OperatingProcedures: Record<string, log> = {
  '120020011': {
    '/app/mes/operate/save/category': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/mes/operate/update/category': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/mes/operate/delete/category': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/mes/operate/rule/save': {
      type: OperationType.add,
      business: '新增文件',
    },
    '/app/mes/operate/rule/version/save': {
      type: OperationType.add,
      business: '新增版本',
    },
    '/app/mes/operate/rule/version/update': {
      type: OperationType.edit,
      business: '编辑文件版本',
    },
    '/app/mes/operate/rule/version/update/state': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.stateName,
        };
      },
    },
    '/app/mes/operate/rule/version/start/flow': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.auditType ? '启用文件版本' : '停用文件版本',
        };
      },
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '文件数据授权',
    },
    '/app/mes/operate/rule/version/update/effect': {
      type: OperationType.edit,
      business: '版本立即生效',
    },
    '/app/mes/operate/rule/version/update/valid': {
      type: OperationType.edit,
      business: '版本直接生效',
    },
  },
};

export default OperatingProcedures;
