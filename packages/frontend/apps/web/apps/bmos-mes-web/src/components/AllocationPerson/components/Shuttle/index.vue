<template>
  <div class="shuttle-container">
    <Empty v-if="treeActive.treeData.length === 0"></Empty>
    <Row v-else class="common">
      <Col :span="12" class="common">
        <Content :count="personCount" :title="t('人员列表')" :icon="false" class="left-content">
          <slot name="left">
            <BMSearchTree
              ref="searchTreeRef"
              v-bind="treeProps"
              v-model:expandedKeys="treeActive.expandedKeys"
              v-model:checkedKeys="treeActive.checkedKeys"
              :treeData="treeActive.treeData"
              checkable
              show-icon
              @check="check">
              <template #icon="{ dataRef }">
                <BMIcons v-if="dataRef.deptFlag" icon="Bag" style="width: 16px"></BMIcons>
                <TeamOutlined v-else />
              </template>
            </BMSearchTree>
          </slot>
        </Content>
      </Col>
      <Col :span="12" class="common">
        <Content
          :count="CHECK_LIST.length"
          :title="t('已选择')"
          class="right-content"
          :isView="isView"
          @icon-click="clearAll">
          <slot name="right">
            <DetailList :list="CHECK_LIST" :isView="isView" @icon-click="removeCheckNode"></DetailList>
          </slot>
        </Content>
      </Col>
    </Row>
  </div>
</template>

<script setup lang="ts">
  import Content from '../Content/index.vue';
  import DetailList from '../DetailList/index.vue';
  import { cloneDeep } from '@bmos/utils';
  import { useTree } from '../hooks/useTree';
  import { BMSearchTree } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { TeamOutlined } from '@ant-design/icons-vue';
  import { BMIcons } from '@bmos/icons';

  const props = defineProps({
    openPeople: {
      type: Boolean,
      default: false,
    },
    hasCheckPeople: {
      type: Array,
      default: () => [],
    },
    isView: {
      type: Boolean,
      default: false,
    },
  });
  const { check, treeActive, personCount, initTreeData, treeProps, CHECK_LIST, removeCheckNode, clearAll } = useTree();

  const getCheckNodes = () => {
    return cloneDeep(CHECK_LIST.value);
  };

  watch(
    () => props.openPeople,
    val => {
      if (val) {
        initTreeData(props.hasCheckPeople);
      }
    },
    { immediate: true },
  );

  defineExpose({ getCheckNodes });
</script>

<style scoped lang="less">
  .shuttle-container {
    width: 100%;
    height: 100%;
  }
  .left-content {
    border-right: 1px solid #e1e3e5;
  }
  :deep(.right-content) {
    .shuttle-content-detail {
      padding-inline: 0;
    }
  }
  :deep(.bmos-search-tree) {
    // overflow: auto;
    .plat-input-group-wrapper {
      position: sticky;
      width: 100%;
      top: 0;
      margin: 0;
      padding: 16px;
      background-color: #ffffff;
      z-index: 1;
    }
  }
</style>
