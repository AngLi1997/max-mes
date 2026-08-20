import { UserList } from '@/components/Sign/type';
import { reqPlatformUserListByMenuId } from '@/services';

export const usePermissionCodeUserList = () => {
  // 签名userList
  const permissionCodeUserList = ref<UserList[]>([]);
  const getPermissionCodeUserList = async (code: string) => {
    try {
      const { data } = await reqPlatformUserListByMenuId(code);
      permissionCodeUserList.value = data || [];
      return Promise.resolve(data || []);
    } catch (error: any) {
      return Promise.reject(error);
    }
  };
  return {
    getPermissionCodeUserList,
    permissionCodeUserList,
  };
};
