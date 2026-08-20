import { UserList } from '@/components/Sign/type';
import { reqMaterialPositionListBoundUser } from '@/services';

export type UsePositionUserListParams = {};

export const usePositionUserList = () => {
  // 签名userList
  const curPositionId = ref<string>('');
  const positionUserList = ref<UserList[]>([]);
  const getPositionUserList = async (code?: string) => {
    try {
      const { data } = await reqMaterialPositionListBoundUser(curPositionId.value, ...(code ? [code] : []));
      positionUserList.value =
        data?.map((userItem: any) => {
          return {
            label: userItem.userName + '-' + userItem.loginName,
            value: userItem.userId,
            ...userItem,
          };
        }) || [];
      return Promise.resolve();
    } catch (error: any) {
      return Promise.reject(error);
    }
  };
  return {
    getPositionUserList,
    curPositionId,
    positionUserList,
  };
};
