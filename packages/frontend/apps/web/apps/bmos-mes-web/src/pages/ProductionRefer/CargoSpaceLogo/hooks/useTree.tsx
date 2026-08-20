import { reqStorageConfigQueryAllTreeWithCargoPosition } from '@/services';

import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { reactive, ref } from 'vue';

export const useTree = () => {
  const treeField = reactive({
    field: {
      materialPositionId: 'id',
    },
  });

  const treeData = ref<any>([
    {
      id: 'all',
      name: t('全部'),
      children: [],
    },
  ]);

  const fetchTreeData = async () => {
    try {
      const res = await reqStorageConfigQueryAllTreeWithCargoPosition();
      const data = res.data;
      treeData.value[0].children = data;
    } catch (error: any) {
      message.error(error.message);
    }
  };

  return {
    treeField,
    treeData,
    fetchTreeData,
  };
};
