<template>
  <PageExpandCom
    ref="pageRef"
    tableRowKey="sampleNo"
    :tableProps="{
      search: [true],
      hideRightTree: true,
      showToolBars: [true],
      rowSelections: [rowSelection],
      formProps: [formFirstProps],
      columns: [columnsFirst],
      showIndexs: [true],
      tableFields: [
        {
          default: { inspectDataStatus: segmentedValue },
        },
      ],
      rowClassNames: [
        (record: any) => {
          return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row';
        },
      ],
      rowExpandables: [
        (record: any) => {
          return record.inspectTimes?.value === InspectionCountEnum.RE_INSPECT;
        },
      ],
    }"
    :tableLoadApi="inspectSingledataList"
    :expandLoadApi="inspectSamplesingledataList"
    :expandFields="(record: any) => ({ sampleNo: record.sampleNo })"
    :expandProps="{
      rowKeys: ['id'],
      search: [false],
      hideRightTree: true,
      showHeader: [false],
      showToolBars: [false],
      columns: [columnsSecond],
      showIndexs: [true],
      rowClassNames: [
        (record: any) => {
          return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row';
        },
      ],
    }">
    <template #tableHeaderTitle0>
      <Segmented v-model:value="segmentedValue" :options="options" />
    </template>
    <template #tableHeaderToolbar0>
      <Button v-if="fileReadPermission" type="primary" :loading="readLoading" @click="openFileReadModal">
        {{ t('文件读取') }}
      </Button>
      <Button v-if="readPermission" type="primary" :loading="readLoading" @click="handleRead">
        {{ t('读取') }}
      </Button>
      <Button v-if="publishPermission" :disabled="!publishActive" type="primary" @click="openPublish">
        {{ t('发布') }}
      </Button>
      <Button v-if="checkPermission" :disabled="!checkActive" @click="openCheck">
        {{ t('核对') }}
      </Button>
      <Button v-if="batchPublishPermission" type="primary" @click="openBatchPublish">
        {{ t('批量发布') }}
      </Button>
      <Button v-if="batchCheckPermission" @click="openBatchCheck">
        {{ t('批量核对') }}
      </Button>
      <Button
        v-if="inspectItem.value === InspectionProjectEnum.ProteinElectrophoresis"
        v-hasAuth="210030010000004"
        @click="openPrint">
        {{ t('打印蛋白电泳检测报告') }}
      </Button>
    </template>
  </PageExpandCom>
  <FileRead ref="fileReadRef" @ok="updateTable" />
  <Publish v-model:modalOpen="publishModal" :inspectItem :tableData="selectedRows" @ok="updateTable" />
  <BatchPublish v-model:modalOpen="batchPublishModal" :inspectItem />

  <Check v-model:modalOpen="checkModal" :inspectItem :tableData="selectedRows" @ok="updateTable" />
  <BatchCheck v-model:modalOpen="batchCheckModal" :inspectItem />

  <Print v-model:modalOpen="printModal" @ok="updateTable" />

  <Sign ref="signRef" :signatureAction="1004" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { Recordable } from '@bmos/components';
  import { useRead, useTable } from './hooks';
  import { postInspectSingledataList, postInspectSamplesingledataList } from '@/services';
  import { InspectionResultEnum, InspectionCountEnum, InspectionProjectEnum } from '@/types';
  import { useRowSelection } from '@/hooks';
  import { message } from 'ant-design-vue';
  import FileRead from './components/FileRead.vue';
  import Publish from './components/Publish.vue';
  import BatchPublish from './components/BatchPublish.vue';
  import Check from './components/Check.vue';
  import Print from './components/Print.vue';
  import BatchCheck from './components/BatchCheck.vue';
  import { useConfig } from '@/stores';
  import { PageExpandCom } from '@/components/PageExpandCom';
  import { Sign } from '@/components/Sign';
  import { usePermissionStore } from '@/stores';

  defineOptions({
    name: 'ExecutionManagementComponent',
    inheritAttrs: false,
  });

  const props = withDefaults(
    defineProps<{
      inspectItem: Recordable;
    }>(),
    {
      inspectItem: () => ({}),
    },
  );

  const { InspectionTaskStatusDict } = getDicts();

  const inspectSingledataList = async (params: any) => {
    return postInspectSingledataList({
      ...params,
      inspectItemCode: props.inspectItem.value,
    });
  };
  const inspectSamplesingledataList = async (params: any) => {
    return postInspectSamplesingledataList({
      ...params,
      inspectItemCode: props.inspectItem.value,
    });
  };

  const { getConfigEnumsValueByParamId } = useConfig();

  const { columnsFirst, columnsSecond, pageRef, formFirstProps } = useTable({
    props,
  });

  const { selectedRows, rowSelection, clearSelect } = useRowSelection({
    hideSelectAll: false,
    getCheckboxProps: (_record: any) => {
      return {
        disabled: false,
      };
    },
  });

  // 发布激活状态
  const publishActive = computed(() => {
    return (
      selectedRows.value?.length && selectedRows.value?.every((item: any) => item.publishStatus?.value === 'TO_PUBLISH')
    );
  });
  // 核对
  const checkActive = computed(() => {
    return (
      selectedRows.value?.length && selectedRows.value?.every((item: any) => item.publishStatus?.value === 'PUBLISHED')
    );
  });

  const updateTable = () => {
    clearSelect();
    pageRef.value?.fetchData(0);
  };

  const segmentedValue = ref('');
  const options = [{ label: t('全部'), value: '' }, ...InspectionTaskStatusDict];

  // 发布弹窗
  const publishModal = ref<boolean>(false);
  const openPublish = () => {
    if (selectedRows.value.length === 0) {
      message.warning(t('请选择需要发布的数据'));
      return;
    }
    publishModal.value = true;
  };

  // 批量发布弹窗
  const batchPublishModal = ref<boolean>(false);
  const openBatchPublish = () => {
    batchPublishModal.value = true;
  };

  // 核对弹窗
  const checkModal = ref<boolean>(false);
  const openCheck = () => {
    if (selectedRows.value.length === 0) {
      message.warning(t('请选择需要核对的数据'));
      return;
    }
    checkModal.value = true;
  };

  // 批量发布弹窗
  const batchCheckModal = ref<boolean>(false);
  const openBatchCheck = () => {
    batchCheckModal.value = true;
  };

  // 打印蛋白电泳检测报告弹窗
  const printModal = ref<boolean>(false);
  const openPrint = () => {
    printModal.value = true;
  };

  const unqualifiedColor = computed(() => {
    return getConfigEnumsValueByParamId('不合格数据颜色');
  });

  const qualifiedColor = computed(() => {
    return getConfigEnumsValueByParamId('合格数据颜色');
  });

  const fileReadRef = ref<InstanceType<typeof FileRead>>();

  // 文件读取
  const openFileReadModal = () => {
    fileReadRef.value?.openModal(props.inspectItem.value);
  };

  // 读取
  const { readLoading, handleRead, signRef, signSuccess } = useRead({
    props,
    updateTable,
  });

  const { hasPermission } = usePermissionStore();

  // 文件读取权限
  const fileReadPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000006');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000006');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000006');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000006');
      default:
        return false;
    }
  });

  // 读取权限
  const readPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
        return hasPermission('210030004000001');
      case InspectionProjectEnum.ALT:
        return hasPermission('210030005000001');
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000001');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000001');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000001');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000001');
      case InspectionProjectEnum.ProteinElectrophoresis:
        return hasPermission('210030010000001');
      default:
        return false;
    }
  });
  // 发布权限
  const publishPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
        return hasPermission('210030004000002');
      case InspectionProjectEnum.ALT:
        return hasPermission('210030005000002');
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000002');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000002');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000002');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000002');
      case InspectionProjectEnum.ProteinElectrophoresis:
        return hasPermission('210030010000002');
      default:
        return false;
    }
  });
  // 核对权限
  const checkPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
        return hasPermission('210030004000003');
      case InspectionProjectEnum.ALT:
        return hasPermission('210030005000003');
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000003');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000003');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000003');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000003');
      case InspectionProjectEnum.ProteinElectrophoresis:
        return hasPermission('210030010000003');
      default:
        return false;
    }
  });
  // 批量核对权限
  const batchCheckPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
        return hasPermission('210030004000004');
      case InspectionProjectEnum.ALT:
        return hasPermission('210030005000004');
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000004');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000004');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000004');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000004');
      case InspectionProjectEnum.ProteinElectrophoresis:
        return hasPermission('210030010000005');
      default:
        return false;
    }
  });
  // 批量发布权限
  const batchPublishPermission = computed(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
        return hasPermission('210030004000005');
      case InspectionProjectEnum.ALT:
        return hasPermission('210030005000005');
      case InspectionProjectEnum.HBsAg:
        return hasPermission('210030006000005');
      case InspectionProjectEnum.AntiHCV:
        return hasPermission('210030007000005');
      case InspectionProjectEnum.HIVAgAb:
        return hasPermission('210030008000005');
      case InspectionProjectEnum.AntiTP:
        return hasPermission('210030009000005');
      case InspectionProjectEnum.ProteinElectrophoresis:
        return hasPermission('210030010000006');
      default:
        return false;
    }
  });

  provide('page', { updateTable });
</script>

<style scoped lang="less">
  :deep(.unqualified-row) {
    color: v-bind(unqualifiedColor);
  }
  :deep(.qualified-row) {
    color: v-bind(qualifiedColor);
  }
</style>
