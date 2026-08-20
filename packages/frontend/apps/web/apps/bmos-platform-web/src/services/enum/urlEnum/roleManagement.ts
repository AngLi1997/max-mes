import { OperationType } from '../const';
import { log } from '../type';
const RoleManagementHeadersEnum: Record<string, log> = {
  '100030003': {
    '/api/app/platform/role/save-role': {
      type: OperationType.add,
      business: '新增角色',
    },
    '/api/app/platform/role/relate-user-save': {
      type: OperationType.relevance,
      business: '人员分配',
    },
    '/api/app/platform/role/menu/save': {
      type: OperationType.relevance,
      business: '菜单分配',
    },
    '/api/app/platform/role/update-role': {
      type: OperationType.edit,
      business: '编辑角色',
    },
    '/api/app/platform/role/delete-role': {
      type: OperationType.delete,
      business: '删除角色',
    },
    '/api/app/platform/role/save-type': {
      type: OperationType.add,
      business: '新增分类',
    },
    '/api/app/platform/role/update-type': {
      type: OperationType.edit,
      business: '编辑分类',
    },
    '/api/app/platform/role/delete-type': {
      type: OperationType.delete,
      business: '删除分类',
    },
  },
};

export default RoleManagementHeadersEnum;
