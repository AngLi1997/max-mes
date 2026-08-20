<template>
  <div>
    <Modal
      :okText="t('确定')"
      :cancelText="t('取消')"
      :open="props.open"
      :title="t('绑定产线')"
      :maskClosable="false"
      wrapClassName="modalSizeMedium"
      @ok="handleOk"
      @cancel="close">
      <div style="height: 510px; overflow: auto">
        <Tree
          v-if="treeData.length > 0 && allRoles.length > 0"
          v-model:expandedKeys="expandedKeys"
          v-model:checkedKeys="checkedKeys"
          checkable
          :default-expand-all="true"
          :tree-data="treeData"
          :fieldNames="{
            title: 'name',
            key: 'id',
          }"
          @check="checkNode" />
        <Empty v-else :emptyName="t('暂无产线信息')" />
      </div>
    </Modal>
  </div>
</template>

<script lang="ts" setup>
  import { getPlanTeamListByTeamId, postBoundProductionLine, getProcessProductLine } from '@/services';
  import { Modal, Tree, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  const emit = defineEmits(['update:open', 'success']);
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    menuId: {
      type: String,
      default: '',
    },
    node: {
      type: Object,
      default: () => ({}),
    },
  });
  const expandedKeys = ref<string[]>([]);
  const checkedKeys = ref<string[]>([]);
  const treeData = ref<any>([]);
  const allRoles = ref([]); //查所有树结构数据(非回显数据),若为空就展示暂无数据
  const handleOk = async () => {
    const params = {
      id: props.menuId,
      productionLineIds: checkedKeys.value,
    };
    try {
      await postBoundProductionLine(params);
      close();
      emit('success');
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const checkNode = (keys: any, { checkedNodes }: any) => {
    checkedKeys.value = checkedNodes.reduce((prev: any, cur: any) => {
      if (!cur.roleTypeFlag) {
        prev.push(cur.id);
      }
      return prev;
    }, []);
  };
  const close = () => {
    emit('update:open', false);
  };
  const getTreeData = async () => {
    const res = await getProcessProductLine();
    const tree = getChildrenList(res.data);
    allRoles.value = tree;
    treeData.value = tree;
    const { data } = await getPlanTeamListByTeamId({ teamId: props.menuId });
    checkedKeys.value = data.map((item: any) => item.id);
  };

  const getChildrenList = (list: any) => {
    if (!list) {
      return [];
    }
    let newChildren = [] as any;
    list.map((item: any) => {
      const children = getChildrenList(item.children);
      item.name = item.code + '-' + item.name;
      if (item.infoList) {
        item.infoList.map((info: any) => {
          info.name = info.code + '-' + info.name;
        });
        item.children = [...children, ...item.infoList];
      } else {
        item.children = [...children];
      }
      newChildren.push(item);
    });
    return newChildren;
  };

  watch(
    () => props.open,
    newVal => {
      checkedKeys.value = [];
      if (newVal) {
        getTreeData();
      }
    },
    {},
  );
</script>
