<!-- 工艺进度 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnProcessProgress">
          {{ t('生产进度') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('工艺进度') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnProcessProgress">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('生产信息')"></BMTableTitle>
    <BMDescriptions :list="detailList" :column="4" :showBottomBorder="false"></BMDescriptions>
    <Flow
      ref="flowInstance"
      :modalJson="modalJson"
      :isShowLeftToolBar="false"
      :showUndo="false"
      :isView="true"
      :isTransform="false"
      :showRedo="false"
      :showDelete="false"
      leftIcon="Process2"
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
                color: '#B3CBFF',
              }" />
            <span class="title">{{ item.status }}</span>
          </div>
        </div>
      </template>
    </Flow>
  </BreadcrumbButton>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { BMTableTitle, BMDescriptions, DescriptionsItemProps } from '@bmos/components';
  import Flow from '@/components/Flow';
  import { Cell } from '@antv/x6';
  import {
    reqFlowProcedureProgress,
    reqGetDetailUsingGET,
    reqGetProcessModelUsingGET,
    reqPlanDetail,
  } from '@/services';
  import { FlowInstanceType } from '@/components/Flow/type';
  import BasicNode from './components/BasicNode.vue';
  import { BMIcons } from '@bmos/icons';
  import RunningNode from './components/RunningNode.vue';
  import CompleteNode from './components/CompleteNode.vue';
  import { StateEnum } from './enum';

  const route = useRoute();
  const router = useRouter();

  const returnProcessProgress = () => {
    router.push({
      name: 'product-progress',
    });
  };

  const isRunning = ref<Array<any>>([]);
  const isCompleted = ref<Array<any>>([]);
  const isActive = ref<Array<any>>([]);
  const isUnActive = ref<Array<any>>([]);
  const isEnd = ref<Array<any>>([]);

  const nodeClick = (cell: Cell) => {
    const curItem = [
      ...isRunning.value,
      ...isCompleted.value,
      ...isActive.value,
      ...isUnActive.value,
      ...isEnd.value,
    ].find((item): any => item.nodeId === cell.id);
    if (curItem) {
      router.push({
        name: 'product-progress-procedure-step',
        query: {
          processModelId: cell.data.processModelId,
          ...route.query,
          procedureName: cell?.data?.name,
          executionIdList: curItem.executionIdList,
          freshExecutionId: curItem.freshExecutionId,
          planId: curItem.planId,
          procedureModelId: curItem.procedureModelId,
          procedureChangeNumber: curItem.procedureChangeNumber,
          processChangeNumber: curItem.processChangeNumber,
          state: curItem.stateEnum?.value,
        },
      });
    }
  };

  const detailList = ref<DescriptionsItemProps[]>([]);
  const modalJson = ref<any[]>([]);
  const flowInstance = ref<FlowInstanceType>();

  const customList = [
    {
      icon: 'NotActivated',
      status: t('未激活'),
    },
    {
      icon: 'InProgress',
      status: t('进行中'),
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

  onMounted(async () => {
    await nextTick();
    flowInstance.value?.register({
      shape: 'custom-vue-node',
      width: 214,
      height: 52,
      component: {
        render() {
          return <BasicNode onNextClick={(cell: Cell) => nodeClick(cell)} />;
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-running-node',
      width: 214,
      height: 52,
      component: {
        render() {
          return <RunningNode onNextClick={(cell: Cell) => nodeClick(cell)} />;
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-complete-node',
      width: 214,
      height: 52,
      component: {
        render() {
          return <CompleteNode onNextClick={(cell: Cell) => nodeClick(cell)} />;
        },
      },
    });
    flowInstance.value?.register({
      shape: 'custom-end-node',
      width: 214,
      height: 52,
      component: {
        render() {
          return <CompleteNode onNextClick={(cell: Cell) => nodeClick(cell)} icon='ProgressEnd' />;
        },
      },
    });
    try {
      const { productPlanId, executeProcessInstanceId } = route.query;
      const { data: planDetail } = await reqPlanDetail(productPlanId as string);
      detailList.value = [
        {
          label: t('产品名称'),
          value: planDetail.productName as string,
        },
        {
          label: t('产品编码'),
          value: planDetail.productMergeCode as string,
        },
        {
          label: t('产品规格'),
          value: planDetail.productSpecification as string,
        },
        {
          label: t('工艺名称'),
          value: planDetail.processName as string,
        },
        {
          label: t('生产批号'),
          value: planDetail.batchNo as string,
        },
        {
          label: t('开始时间'),
          value: planDetail.startTime as string,
        },
      ];
      const { data } = await reqFlowProcedureProgress(executeProcessInstanceId as string);
      data?.forEach((item: any) => {
        if (item.stateEnum?.value === StateEnum.ACTIVE) {
          isRunning.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.COMPLETE) {
          isCompleted.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.IS_ACTIVE) {
          isActive.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.INACTIVE) {
          isUnActive.value.push(item);
        } else if (item.stateEnum?.value === StateEnum.IS_END) {
          isEnd.value.push(item);
        }
      });
      const { data: processDetail } = await reqGetDetailUsingGET({
        processId: planDetail.processId as string,
        version: planDetail.processVersion as string,
      } as any);
      const { data: modalData } = await reqGetProcessModelUsingGET({
        processModelId: processDetail.processModelId,
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
        }
        return {
          ...metaInfo,
          data: {
            ...metaInfo.data,
            processModelId: processDetail.procedures.find((procedure: any) => procedure.nodeId === item.key)
              ?.processModelId,
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
</style>
