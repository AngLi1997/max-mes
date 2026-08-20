import { getProductMaterialFinishProductTree } from '@/services';
import { message } from 'ant-design-vue';
import { DataNode } from 'ant-design-vue/es/tree';

export const rootKey = 'all';
export const useTree = () => {
  const treeField = {
    field: {
      productIds: 'id',
      categoryFlag: 'categoryFlag',
    },
  };
  const fieldNames = {
    title: 'showName',
    key: 'id',
  };
  const TREE_DATA = ref<DataNode[]>([]);

  const INIT_TREE_DATA = async () => {
    try {
      const { data } = await getProductMaterialFinishProductTree();
      TREE_DATA.value = [
        {
          id: 'all',
          key: 'all',
          showName: t('全部'),
          children: data,
        },
      ];
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {
    INIT_TREE_DATA();
  });

  return {
    TREE_DATA,
    INIT_TREE_DATA,
    fieldNames,
    treeField,
  };
};
