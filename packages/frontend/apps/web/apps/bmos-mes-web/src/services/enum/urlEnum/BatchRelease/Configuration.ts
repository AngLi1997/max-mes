import { OperationType } from '../../const';
import { log } from '../../type';
// 批签发配置
const BatchReleaseConfigurationEnum: Record<string, log> = {
  '120040002': {
    '/app/mes/lotRelease/template/category/createCategory': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/app/mes/lotRelease/template/category/editCategory': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/app/mes/lotRelease/template/category/delete': {
      type: OperationType.delete,
      business: '删除分类',
    },
    '/app/mes/lotRelease/template/createTemplate': {
      type: OperationType.add,
      business: '新增模板',
    },
    '/app/mes/lotRelease/template/createTemplateVersion': {
      type: OperationType.add,
      business: '新增模板版本',
    },
    '/app/mes/lotRelease/template/updateTemplateFile': {
      type: OperationType.add,
      business: '上传模板',
    },
    '/app/mes/lotRelease/template/makeDefault': {
      type: OperationType.edit,
      business: '设为默认',
    },
    '/app/mes/lotRelease/template/makeSure': {
      type: OperationType.edit,
      business: '确认',
    },
    '/app/mes/lotRelease/template/scrap': {
      type: OperationType.edit,
      business: '作废',
    },
    '/app/mes/lotRelease/manage/generate': {
      type: OperationType.add,
      business: '验证',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '数据授权',
    },
    '/app/mes/lotRelease/template/bindProcess': {
      type: OperationType.edit,
      business: '绑定工艺',
    },
    '/app/mes/lotRelease/template/downloadTemplate': {
      type: OperationType.export,
      business: '下载',
    },
  },
};

export { BatchReleaseConfigurationEnum };
