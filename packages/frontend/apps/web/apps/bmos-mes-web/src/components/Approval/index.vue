<template>
  <div class="approval-flow-container">
    <Row class="flow-header">
      <Col :span="8">
        <slot name="breadcrumb">
          <Breadcrumb class="crumb">
            <breadcrumb-item class="crumb-allow-click">
              {{ t('工艺配置') }}
            </breadcrumb-item>
            <breadcrumb-item>{{ t('工序流程') }}</breadcrumb-item>
          </Breadcrumb>
        </slot>
      </Col>
      <Col :span="8" :offset="8" class="action">
        <Space :size="16">
          <Button @click="handleClickBack">{{ t('返回') }}</Button>
        </Space>
      </Col>
    </Row>
    <div class="setting">
      <Flow
        ref="flowInstance"
        :modalJson="modalJson"
        :isShowLeftToolBar="false"
        nextIcon="File"
        :showUndo="false"
        :isView="true"
        :isTransform="false"
        :showRedo="false"
        :showDelete="false"
        leftIcon="Procedure2"
        :isOptionClickNode="notAllowClick"
        class="flow"
        @nodeClick="nodeClick"
        @graphRender="graphRender" />
      <div class="approval-table">
        <div class="title">{{ t('节点信息') }}</div>
        <BMTable
          ref="tableInstance"
          :data-source="curTableData"
          :columns="columns"
          row-key="id"
          :auto-height="true"
          :autoHeightOffset="24"
          :scroll="{ x: 1380, y: 400 }"
          :showToolBar="false"
          :showRefresh="false"
          :search="false"
          :pagination="{
            pageSize: 20,
          }"></BMTable>
      </div>
    </div>
  </div>
</template>

<script setup lang="tsx">
  import { ref } from 'vue';
  import { Row, Col, Breadcrumb, BreadcrumbItem, Space, Button } from 'ant-design-vue';
  import Flow from '@/components/Flow';
  import { useTable, useAction } from './hooks';
  import { BMTable } from '@bmos/components';
  import { FlowInstanceType } from '@/components/Flow/type';
  import { t } from '@bmos/i18n';
  import { approvalProps, approvalEmits } from './types';
  import StartNode from './components/FlowNode/ApprovalStartNode.vue';
  import BasicNode from './components/FlowNode/ApprovalBasicNode.vue';
  import EndNode from './components/FlowNode/ApprovalEndNode.vue';

  const props = defineProps(approvalProps);
  const emit = defineEmits(approvalEmits);
  const flowInstance = ref<FlowInstanceType>();
  const {
    tableInstance,
    columns,
    curTableData,
    modalJson,
    nodeClick,
    graphRender,
    // @ts-ignore
  } = useTable({ props, flowInstance });

  const { handleClickBack } = useAction({ emit });

  onMounted(async () => {
    try {
      await nextTick();
      flowInstance.value?.register({
        shape: 'custom-vue-node',
        component: {
          render() {
            return <BasicNode notAllowClick={props.notAllowClick} />;
          },
        },
      });
      flowInstance.value?.register({
        shape: 'custom-vue-start-node',
        component: {
          render() {
            return <StartNode notAllowClick={props.notAllowClick} />;
          },
        },
      });
      flowInstance.value?.register({
        shape: 'custom-vue-end-node',
        component: {
          render() {
            return <EndNode notAllowClick={props.notAllowClick} />;
          },
        },
      });
    } catch (error) {}
  });
</script>

<style scoped lang="less">
  .approval-flow-container {
    width: 100%;
    height: 100%;
    position: relative;
    padding: 0;
    .flow-header {
      padding: 4px 0 var(--bmos-padding-small) 0;
      .crumb {
        line-height: 36px;
        .crumb-allow-click {
          cursor: pointer;
        }
      }
    }
  }
  .action {
    text-align: right;
  }
  .setting {
    width: 100%;
    height: calc(100% - 56px);
    background-color: var(--bmos-primary-color-white);
    padding: 5px 5px 0 5px;
    .flow {
      width: 100%;
      height: 50%;
    }
    .approval-table {
      height: 50%;
      .title {
        line-height: 40px;
        padding: 10px 16px 10px 16px;
      }
    }
  }
</style>
