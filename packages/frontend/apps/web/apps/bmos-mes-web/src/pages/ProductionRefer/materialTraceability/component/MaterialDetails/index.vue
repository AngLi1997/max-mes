<!-- 物料追溯-物料详情 -->
<template>
  <div class="content_box">
    <div class="header_box">
      <Breadcrumb>
        <breadcrumb-item @click="goBack">{{ t('物料追溯') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('物料详情') }}</breadcrumb-item>
      </Breadcrumb>
      <Button @click="goBack">{{ t('返回') }}</Button>
    </div>
    <div class="descriptions_box">
      <BMTableTitle :title="t('生产信息')"></BMTableTitle>
      <BMDescriptions :list="descData" :column="4" :showBottomBorder="false" hasTitle></BMDescriptions>
    </div>
    <div class="content_title">
      <BMTableTitle :title="t('物料追溯')" style="padding: 8px 16px"></BMTableTitle>
      <div class="segmented_box">
        <Segmented v-model:value="segmentedValue" :options="segmentedData" block @change="segmentedChange">
          <template #label="{ title }">
            <div>{{ title }}</div>
          </template>
        </Segmented>
      </div>
    </div>
    <div v-if="segmentedValue == 'tree'" id="mindMapBox" class="content content_table">
      <div class="tree_box">
        <BMSearchTree
          v-if="showTree"
          ref="searchTreeRef"
          v-model:selectedKeys="selectedKeys"
          :expandedKeys="null"
          :showAllAddIcon="false"
          :showAddChildren="false"
          :showDeleteNode="false"
          :showAction="false"
          :defaultExpandAll="defaultExpandAllValue"
          :fieldNames="{
            title: 'fullName',
            key: 'id',
          }"
          :treeData="treeData ? [treeData] : []"
          @select="treeSelect">
          <template #title="data">
            <div v-if="data.isUnMatched" class="is-unMatched"></div>
            <treeItem v-else :data="data" :selectedKeys="selectedKeys" />
          </template>
        </BMSearchTree>
      </div>
      <div class="msg_box">
        <msgTableBox v-if="treeSelectData" type="" :treeSelectData="treeSelectData" />
      </div>
    </div>
    <div v-else id="mindMapBox" class="content content_map">
      <MindMap ref="MindMapRef" :config="mindMapConfig" boxId="mindMapBox" />
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { BMDescriptions, BMTableTitle, BMSearchTree } from '@bmos/components';
  import { Breadcrumb, BreadcrumbItem, Button, Segmented } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { useData } from './hooks/useData';
  import MindMap from '@/components/MindMap/index.vue';
  import msgTableBox from '../msgTableBox/index.vue';
  import treeItem from './component/treeItem.vue';

  const props = withDefaults(defineProps<{ rowData: any; showType: string }>(), { rowData: {} });
  const emit = defineEmits(['close', 'update:showType']);
  const {
    descData,
    mindMapConfig,
    MindMapRef,
    treeSelect,
    selectedKeys,
    treeData,
    segmentedValue,
    segmentedData,
    treeSelectData,
    defaultExpandAllValue,
    segmentedChange,
    showTree,
  } = useData(props);
  const goBack = () => {
    emit('update:showType', 'page');
  };
</script>
<style scoped lang="less">
  .content_box {
    :deep(.mes-descriptions) {
      padding: 0;
    }
    :deep(.mes-tree-treenode) {
      border-bottom: 1px solid #e1e3e5;
    }
    height: 100%;
    .header_box {
      height: 47px;
      margin-bottom: 6px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .descriptions_box {
      background-color: #fff;
      padding: 6px 16px;
    }
    .content_title {
      border-top: 4px solid #f5f7fa;
      height: 54px;
      background-color: #fff;
      box-sizing: border-box;
      border-bottom: 1px solid #e1e3e5;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-right: 16px;
      .segmented_box {
        width: 132px;
      }
    }
    .content {
      height: calc(100% - 47px - 6px - 124px - 54px);
      background-color: #fff;
    }
    .content_table {
      width: 100%;
      display: flex;
      justify-content: space-between;
      :deep(.mes-tabs-nav) {
        margin: 0;
      }
      .tree_box {
        width: 50%;
        box-sizing: border-box;
        border-right: 2px solid #f5f7fa;
      }
      .msg_box {
        width: 50%;
        padding: 0 16px;
      }
    }
  }
</style>
