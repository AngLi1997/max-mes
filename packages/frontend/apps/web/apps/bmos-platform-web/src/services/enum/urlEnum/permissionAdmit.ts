import { OperationType } from '../const';
import { log } from '../type';
const PermissionAdmitHeadersEnum: Record<string, log> = {
  '100030004': {
    '/api/app/platform/menu/auth/role/save': {
      type: OperationType.relevance,
      business: '编辑权限授权角色',
    },
  },
};

export default PermissionAdmitHeadersEnum;
