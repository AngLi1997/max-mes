import { OperationType } from '../const';
import { log } from '../type';
const DepartmentManagementHeadersEnum: Record<string, log> = {
  '100030002': {
    '/api/app/platform/dept/save': {
      type: OperationType.add,
      business: '新增部门',
    },
    '/api/app/platform/dept/update': {
      type: OperationType.edit,
      business: '编辑部门',
    },
    '/api/app/platform/dept/delete': {
      type: OperationType.delete,
      business: '删除部门',
    },
    '/api/app/platform/dept/relate-user-save': {
      type: OperationType.relevance,
      business: '分配部门人员',
    },
    '/api/app/platform/dept/remove/user': {
      type: OperationType.edit,
      business: '移除部门人员',
    },
    '/api/app/platform/dept/relate-user-delAll': {
      type: OperationType.edit,
      business: '移除部门人员',
    },
    '/api/app/platform/user/dept/user/bind/role': {
      type: OperationType.edit,
      business: '绑定角色',
    },
    '/api/app/platform/equipment/station/user/bind/station': {
      type: OperationType.edit,
      business: '绑定工位',
    },
    '/api/app/platform/dept/bind/role': {
      type: OperationType.edit,
      business: '分配角色',
    },
  },
};

export default DepartmentManagementHeadersEnum;
