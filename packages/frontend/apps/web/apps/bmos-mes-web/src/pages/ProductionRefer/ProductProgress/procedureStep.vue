<!-- 工序进度 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnProcessProgress">
          {{ t('生产进度') }}
        </breadcrumb-item>
        <breadcrumb-item @click="returnProcedure">{{ t('工艺进度') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('工序进度') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnProcedure">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('生产信息')"></BMTableTitle>
    <BMDescriptions :list="detailList" :column="4" :showBottomBorder="false"></BMDescriptions>
    <div class="setting">
      <div class="task-list">
        <div class="title">
          <span>{{ t('任务库') }}</span>
        </div>
        <div class="list">
          <TaskList :taskList="taskList" :customList="customList" @clickShowUsers="clickShowUsers" />
        </div>
      </div>
      <Flow
        ref="flowInstance"
        :modalJson="modalJson"
        :isShowLeftToolBar="false"
        :showUndo="false"
        :isView="true"
        :isTransform="false"
        :showRedo="false"
        :showDelete="false"
        leftIcon="Procedure2"
        :showNextIcon="false"
        :showSetIcon="false"
        :isOptionClickNode="true"
        :showDivider="false">
        <template #custom>
          <div class="custom">
            <div v-for="item in customList" :key="item.icon" class="custom-item">
              <BMIcons
                :icon="item.icon"
                :style="{
                  width: '20px',
                  height: '20px',
                  ...(item.color ? { color: item.color } : {}),
                }" />
              <span class="title">{{ item.status }}</span>
            </div>
          </div>
        </template>
      </Flow>
    </div>
  </BreadcrumbButton>
  <OrderModal v-model:order-modal-open="orderModalOpen" :params="orderModalParams" />
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { BMTableTitle, BMDescriptions, DescriptionsItemProps, Recordable } from '@bmos/components';
  import Flow from '@/components/Flow';
  import { reqFlowListStepProgress, reqGetProcessModelUsingGET } from '@/services';
  import { FlowInstanceType } from '@/components/Flow/type';
  import BasicNode from './components/BasicNode.vue';
  import RunningNode from './components/RunningNode.vue';
  import CompleteNode from './components/CompleteNode.vue';
  import ActivatedNode from './components/ActivatedNode.vue';
  import { BMIcons } from '@bmos/icons';
  import TaskList from './components/TaskList.vue';
  import { Cell } from '@antv/x6';
  import OrderModal from './components/OrderModal.vue';
  import { StateEnum } from './enum';

  const route = useRoute();
  const router = useRouter();

  const returnProcessProgress = () => {
    router.push({
      name: 'product-progress',
    });
  };

  const returnProcedure = () => {
    router.push({
      name: 'product-progress-procedure',
      query: {
        productPlanId: route.query.productPlanId,
        executeProcessInstanceId: route.query.executeProcessInstanceId,
        processName: route.query.processName,
      },
    });
  };

  const customList: Array<{
    icon: string;
    status: string;
    color?: string;
  }> = [
    {
      icon: 'NotActivated',
      status: t('未激活'),
    },
    {
      icon: 'Activated',
      status: t('已激活'),
      color: '#99E6FF',
    },
    {
      icon: 'InProgress',
      status: t('进行中'),
      color: '#B3CBFF',
    },
    {
      icon: 'Completed',
      status: t('已完成'),
    },
    {
      icon: 'ProgressEnd',
      status: t('已结束'),
    },
  ];

  const isRunning = ref<Array<any>>([]);
  const isCompleted = ref<Array<any>>([]);
  const isActive = ref<Array<any>>([]);
  const isEnd = ref<Array<any>>([]);

  const detailList = ref<DescriptionsItemProps[]>([]);
  const modalJson = ref<any[]>([]);
  const flowInstance = ref<FlowInstanceType>();
  const taskList = ref<any[]>([]);

  const orderModalOpen = ref<boolean>(false);
  const orderModalParams = ref<Recordable>({});
  const clickShowUsers = (node: any, type?: number) => {
    const { procedureName } = route.query;
    if (type === 1) {
      orderModalParams.value = {
        procedureName,
        name: node.name,
        params: {
          executionIdList: detail.value.executionIdList,
          planId: detail.value.planId,
          procedureStepModelId: node.procedureStepModelId,
          type: 'TASK',
        },
      };
    } else {
      const { data = {} } = node;
      const { name } = data.formData;
      const stepItem = detail.value.stepProgressList?.find((item: any) => item.nodeId === node.id);
      orderModalParams.value = {
        procedureName,
        name,
        params: {
          executionIdList: detail.value.executionIdList,
          nodeId: node.id,
          planId: detail.value.planId,
          procedureStepModelId: stepItem?.procedureStepModelId,
          type: 'STEP',
        },
      };
    }
    orderModalOpen.value = true;
  };
  const detail = ref<any>({});
  onMounted(async () => {
    await nextTick();
    flowInstance.value?.register({
      shape: 'custom-vue-node',
      component: {
        render() {
          return (
            <BasicNode
              showNext={false}
              showUsers={true}
              onClickShowUsers={(node: Cell) => {
                clickShowUsers(node);
              }}
            />
          );
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-complete-node',
      component: {
        render() {
          return (
            <CompleteNode
              showNext={false}
              showUsers={true}
              onClickShowUsers={(node: Cell) => {
                clickShowUsers(node);
              }}
            />
          );
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-end-node',
      component: {
        render() {
          return (
            <CompleteNode
              showNext={false}
              showUsers={true}
              icon='ProgressEnd'
              onClickShowUsers={(node: Cell) => {
                clickShowUsers(node);
              }}
            />
          );
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-running-node',
      component: {
        render() {
          return (
            <RunningNode
              showNext={false}
              showUsers={true}
              onClickShowUsers={(node: Cell) => {
                clickShowUsers(node);
              }}
            />
          );
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-activated-node',
      component: {
        render() {
          return (
            <ActivatedNode
              showUsers={true}
              onClickShowUsers={(node: Cell) => {
                clickShowUsers(node);
              }}
            />
          );
        },
      },
    });
    try {
      const {
        procedureName,
        processName,
        processModelId,
        executionIdList,
        freshExecutionId,
        planId,
        procedureModelId,
        procedureChangeNumber,
        processChangeNumber,
        state,
      } = route.query;
      detailList.value = [
        {
          label: t('工艺名称'),
          value: processName as string,
        },
        {
          label: t('工序名称'),
          value: procedureName as string,
        },
      ];
      const { data } = await reqFlowListStepProgress({
        executionIdList,
        freshExecutionId,
        planId,
        procedureChangeNumber,
        procedureModelId,
        processChangeNumber,
        state,
      });
      detail.value = data;
      data?.stepProgressList?.forEach((item: any) => {
        if (item.stateEnum?.value === StateEnum.ACTIVE) {
          isRunning.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.COMPLETE) {
          isCompleted.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.IS_ACTIVE) {
          isActive.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.IS_END) {
          isEnd.value.push(item);
        }
      });
      taskList.value = data?.taskProgressList || [];
      const { data: modalData } = await reqGetProcessModelUsingGET({
        processModelId: processModelId as string,
      });
      modalJson.value = JSON.parse(modalData).map((item: any) => {
        const metaInfo = JSON.parse(item.metaInfo);
        if (isRunning.value.find((runningItem): any => runningItem.nodeId === item.key)) {
          metaInfo.shape = 'custom-running-node';
        } else if (isEnd.value.find((endItem): any => endItem.nodeId === item.key)) {
          metaInfo.shape = 'custom-end-node';
        } else if (isCompleted.value.find((completedItem): any => completedItem.nodeId === item.key)) {
          metaInfo.shape = 'custom-complete-node';
          metaInfo.size = {
            ...metaInfo.size,
            height: metaInfo.size.height + 36,
            width: metaInfo.size.width > 300 ? metaInfo.size.width : 300,
          };
          metaInfo.data = {
            ...metaInfo.data,
            ...isCompleted.value.find((completedItem): any => completedItem.nodeId === item.key),
          };
        } else if (isActive.value.find((activeItem): any => activeItem.nodeId === item.key)) {
          metaInfo.shape = 'custom-activated-node';
        }
        return {
          ...metaInfo,
          data: {
            ...metaInfo.data,
          },
        };
      });
    } catch (error) {}
  });
</script>

<style lang="less" scoped>
  :deep(.custom) {
    position: absolute;
    top: 12px;
    left: 12px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: var(--bmos-padding-mini) var(--bmos-padding-small);
    border-radius: 5px;
    background: #fff;
    box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.2);
    .custom-item {
      display: flex;
      align-items: flex-start;
      margin-right: 20px;
      .title {
        margin-left: 4px;
      }
    }
  }
  .setting {
    width: 100%;
    height: calc(100% - 52px - 56px);
    background-color: var(--bmos-primary-color-white);
    padding: 5px 5px 0 5px;
    border-top: 4px solid var(--bmos-background-color);
    display: flex;
    .task-list {
      width: 300px;
      display: flex;
      flex-direction: column;
      min-width: 300px;
      .title {
        display: flex;
        justify-content: space-between;
        padding: var(--bmos-padding-mini) var(--bmos-padding-small);
        background-color: var(--bmos-primary-color-white);
        border-bottom: 1px solid var(--bmos-first-level-border-color);
        span {
          font-size: 16px;
          line-height: 20px;
        }
      }
      .list {
        flex: 1;
        overflow-x: hidden;
        overflow-y: auto;
        padding: var(--bmos-padding-mini) var(--bmos-padding-small);
      }
    }
    .flow-container {
      flex: 1;
      width: calc(100% - 300px);
    }
  }
</style>
