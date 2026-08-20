import { OperationType } from '../../const';
import { log } from '../../type';
// 批记录配置
const BatchRecordsConfigurationEnum: Record<string, log> = {
  '120080001': {
    '/app/mes/plan/archive/template/category/save': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/mes/plan/archive/template/category/update': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/mes/plan/archive/template/category/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/mes/plan/archive/template/save': {
      type: OperationType.add,
      business: '新增模板',
    },
    '/app/mes/plan/archive/template/version/save': {
      type: OperationType.add,
      business: '新增模板版本',
    },
    '/app/mes/plan/archive/template/version/upload': {
      type: OperationType.add,
      business: '上传模板',
    },
    '/app/mes/plan/archive/template/version/normal': {
      type: OperationType.edit,
      business: '设为默认',
    },
    '/app/mes/plan/archive/template/version/confirm': {
      type: OperationType.edit,
      business: '确认',
    },
    '/app/mes/plan/archive/template/scrap': {
      type: OperationType.edit,
      business: '作废',
    },
    '/app/mes/plan/archive/version/verify': {
      type: OperationType.add,
      business: '验证',
    },
    '/app/mes/plan/archive/template/version/download': {
      type: OperationType.export,
      business: '下载',
    },
    '/app/mes/plan/archive/template/path/download': {
      type: OperationType.export,
      business: '下载文件',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '数据授权',
    },
    '/app/mes/plan/archive/template/bind/process': {
      type: OperationType.edit,
      business: '绑定工艺',
    },
  },
};

export { BatchRecordsConfigurationEnum };
