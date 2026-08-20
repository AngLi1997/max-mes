import { OperationType } from '../const';
import { log } from '../type';
const UserManagementHeadersEnum: Record<string, log> = {
  '100030001': {
    '/api/app/platform/user/save': {
      type: OperationType.add,
      business: '新增用户',
    },
    // '': {
    //   type: OperationType.add,
    //   business: '导入',
    // },
    // '': {
    //   type: OperationType.export,
    //   business: '导出',
    // },
    '/api/app/platform/user/update': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.lock ? '解锁' : '编辑用户',
        };
      },
    },
    '/api/app/platform/user/relate-role-save': {
      type: OperationType.relevance,
      business: '绑定角色',
    },
    '/api/app/platform/user/relate-dept-save': {
      type: OperationType.relevance,
      business: '分配部门',
    },
    '/api/app/platform/user/resetPwd': {
      type: OperationType.edit,
      business: '重置密码',
    },
    '/api/app/platform/user/changePwd': {
      type: OperationType.edit,
      business: '修改密码',
    },
    '/api/app/platform/user/start': {
      export: config => {
        const data = JSON.parse(config.data);
        return {
          type: OperationType.edit,
          business: data.state ? '启用' : '停用',
        };
      },
    },
    '/api/app/platform/user/import/user': {
      type: OperationType.add,
      business: '导入用户',
    },
    '/api/app/platform/user/export/user': {
      type: OperationType.export,
      business: '导出用户',
    },
  },
};

export default UserManagementHeadersEnum;
