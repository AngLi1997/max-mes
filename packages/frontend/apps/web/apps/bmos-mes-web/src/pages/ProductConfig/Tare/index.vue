<!-- 皮重管理 -->
<template>
  <BMPageComponent
    ref="tableInstance"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :requests="[loadData as any]"
    :columns="[columns]"
    :formProps="[
      {
        actionColOptions: {
          span: 12,
        },
        showAdvancedButton: false,
      },
      {},
    ]">
    <template #tableHeaderToolbar0>
      <!-- 新增编辑-弹框 -->
      <TareModal
        ref="TareModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></TareModal>
      <!-- 打印标签弹框 -->
      <BMPrint
        v-model:open="printOpen"
        :getPrinter="reqGetPrintEquipment"
        sceneId="120006001"
        @printConfirm="printConfirm"></BMPrint>
      <Button v-hasAuth="120020015000005" @click="print">
        {{ t('标签打印') }}
      </Button>
      <Button v-hasAuth="120020015000001" type="primary" @click="handleTare({}, 'add')">
        {{ t('新增') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import { ref } from 'vue';
  import { message, Button, Modal } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import type { TableColumn } from '@bmos/components';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { BMPageComponent, BMPrint } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';
  import TareModal from './components/TareModal.vue';
  import {
    reqTareWeighConfigPage, //获取表格数据
    reqTareWeighConfigDelete, //删除皮重
    reqGetPrintEquipment, //获取打印机
    postTagInstancePrintBatch, //打印接口
  } from '@/services';
  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<any>();
  const TareModalRef = ref<any>();
  const rowId = ref<any>();
  const formData = ref<any>();
  const type = ref<string>('');
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存多选的数据
  const printOpen = ref<any>(false);
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
  // 表格项
  const columns: TableColumn[] = [
    {
      title: t('重量'),
      dataIndex: 'tareWeigh',
      resizable: true,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('描述信息'),
      dataIndex: 'describeInfo',
      resizable: true,
    },
    {
      title: t('修订人'),
      dataIndex: 'operator',
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => record.editorName + '-' + record.editorLoginName,
    },
    {
      title: t('修订时间'),
      dataIndex: 'editTime',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 200,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }, tableAction) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('120020015000002'),
          onClick: () => {
            handleTare(record, 'edit');
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120020015000003'),
          onClick: () => {
            handleTare(record, 'look');
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('120020015000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该数据'),
              onOk: async () => {
                try {
                  await reqTareWeighConfigDelete({ id: record.id });
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
          },
        },
      ],
    },
  ];
  // 获取表格数据
  const loadData = async (params: any) => {
    return await reqTareWeighConfigPage(params as any);
  };
  // 新增编辑
  const handleTare = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    TareModalRef.value.openModal();
  };
  // 打印标签
  const print = () => {
    if (operationSelectedRows.value.length === 0) return message.error(t('请先选择皮重'));
    printOpen.value = true;
  };
  // 确认打印
  const printConfirm = async (printerParams: any) => {
    try {
      const { printerIp, printerPort, printerDpi, sceneId } = printerParams;
      const batchParams = operationSelectedRows.value.map((item: any) => {
        return {
          printerIp,
          printerPort,
          dpi: printerDpi,
          sceneId,
          body: {
            id: item?.id,
          },
        };
      });
      await postTagInstancePrintBatch(batchParams);
      message.success(t('打印成功'));
      printOpen.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  //刷新列表
  const updateTable = () => {
    tableInstance.value?.fetchData();
  };
</script>

<style scoped></style>
