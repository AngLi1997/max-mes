import { OperationType } from '../../const';
import { log } from '../../type';
// 生产BOM配置
const FormulaConfigurationEnum: Record<string, log> = {
  '120020004': {
    '/app/mes/product/formula/version/changeState': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.state === 'true' ? '启用版本' : '停用版本',
        };
      },
    },
    '/app/mes/product/formula/audit/submit': {
      type: OperationType.audit,
      business: '审核',
    },
    '/app/mes/product/formula/save': {
      type: OperationType.add,
      business: '新增生产BOM',
    },
    '/app/mes/product/formula/version/save': {
      type: OperationType.add,
      business: '新增版本',
    },
    '/app/mes/product/formula/version/edit': {
      type: OperationType.edit,
      business: '编辑版本',
    },
    '/app/mes/resource/permission/save': {
      type: OperationType.edit,
      business: '生产BOM数据授权',
    },
  },
};

export { FormulaConfigurationEnum };
