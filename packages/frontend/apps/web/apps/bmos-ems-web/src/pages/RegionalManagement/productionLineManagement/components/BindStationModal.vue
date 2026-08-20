<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('绑定工位')"
    wrapClassName="modalSizeMedium"
    :cancelText="t('取消')"
    :okText="t('确定')"
    @okModal="ok">
    <div class="tree-container">
      <BMSearchTree
        v-if="tree.treeData[0]?.children.length > 0"
        v-model:checked-keys="tree.CHECKED_KEYS"
        v-model:expanded-keys="tree.EXPANDED_KEYS"
        :showSearch="true"
        :showAllAddIcon="false"
        :showAction="false"
        :tree-data="tree.treeData"
        :checkable="true"
        :fieldNames="{ title: 'name', key: 'id' }"
        @check="check"></BMSearchTree>
      <Empty v-else></Empty>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm, BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Key } from 'ant-design-vue/lib/_util/type';
  import { message } from 'ant-design-vue';
  import { reactive, watch } from 'vue';
  import { reqEquipmentStationTree, reqLineBindStation } from '@/services';
  const open = ref<boolean>(false);
  const emit = defineEmits(['updateTable']);
  const props = withDefaults(
    defineProps<{
      rowId: string;
      stationIdList: any;
    }>(),
    {
      rowId: '',
      stationIdList: [],
    },
  );
  const tree = reactive<any>({
    treeData: [],
    CHECKED_KEYS: [],
    EXPANDED_KEYS: [],
  });
  const checkedIds = ref<any>([]); //设备id集合
  const loopTree = (data: any) => {
    return data?.map((item: any) => {
      item.name = `${item.code}-${item.name}`;
      if (item.children && item.children?.length > 0) {
        item.children = [...item.children, ...(item.infoList || [])];
      } else {
        item.children = item?.infoList || [];
      }
      if (!item.infoList && item.infoList !== null) {
        //有此字段则为工位(非分类)
        item.stationFlag = true;
      }
      if (item.children) {
        loopTree(item.children);
      }
      return item;
    });
  };
  const getTreeData = async () => {
    try {
      const res = await reqEquipmentStationTree();
      tree.treeData = [
        {
          name: t('全部'),
          id: '0',
          children: loopTree(res.data || []),
        },
      ] as any[];
    } catch (error: any) {
      tree.treeData = [];
      message.error(error.message);
    } finally {
      tree.EXPANDED_KEYS = ['0'];
    }
  };
  // 选中复选框触发
  const check = (
    selectedKeys: Key[],
    info: {
      checkedNodes: any;
    },
  ) => {
    checkedIds.value = [];
    const temp = info.checkedNodes.filter((item: any) => item.stationFlag == true);
    temp.forEach((item: any) => {
      checkedIds.value.push(item.id);
    });
  };
  // 绑定设备确定弹框
  const ok = async () => {
    if (tree.treeData[0]?.children.length === 0) {
      open.value = false;
      return;
    }
    try {
      const data = {
        id: props.rowId,
        stationIdList: checkedIds.value,
      };
      await reqLineBindStation(data);
      message.success(t('绑定成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getTreeData();
        tree.CHECKED_KEYS = props.stationIdList;
        checkedIds.value = props.stationIdList;
      } else {
        tree.CHECKED_KEYS = [];
      }
    },
    {
      immediate: true,
    },
  );
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .tree-container {
    height: 400px;
    overflow-y: auto;
  }
</style>
