import { OperationType } from '../const';
import { log } from '../type';
const MenuPermissionsHeadersEnum: Record<string, log> = {
  '100030005': {
    '/api/app/platform/menu/role/save': {
      type: OperationType.relevance,
      business: '编辑菜单权限角色',
    },
  },
};

export default MenuPermissionsHeadersEnum;
