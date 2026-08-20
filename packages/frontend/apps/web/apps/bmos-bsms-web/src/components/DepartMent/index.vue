<template>
  <div class="tree-container">
    <BMSearchTree
      :showSearch="showSearch"
      :showAllAddIcon="false"
      :showAction="false"
      :checkStrictly="true"
      :tree-data="tree.treeData"
      :checkable="true"
      v-model:expanded-keys="tree.EXPANDED_KEYS"
      :checked-keys="tree.SELECT_KEYS"
      :fieldNames="{ title: 'name', key: 'id' }"
      @check="TREE_CHECK"></BMSearchTree>
  </div>
</template>

<script setup lang="ts">
  import { BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { onMounted, reactive } from 'vue';
  import { getResourcePermissionTree, getResourcePermissionTreeDept, getPermissionPartitionTree } from '@/services';
  import { useConfig } from '@/stores/config';
  import { storeToRefs } from 'pinia';
  import { COMPANY_CODE } from '@/components/AllocationPerson/components/const';
  const store = useConfig();
  const { configs } = storeToRefs(store);
  const props = withDefaults(
    defineProps<{
      isAdd?: boolean;
      checks?: any[];
      record?: string;
      type?: boolean;
      showSearch?: boolean;
    }>(),
    {
      isAdd: true,
      type: true,
      showSearch: false,
    },
  );

  const tree = reactive<{
    treeData: any[];
    SELECT_KEYS: any[];
    EXPANDED_KEYS: any[];
  }>({
    treeData: [],
    SELECT_KEYS: [],
    EXPANDED_KEYS: [],
  });

  const addFa = (node: any) => {
    let par: any = node.parent;
    while (par) {
      if (tree.SELECT_KEYS.includes(par.key)) {
        break;
      }
      tree.SELECT_KEYS.push(par.key);
      par = par.parent;
    }
  };

  const getChildren = (arr: any[], keys: any[] = []) => {
    arr.forEach(item => {
      if (item.children && item.children.length > 0) {
        getChildren(item.children, keys);
      }
      keys.push(item.id);
    });
  };

  const filterFa = (node: any) => {
    const fils: any[] = [];
    let cur = node;
    // while (true) {
    //   if (!cur || !cur.parent) break;
    //   const flag = cur.parent?.children.every((item: any) => {
    //     if (item.key !== cur.key) {
    //       return !tree.SELECT_KEYS.includes(item.key);
    //     }
    //     return true;
    //   });
    //   if (!flag) break;

    //   cur = cur.parent;
    //   fils.push(cur.key);
    // }
    if (node.children && node.children.length > 0) {
      getChildren(node.children, fils);
    }
    if (fils.length === 0) return;
    tree.SELECT_KEYS = tree.SELECT_KEYS.filter(i => !fils.includes(i));
  };

  const TREE_CHECK = (keys: any, { checked, checkedNodes, node }: any) => {
    tree.SELECT_KEYS = keys?.checked ? keys?.checked : keys;
    if (checked) {
      node?.parent && addFa(node);
    } else {
      filterFa(node);
    }
  };

  const getTreeData = async () => {
    try {
      if (!configs.value[COMPANY_CODE]) {
        await store.findConfigByCode(COMPANY_CODE);
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
    try {
      let res: any;
      if (props.type) res = await getResourcePermissionTree();
      else res = await getPermissionPartitionTree();
      tree.treeData = [
        {
          name: configs.value?.[COMPANY_CODE]?.value || t('部门权限'),
          id: COMPANY_CODE,
          children: res.data,
          checkable: false,
        },
      ];
      tree.EXPANDED_KEYS = [COMPANY_CODE];
    } catch (error) {
      tree.treeData = [];
      message.error(t('查询数据失败'));
    }
  };

  const getSelectKeys = async () => {
    if (!props.record) return;
    try {
      const res = await getResourcePermissionTreeDept({
        resourceId: props.record,
      });

      tree.SELECT_KEYS = res.data;
    } catch (error) {
      tree.SELECT_KEYS = [];
      message.error(t('查询数据失败'));
    }
  };

  const exposeSelectKeys = () => {
    if (tree.SELECT_KEYS && tree.SELECT_KEYS.length > 0) {
      return tree.SELECT_KEYS?.filter(item => item !== COMPANY_CODE);
    }
    return [];
  };

  onMounted(() => {
    getTreeData();
    if (!props.isAdd) {
      getSelectKeys();
    } else {
      tree.SELECT_KEYS = props.checks as any[];
    }
  });

  defineExpose({
    getSelectKeys: exposeSelectKeys,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
