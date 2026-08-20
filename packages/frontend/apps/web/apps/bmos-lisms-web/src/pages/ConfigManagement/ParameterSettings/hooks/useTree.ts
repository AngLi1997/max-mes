import { postStaticDataConfigMenuTree } from '@/services';
export const useTree = () => {
  const treeData = ref<any[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await postStaticDataConfigMenuTree('2');
      treeData.value = data;
    } catch (error) {
      //
    }
  };
  onMounted(() => {
    getTreeData();
  });

  return {
    treeData,
  };
};
