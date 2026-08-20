<template>
  <BMPageComponent
    ref="tableInstance"
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['id']"
    :treeData="treeData"
    :search="[true]"
    :formProps="[formFirstProps]"
    :defaultSelectedNode="treeData[0]"
    :expanded-keys="firstTreeKey"
    :showIndexs="[true, false]"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :pageSizeChangeToFirsts="[true]"
    :fieldNames="{
      title: 'name',
      key: 'id',
    }"
    :treeField="{
      field: {
        menuId: 'id',
      },
    }"
    :requests="[loadData as any]"
    :columns="[columnsFirst]"
    @tree-select="treeSelect"
    @reset="reset">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('操作日志')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0="{ treeNode, instance }">
      <Button v-hasAuth="111010002000001" type="primary" @click="operationLogExport(treeNode, instance)">
        {{ t('导出') }}
      </Button>
    </template>
  </BMPageComponent>
  <!-- 查看弹框 -->
  <Detail ref="detailRef" :rowData="rowData" :treeNodeId="treeNodeId"></Detail>
</template>

<script lang="tsx" setup>
  import { ref, onMounted, reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import {
    getMenuTreeList,
    getPlatformOperationLogpage,
    // getMesOperationLogpage,
    // getLimsOperationLogpage,
    // getWmsOperationLogpage,
    // getBsmsOperationLogpage,
    // getBimsOperationLogpage,
    // getLismsOperationLogpage,
    reqLogExportSave,
    OperationLogPlatformExport,
    OperationLogMesExport,
    OperationLogLimsExport,
    OperationLogWmsExport,
    OperationLogBsmsExport,
    OperationLogBimsExport,
    OperationLogLismsExport,
  } from '@/services';
  import dayjs, { Dayjs } from 'dayjs';
  import { DataNode } from 'ant-design-vue/es/tree';
  import type { TableColumn } from '@bmos/components';
  import { BMPageComponent, BMTableTitle, TableInstance } from '@bmos/components';
  import Detail from './components/Detail.vue';
  import { fileStreamDownload } from '@bmos/utils';
  import { Button, message } from 'ant-design-vue';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  type RangeValue = [Dayjs, Dayjs];
  const dates = ref<any>([dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')]);
  const value = ref<RangeValue>();
  const detailRef = ref();
  const rowData = ref();
  const firstTreeKey = ref<any>([]);
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存操作日志多选的数据
  const tableInstance = ref<TableInstance>();
  const treeNodeId = ref<any>();
  const formFirstProps = reactive<any>({
    showAdvancedButton: false,
    fieldMapToTime: [['chooseTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });

  const columnsFirst: TableColumn[] = [
    {
      title: t('操作类型'),
      align: 'left',
      dataIndex: 'operationType',
      width: 120,
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 1,
        componentProps: () => ({
          options: [
            {
              label: t('新增'),
              value: 0,
            },
            {
              label: t('编辑'),
              value: 1,
            },
            {
              label: t('删除'),
              value: 2,
            },
            {
              label: t('导出'),
              value: 3,
            },
            {
              label: t('关联'),
              value: 4,
            },
            {
              label: t('审核'),
              value: 5,
            },
          ],
        }),
      },
      customRender: ({ record }: any) => {
        return record?.operationType?.label;
      },
    },
    {
      title: t('业务操作'),
      align: 'left',
      dataIndex: 'operationBusiness',
      width: 190,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('菜单名称'),
      align: 'left',
      dataIndex: 'menuId',
      width: 190,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => t(record.menuId) ?? '-',
    },
    {
      title: t('操作人'),
      align: 'left',
      dataIndex: 'userName',
      width: 190,
      resizable: true,
      formItemProps: {
        order: 0,
        labelWidth: 60,
      },
      customRender: ({ record }) => (record?.loginName ? record.userName + '-' + record?.loginName : record.userName),
    },
    {
      title: t('操作时间'),
      align: 'left',
      dataIndex: 'operationTime',
      width: 200,
      resizable: true,
      hideInSearch: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          showTime: true,
        },
      },
    },
    {
      title: t('操作日期'),
      align: 'left',
      dataIndex: 'chooseTime',
      width: 190,
      hideInTable: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        colProps: { span: 6 },
        defaultValue: [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')],
        componentProps: ({ formModel }: any) => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
            style: { width: '100%' },
            value: formModel.chooseTime || value.value, //暂不生效
            disabledDate: (current: Dayjs) => {
              if (!dates.value || (dates.value as any).length === 0) {
                return false;
              }
              const tooLate = dates.value[0] && dayjs(current).diff(dates.value[0], 'days') > 30;
              const tooEarly = dates.value[0]
                ? dates.value[1] && dayjs(dates.value[1]).diff(current, 'days') > 30
                : dayjs(dates.value).startOf('month') > current;
              return tooEarly || tooLate;
            },
            onChange: (val: RangeValue) => {
              value.value = val;
            },
            onCalendarChange: (val: RangeValue) => {
              dates.value = val;
            },
          };
        },
      },
    },
    {
      title: t('备注'),
      align: 'left',
      dataIndex: 'remark',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看'),
          ifShow: hasPermission('111010002000002'),
          onClick: () => {
            look(record);
          },
        },
      ],
    },
  ];

  const loadData = async (params: any) => {
    // 默认查最近30天
    const data = {
      startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
      endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
    };
    const data2 = {
      ...data,
      ...params,
    };
    if (!params.menuId || params.menuId === 'all') {
      return;
    }
    const res = await getPlatformOperationLogpage(data2);
    return res;
    // 单独处理app中查设备相关操作日志
    // if (data2.menuId === '121030' || data2.menuId === '121030001') {
    // }
    // let res1;
    // switch (data2.menuId.slice(0, 3)) {
    //   case '120':
    //     res1 = await getMesOperationLogpage(data2);
    //     break;
    //   case '121':
    //     res1 = await getMesOperationLogpage(data2);
    //     break;
    //   case '130':
    //     res1 = await getLimsOperationLogpage(data2);
    //     break;
    //   case '150':
    //     res1 = await getWmsOperationLogpage(data2);
    //     break;
    //   case '170':
    //     res1 = await getBsmsOperationLogpage(data2);
    //     break;
    //   case '180':
    //     res1 = await getBimsOperationLogpage(data2);
    //     break;
    //   case '210':
    //     res1 = await getLismsOperationLogpage(data2);
    //     break;
    //   default:
    //     res1 = await getPlatformOperationLogpage(data2); //走默认调平台的菜单
    //     break;
    // }
    // return res1;
  };
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
  const reset = () => {
    dates.value = [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')];
  };
  // 树节点之点击切换事件
  const treeSelect = (node: any) => {
    // 切换树节点时清空右边表格选中项
    operationSelectedRows.value = [];
    selectedRowKeys1.value = [];
    treeNodeId.value = node[0];
  };
  // 导出
  const operationLogExport = async (treeNode: any, instance: any) => {
    const operationSelectedRowsId: any = [];
    if (operationSelectedRows.value.length !== 0) {
      operationSelectedRows.value.forEach((item: any) => {
        operationSelectedRowsId.push(item.id);
      });
    }
    // 获取搜索项表单数据
    const res = instance.queryFormRef?.getFormValues();
    try {
      const data = {
        startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
        endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
      };
      const data2 = {
        ...data,
        ...res,
        menuId: treeNode.id,
        selectIds: operationSelectedRowsId + '',
      };
      await reqLogExportSave(data2);
      let res1: any;
      switch (treeNode.id.slice(0, 3)) {
        case '100':
          res1 = await OperationLogPlatformExport(data2);
          break;
        case '111':
          res1 = await OperationLogPlatformExport(data2);
          break;
        case '120':
          res1 = await OperationLogMesExport(data2);
          break;
        case '121':
          res1 = await OperationLogMesExport(data2);
          break;
        case '130':
          res1 = await OperationLogLimsExport(data2);
          break;
        case '150':
          res1 = await OperationLogWmsExport(data2);
          break;
        case '170':
          res1 = await OperationLogBsmsExport(data2);
          break;
        case '180':
          res1 = await OperationLogBimsExport(data2);
          break;
        case '210':
          res1 = await OperationLogLismsExport(data2);
          break;
        default: //走默认调平台的导出
          res1 = await OperationLogPlatformExport(data2);
          break;
      }
      fileStreamDownload(res1);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const treeData = ref<DataNode[]>([]);
  // 获取菜单树
  const getMenuList = async () => {
    try {
      const res: any = await getMenuTreeList({});
      treeData.value = res.data;
      // 默认展开树节点
      firstTreeKey.value = [res.data[0].id];
    } catch (error) {}
  };
  // 查看
  const look = async (row: any) => {
    detailRef.value.openModal();
    rowData.value = row;
  };

  onMounted(() => {
    getMenuList();
  });
</script>
<style lang="less" scoped></style>
