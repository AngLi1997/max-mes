import { UserList } from '@/components/Sign/type';
import { reqMaterialPositionListBoundUser } from '@/services';

export type UsePositionUserListParams = {};

export const usePositionUserList = () => {
  // 签名userList
  const curPositionId = ref<string>('');
  const positionUserList = ref<UserList[]>([]);
  const getPositionUserList = async () => {
    try {
      const { data } = await reqMaterialPositionListBoundUser(
        curPositionId.value,
      );
      positionUserList.value = data || [];
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
