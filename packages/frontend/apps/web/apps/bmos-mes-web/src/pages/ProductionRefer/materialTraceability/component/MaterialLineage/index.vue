<!-- 物料追溯-物料谱系 -->
<template>
  <div class="content_box">
    <div class="header_box">
      <Breadcrumb>
        <breadcrumb-item @click="goBack">{{ t('物料追溯') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('物料谱系') }}</breadcrumb-item>
      </Breadcrumb>
      <Button @click="goBack">{{ t('返回') }}</Button>
    </div>
    <div class="descriptions_box">
      <BMTableTitle :title="t('生产信息')"></BMTableTitle>
      <BMDescriptions :list="descData" :column="4" :showBottomBorder="false" hasTitle></BMDescriptions>
    </div>
    <div id="mindMapBox" class="content">
      <div class="tree_select_box">
        <TreeSelect
          v-model:value="selectBatch"
          :tree-data="treeData"
          style="width: 540px"
          :getPopupContainer="triggerNode => triggerNode.parentNode"
          show-search
          allow-clear
          treeNodeFilterProp="name"
          :placeholder="t('请选择物料批次')"
          :field-names="{ label: 'name', value: 'id' }"
          @select="treeSelect">
          <template #title="data">
            <div class="tree_select_item">
              <div class="tree_select_title" :style="selectBatch == data.id ? 'color: #2871FF;' : ''">
                {{ data.mergeCode }}-{{ data.materialName }}
              </div>
              <div class="tree_select_msg_box">
                <div class="tree_select_msg">{{ t('物料批号') }}: {{ data.storageMaterialBatchNo }}</div>
                <div class="tree_select_msg">{{ t('生产批号') }}: {{ data.sourceBatchNo }}</div>
              </div>
            </div>
          </template>
        </TreeSelect>
      </div>
      <MindMap v-if="mindMapConfig.node" ref="MindMapRef" :config="mindMapConfig" boxId="mindMapBox" />
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { BMDescriptions, BMTableTitle } from '@bmos/components';
  import { Breadcrumb, BreadcrumbItem, Button } from 'ant-design-vue';
  import MindMap from '@/components/MindMap/index.vue';
  const props = withDefaults(defineProps<{ rowData: any; showType: string }>(), { rowData: {} });
  const emit = defineEmits(['close', 'update:showType']);
  import { useData } from './hooks/useData';
  const { descData, mindMapConfig, selectBatch, MindMapRef, treeData, treeSelect } = useData(props);

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
    .content {
      border-top: 4px solid #f5f7fa;
      height: calc(100% - 47px - 6px - 124px);
      background-color: #fff;
      position: relative;
      .tree_select_box {
        position: absolute;
        right: 16px;
        top: 16px;
        z-index: 10;
      }
    }
    .tree_select_box {
      .tree_select_item {
        min-height: 52px;
        .tree_select_msg_box {
          color: #606266;
          font-size: 12px;
          display: flex;
          align-items: center;
          .tree_select_msg {
            width: 50%;
          }
        }
      }
    }
  }
</style>
