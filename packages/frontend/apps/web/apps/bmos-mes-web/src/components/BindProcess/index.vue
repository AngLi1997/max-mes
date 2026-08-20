<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('绑定工艺')"
    :submit="okModal"
    destroyOnClose
    wrap-class-name="modalSizeMedium">
    <BMSearchTree
      :expanded-keys="expandedKeys"
      :showAllAddIcon="false"
      :showAction="false"
      :tree-data="treeData"
      :checkable="true"
      :checked-keys="curCheckedKeys"
      :fieldNames="{ title: 'showName', key: 'id' }"
      @check="check"></BMSearchTree>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { NormalModalForm, BMSearchTree } from '@bmos/components';
  import { getProcessListTreeReq } from '@/services';
  import { DataNode } from 'ant-design-vue/es/tree';

  const emits = defineEmits(['update:permissionOpen', 'ok']);
  const open = defineModel<boolean>('processOpen', {
    default: false,
  });
  const props = defineProps({
    // 选中的数据
    checkIds: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    extraParams: {
      type: Object as PropType<Record<string, any>>,
      default: () => ({}),
    },
    // 保存接口
    saveApi: {
      type: Function as PropType<(params: Record<string, any>) => Promise<any>>,
      default: () => () => Promise.resolve(),
    },
  });

  const curCheckedKeys = ref<string[] | { checked: string[]; halfChecked: string[] }>([]);
  const curCheckedNodes = ref<DataNode[]>([]);
  const expandedKeys = ref<string[]>(['all']);

  const check = (keys: any, e: any) => {
    curCheckedNodes.value = e.checkedNodes;
    curCheckedKeys.value = keys;
  };

  const okModal = async () => {
    try {
      const result: string[] = [];
      // 获取选中的工艺
      curCheckedNodes.value.forEach(item => {
        if (item.isFlag) {
          result.push(item.id);
        }
      });
      await props.saveApi({ ...props.extraParams, processIds: result });
      message.success(t('绑定成功'));
      emits('ok', result);
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    }
  };

  const treeData = ref<DataNode[]>([]);
  const getTreeData = async () => {
    try {
      // 获取工艺树
      const { data } = await getProcessListTreeReq();
      treeData.value = [
        {
          showName: t('全部'),
          id: 'all',
          key: 'all',
          children: data,
        },
      ];
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  watch(
    () => open.value,
    val => {
      if (val) {
        getTreeData();
        curCheckedKeys.value = props.checkIds;
      }
    },
  );
</script>

<style lang="less"></style>
