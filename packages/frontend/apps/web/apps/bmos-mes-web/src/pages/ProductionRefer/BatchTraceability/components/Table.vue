<template>
  <div class="container">
    <BMTable
      :key="tableKey"
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      :scroll="{ x: 844, y: 400 }"
      :extraParams="{
        type,
      }"
      :showSearchBorder="false"
      :search="false"></BMTable>

    <InspectionDetailsModal
      ref="InspectionDetailsModalRef"
      :inspectionRowData="inspectionRowData"></InspectionDetailsModal>
  </div>
</template>
<script lang="tsx" setup>
  import type { DataRequestFn } from '@bmos/components';
  import { BMTable, BMStateTag } from '@bmos/components';
  import InspectionDetailsModal from './InspectionDetailsModal.vue';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { ref, onMounted } from 'vue';
  import {
    getPlanRetraceExecuteTracePage,
    getPlanRetraceMaterialTracePage,
    getPlanRetraceEquipmentTracePage,
    getPlanRetraceRoomTracePage,
    getInspectPage,
    getInspectProgramResult,
    getPlanRetraceDeviationTracePage,
  } from '@/services';

  const tableInstance = ref<any>();
  const statusList = ref<any>(['primary', 'warning', 'success']);
  const InspectionDetailsModalRef = ref<any>();

  const props = defineProps({
    activeKey: {
      type: String || undefined,
      default: '1',
    },
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const type = computed(() => {
    return props.activeKey;
  });
  const inspectionRowData = ref<any>();
  const tableKey = ref<any>(0);
  const loadData: DataRequestFn = async (params: any) => {
    const { type, ...newParams } = params;
    const data = { ...newParams, planId: props.rowData.id };
    switch (type) {
      case '1':
        return getPlanRetraceExecuteTracePage(data);
      case '2':
        return getPlanRetraceMaterialTracePage(data);
      case '3':
        return getPlanRetraceEquipmentTracePage({
          ...newParams,
          batchNo: props.rowData.batchNo,
          orderBy: 'begin_time',
        });
      case '4':
        return getPlanRetraceRoomTracePage(data);
      case '5':
        return getInspectPage(data);

      case '6':
        return getPlanRetraceDeviationTracePage(data);
      default:
        break;
    }
  };
  const columnsAll = ref<any>([
    // 执行信息
    [
      {
        title: t('工序节点'),
        align: 'left',
        dataIndex: 'procedureName',
        width: 190,
        resizable: true,
      },
      {
        title: t('步骤/任务'),
        align: 'left',
        dataIndex: 'procedureStepName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('工艺班次'),
        align: 'left',
        dataIndex: 'processChangeNum',
        hideInSearch: true,
        width: 190,
        resizable: true,
        customRender: ({ record }: any) => {
          return <div>{Number(record.processChangeNum) + 1 || '-'}</div>;
        },
      },
      {
        title: t('工序班次'),
        align: 'left',
        dataIndex: 'procedureStepNum',
        hideInSearch: true,
        width: 190,
        resizable: true,
        customRender: ({ record }: any) => {
          return <div>{Number(record.procedureStepNum) + 1 || '-'}</div>;
        },
      },
      {
        title: t('开始时间'),
        align: 'left',
        dataIndex: 'procedureStepStartTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('完成时间'),
        align: 'left',
        dataIndex: 'procedureStepEndTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('完成人'),
        align: 'left',
        dataIndex: 'completer',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
    ],
    // 物料追溯
    [
      {
        title: t('操作时间'),
        align: 'left',
        dataIndex: 'operationTime',
        width: 190,
        resizable: true,
      },
      {
        title: t('操作类型'),
        align: 'left',
        dataIndex: 'operationType',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('操作人员'),
        align: 'left',
        dataIndex: 'operateUserName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料名称'),
        align: 'left',
        dataIndex: 'materialName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料编码'),
        align: 'left',
        dataIndex: 'materialCode',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料批号'),
        align: 'left',
        dataIndex: 'storageMaterialBatchNo',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料件号'),
        align: 'left',
        dataIndex: 'storageMaterialNo',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料量'),
        align: 'left',
        dataIndex: 'materialQuantity',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('单位'),
        align: 'left',
        dataIndex: 'unitName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
    ],
    // 设备使用
    [
      {
        title: t('设备名称'),
        align: 'left',
        dataIndex: 'equipmentName',
        width: 190,
        resizable: true,
      },
      {
        title: t('设备编号'),
        align: 'left',
        dataIndex: 'equipmentCode',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('操作内容'),
        align: 'left',
        dataIndex: 'operateContent',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('记录方式'),
        align: 'left',
        dataIndex: 'changeType',
        hideInSearch: true,
        width: 190,
        resizable: true,
        customRender: ({ record }: any) => {
          return <div>{record.changeType == '1' ? t('自动记录') : record.changeType == '0' ? t('手动记录') : '-'}</div>;
        },
      },
      {
        title: t('使用开始时间'),
        align: 'left',
        dataIndex: 'beginTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('使用结束时间'),
        align: 'left',
        dataIndex: 'endTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('操作人'),
        align: 'left',
        dataIndex: 'endOperatorName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('复核人'),
        align: 'left',
        dataIndex: 'reviewerName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('操作时间'),
        align: 'left',
        dataIndex: 'createTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
    ],
    // 房间清场信息
    [
      {
        title: t('房间编码'),
        align: 'left',
        dataIndex: 'roomCode',
        width: 190,
        resizable: true,
      },
      {
        title: t('房间名称'),
        align: 'left',
        dataIndex: 'roomName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('清场类型'),
        align: 'left',
        dataIndex: 'operationType',
        hideInSearch: true,
        width: 190,
        resizable: true,
        customRender: ({ record }: any) => {
          return record?.operationType?.label;
        },
      },
      {
        title: t('清场工序'),
        align: 'left',
        dataIndex: 'procedureName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('清场开始时间'),
        align: 'left',
        dataIndex: 'cleanStartTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('清场结束时间'),
        align: 'left',
        dataIndex: 'cleanEndTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('清场有效期至'),
        align: 'left',
        dataIndex: 'validTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('清场人'),
        align: 'left',
        dataIndex: 'operator',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('复核人'),
        align: 'left',
        dataIndex: 'verifier',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('备注'),
        align: 'left',
        dataIndex: 'desc',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
    ],
    //检验信息
    [
      {
        title: t('请验单号'),
        align: 'left',
        dataIndex: 'inspectNo',
        width: 190,
        resizable: true,
      },
      {
        title: t('请验人'),
        align: 'left',
        dataIndex: 'inspector',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('请验时间'),
        align: 'left',
        dataIndex: 'inspectTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料名称'),
        align: 'left',
        dataIndex: 'materialName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料编码'),
        align: 'left',
        dataIndex: 'materialMergeCode',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('物料批号'),
        align: 'left',
        dataIndex: 'materialBatchNo',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('检验结果'),
        fixed: 'right',
        hideInSearch: true,
        width: 100,
        resizable: true,
        key: 'ACTION',
        actions: ({ record }: any) => [
          {
            label: t('查看'),
            onClick: async () => {
              try {
                const { data } = await getInspectProgramResult({ id: record.id });
                inspectionRowData.value = data;
                InspectionDetailsModalRef.value.openModal();
              } catch (error: any) {
                message.error(error.message);
              }
            },
          },
        ],
      },
    ],
    // 异常信息
    [
      {
        title: t('工序名称'),
        align: 'left',
        dataIndex: 'procedureName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('工序步骤/任务名称'),
        align: 'left',
        dataIndex: 'procedureStepName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('异常类型'),
        align: 'left',
        dataIndex: 'exceptionType',
        width: 190,
        resizable: true,
      },
      {
        title: t('异常描述'),
        align: 'left',
        dataIndex: 'exceptionDescription',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('异常状态'),
        dataIndex: 'exceptionStatus',
        width: 100,
        resizable: true,
        hideInSearch: true,
        customRender: ({ record }: any) => (
          <BMStateTag type={statusList.value[record.exceptionStatus?.value]}>{record.exceptionStatus?.name}</BMStateTag>
        ),
      },
      {
        title: t('记录方式'),
        align: 'left',
        dataIndex: 'exceptionStatus',
        hideInSearch: true,
        width: 190,
        resizable: true,
        customRender: ({ record }: any) => {
          return record?.exceptionStatus?.name;
        },
      },
      {
        title: t('记录人'),
        align: 'left',
        dataIndex: 'recordUserName',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('记录时间'),
        align: 'left',
        dataIndex: 'recordTime',
        hideInSearch: true,
        width: 190,
        resizable: true,
      },
      {
        title: t('处理结果'),
        dataIndex: 'handleResult',
        width: 300,
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('处理人'),
        dataIndex: 'handleUserName',
        width: 100,
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('处理时间'),
        dataIndex: 'handleTime',
        width: 200,
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('作废原因'),
        dataIndex: 'cancelReason',
        width: 300,
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('作废人'),
        dataIndex: 'cancelUserName',
        width: 100,
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('作废时间'),
        dataIndex: 'cancelTime',
        width: 200,
        resizable: true,
        hideInSearch: true,
      },
    ],
  ]);
  const columns = ref<any>(columnsAll.value[0]);

  watch(
    () => props.activeKey,
    val => {
      console.log(val, 'tab的值');
      switch (val) {
        case '1':
          columns.value = columnsAll.value[0];
          break;
        case '2':
          columns.value = columnsAll.value[1];
          break;
        case '3':
          columns.value = columnsAll.value[2];
          break;
        case '4':
          columns.value = columnsAll.value[3];
          break;
        case '5':
          columns.value = columnsAll.value[4];
          break;
        case '6':
          columns.value = columnsAll.value[5];
          break;
        default:
          break;
      }
      tableKey.value++;
    },
  );

  onMounted(() => {});
</script>
<style scoped lang="less">
  .container {
    height: 100%;
    :deep(.bmos-table .bmos-tool-bar) {
      display: none;
    }
  }
</style>
