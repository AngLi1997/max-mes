import { reqPlatformTagTypeGET } from '@/api';
import { t } from '@bmos/i18n';
import { DataNode } from 'ant-design-vue/es/tree';
import { UseTableParams } from '../../../types';
export const useTree = ({ emits }: UseTableParams) => {
  //tree 取值节点
  const fieldNames = {
    children: 'children',
    title: 'tagTypeName',
    key: 'id',
  };
  //暂存间树节点
  const treeData = ref<DataNode[]>([]);
  //获取所有节点
  const getTreeData = async () => {
    try {
      const { data } = await reqPlatformTagTypeGET();
      treeData.value = [
        {
          id: 'all',
          tagTypeName: t('全部'),
          key: 'all',
          children: data,
        },
      ];
      emits('treeData', treeData.value);
    } catch (error) {
      console.log(error);
    }
  };
  onMounted(() => {
    getTreeData();
  });
  return {
    treeData,
    fieldNames,
  };
};
