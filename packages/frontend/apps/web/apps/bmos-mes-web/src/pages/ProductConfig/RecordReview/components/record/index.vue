<template>
  <div class="record-review-content-container">
    <div class="record-review-detail-header">
      <Breadcrumb></Breadcrumb>
      <div class="header-operation">
        <ApprovalBtn
          :settings="settings"
          :taskId="pageParams.taskId"
          :processInstanceId="pageParams.processInstanceId"
          :deploymentId="pageParams.deploymentId"
          :nodeId="pageParams.nodeId"
          :executionId="pageParams.executionId"
          @action="() => next && next(-1)" />
        <Button @click="() => next && next(-1)">{{ t('返回') }}</Button>
      </div>
    </div>
    <div style="flex: 1" class="record-review-content">
      <ContentLayout :title="t('记录项')" class="record-content" :isIcon="false" style="width: 300px">
        <BMSearchTree
          v-model:expanded-keys="EXPANDED_KEYS"
          :showSearch="false"
          :showAllAddIcon="false"
          :showAction="false"
          :blockNode="true"
          :selected-keys="SELECTED_KEYS"
          :fieldNames="{
            title: 'name',
            key: 'itemId',
          }"
          :tree-data="TREE_DATA"
          @select="TREE_SELECT"></BMSearchTree>
      </ContentLayout>
      <div style="flex: 1">
        <Record ref="EDITOR_INSTANCE" :activeKeys="NODE_ACTIVES" @node-click="NODE_CLICK"></Record>
      </div>
      <ContentLayout :title="t('公式')" class="record-content" :isIcon="false" style="width: 300px">
        <Formula :component="CURRENT_COMPONENT" :show="true"></Formula>
      </ContentLayout>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { Record } from '@/components/Record';
  import ApprovalBtn from '@/components/Approval/components/ApprovalBtns/index.vue';
  import { t } from '@bmos/i18n';
  import Breadcrumb from '../Breadcrumb/index.vue';
  import { BMSearchTree } from '@bmos/components';
  import Formula from '@/pages/FormulaConfig/FormulaCheck/index.vue';
  import { useTree, useEDITOR, useNode } from '@/pages/FormulaConfig/hooks';
  import { ComponentNode } from '@/components/Record/NodeList/type';
  import { message } from 'ant-design-vue';
  import ContentLayout from '@/components/ContentLayout/index.vue';

  const next: Function | undefined = inject('switchGo');
  const props = withDefaults(
    defineProps<{
      component: ComponentNode;
      id: string;
      pageParams: any;
    }>(),
    {},
  );

  const node = useNode();
  const editor = useEDITOR(node, true);
  const { EDITOR_INSTANCE, NODE_CLICK, NODE_ACTIVES } = editor;
  const { SET_INST_NODE_LIST, CURRENT_COMPONENT } = node;
  const { TREE_DATA, TREE_SELECT, EXPANDED_KEYS, SELECTED_KEYS, CURRENT_NODE, GET_RECORD } = useTree(
    editor,
    node,
    false,
  );

  onMounted(() => {
    GET_RECORD(props.id);
  });

  const settings = computed(() => {
    try {
      return JSON.parse(props.pageParams.payload?.settings || {});
    } catch (error) {
      return {};
    }
  });

  watch(CURRENT_NODE, val => {
    if (val && val.componentList) {
      return SET_INST_NODE_LIST(val.componentList);
    }
    SET_INST_NODE_LIST([]);
  });

  watch(CURRENT_COMPONENT, () => {
    if (CURRENT_COMPONENT.value && !CURRENT_COMPONENT.value.formulaId) {
      message.warning(t('该组件无引用公式！'));
    }
  });
</script>

<style scoped lang="less">
  .record-review-content-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
  }
  .record-review-detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-block-end: var(--bmos-padding-small);
    .header-operation {
      display: flex;
      column-gap: var(--bmos-padding-small);
    }
  }
  .record-review-content {
    display: flex;
    flex: 1;
    overflow: auto;
  }
</style>
