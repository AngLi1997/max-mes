import { reqPlatformMenuAdminTreeApi } from '@/api';
import { usePermissionStore } from '@/stores/permission.js';
import { flatMenuTreeData } from '@/utils/treeUtils.js';

// 获取权限并保存到pinia中
export async function getPermissions() {
  const { data } = await reqPlatformMenuAdminTreeApi({ rootMenuCode: 121, containsFunc: true });
  const { setPermissions } = usePermissionStore();
  setPermissions(flatMenuTreeData(data));
}
