<template>
  <BMPageComponent
    :showAllAddIcon="false"
    :showAction="false"
    :rowKeys="['processInstanceId']"
    :treeData="treeData"
    :search="[true]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 12,
        },
        fieldMapToTime: [['chooseDate', ['startTime', 'endTime'], 'YYYY-MM-DD']],
      },
    ]"
    :defaultSelectedNode="selectedData"
    :expanded-keys="expandedKey"
    :showIndexs="[true, false]"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :fieldNames="{ title: 'name', key: 'id', children: 'itemList' }"
    :treeField="{
      field: {
        id: 'id',
        categoryCode: 'categoryCode',
      },
    }"
    :requests="[loadData as any]"
    :columns="[columns as any]"
    @reset="reset"
    @tree-select="treeSelect">
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('审批流追溯')"></BMTableTitle>
    </template>
    <template #tableHeaderToolbar0="{ treeNode, instance }">
      <Button v-hasAuth="111020001000001" type="primary" @click="examineExport(treeNode, instance)">
        {{ t('导出') }}
      </Button>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { ref, onMounted, watch, reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import { GetExamineList2, GetAuditHistory, ExamineExport, reqLogExportSaveExamineTraceability } from '@/services';
  import dayjs, { Dayjs } from 'dayjs';
  import type { TableColumn } from '@bmos/components';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import { fileStreamDownload } from '@bmos/utils';
  import { Button, message } from 'ant-design-vue';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();

  type RangeValue = [Dayjs, Dayjs];
  const treeData = ref<any[]>([]);
  const dates = ref<any>([dayjs().subtract(30, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')]);
  const value = ref<RangeValue>();
  const selectTreeKey = ref<string | undefined>(undefined); //左侧树选择时候的id
  const expandedKey = ref<any>(['0']);
  const selectedData = ref<any>(); //默认选中的树节点数据
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存表格多选的数据
  const processName = ref(''); //存流程名称
  const parentId = ref(''); //存所选树节点父级节点的id
  const emit = defineEmits(['look', 'getCategoryCode']); //存分类编码传给详情页
  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList: any = {
    '4': {
      //通过
      color: '#59BF78',
    },
    '5': {
      //不通过
      color: '#FF5633',
    },
    '2': {
      //退回
      color: '#FF9A2F',
    },
    '1': {
      //审批中
      color: '#FFF3E5',
    },
  };
  const columns = ref<TableColumn[]>([
    {
      title: t('实例业务名称'),
      align: 'left',
      dataIndex: 'name',
      hideInSearch: true,
      width: 180,
      resizable: true,
    },
    {
      title: t('实例业务编号'),
      align: 'left',
      dataIndex: 'extField',
      hideInSearch: true,
      width: 180,
      resizable: true,
    },
    {
      title: t('发起日期'),
      align: 'left',
      dataIndex: 'chooseDate',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        colProps: { span: 6 },
        defaultValue: [dayjs().subtract(30, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')],
        componentProps: ({ formModel }: any) => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
            style: { width: '100%' },
            value: formModel.chooseDate || value.value,
            disabledDate: (current: Dayjs) => {
              if (!dates.value || (dates.value as any).length === 0) {
                return false;
              }
              const tooLate = dates.value[0] && dayjs(current).diff(dates.value[0], 'days') > 30;
              const tooEarly = dates.value[1] && dayjs(dates.value[1]).diff(current, 'days') > 30;
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
      title: t('流程发起时间'),
      align: 'left',
      dataIndex: 'processStartTime',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('发起人'),
      align: 'left',
      dataIndex: 'startByName',
      width: 180,
      resizable: true,
    },
    {
      title: t('流程结束时间'),
      align: 'left',
      dataIndex: 'endTime',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('结束状态'),
      align: 'left',
      dataIndex: 'processStateEnum',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.processStateEnum?.value]?.color,
            }}></div>
          <div style={{ color: colorList[record.processStateEnum?.value]?.color }}>
            {record.processStateEnum?.label}
          </div>
        </div>
      ),
    },
    {
      title: t('处理人'),
      align: 'left',
      dataIndex: 'processedBy',
      hideInTable: true, //暂时隐藏
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
          ifShow: hasPermission('111020001000002'),
          onClick: () => {
            look(record);
          },
        },
      ],
    },
  ]);

  const loadData = async (params: any) => {
    // 默认查最近30天
    const data = {
      startTime: dayjs(new Date())?.subtract(30, 'day').format('YYYY-MM-DD'),
      endTime: dayjs(new Date()).format('YYYY-MM-DD'),
    };
    const data2 = { ...data, ...params, startName: params?.startByName, startByName: undefined };
    if (!data2.categoryCode) {
      return;
    }
    return await GetAuditHistory(data2);
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
    dates.value = [dayjs().subtract(30, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')];
  };
  // 树节点之点击切换事件
  const treeSelect = (node: any, info: any) => {
    // 切换树节点时清空右边表格选中项
    operationSelectedRows.value = [];
    selectedRowKeys1.value = [];
    processName.value = info.node.name;
    selectTreeKey.value = node[0]; //默认的树节点id
    parentId.value = info.node?.parent.key; //选中的树节点的父级节点id
  };
  // 导出
  const examineExport = async (treeNode: any, instance: any) => {
    const rowIds: any = [];
    if (operationSelectedRows.value.length !== 0) {
      operationSelectedRows.value.forEach((item: any) => {
        rowIds.push(item.processInstanceId);
      });
    }
    const res: any = instance.queryFormRef?.getFormValues();
    try {
      const data = {
        startTime: dayjs(new Date())?.subtract(30, 'day').format('YYYY-MM-DD'),
        endTime: dayjs(new Date()).format('YYYY-MM-DD'),
      };
      const data2 = {
        ...data,
        ...res,
        startName: res?.startByName,
        startByName: undefined,
        categoryCode: parentId.value,
        id: selectTreeKey.value,
        instanceIdList: rowIds + '',
        name: processName.value,
      };
      await reqLogExportSaveExamineTraceability(data2);
      const res2: any = await ExamineExport(data2);
      fileStreamDownload(res2);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 递归不让选中非最下级节点
  const getDisabled = async (data: any) => {
    data.forEach((item: any) => {
      if (item.itemList?.length) {
        item.selectable = false;
        getDisabled(item.itemList);
      }
      treeData.value = data;
    });
  };
  // 获取审核流追溯左侧树
  const getTreeData = async () => {
    try {
      const res: any = await GetExamineList2({});
      treeData.value = [
        {
          name: t('全部'),
          id: '0',
          selectable: false,
          itemList: res.data,
        },
      ];
      getDisabled(treeData.value);
      selectTreeKey.value = res.data[0]?.itemList[0]?.itemList[0]?.id; //默认存的树节点id
      // 默认展开的树节点
      expandedKey.value = ['0', res.data[0].id, res.data[0]?.itemList[0]?.id];
      // 默认选中的树节点数据
      selectedData.value = res.data[0]?.itemList[0]?.itemList[0];
      // 初始化的parentId
      parentId.value = res.data[0]?.itemList[0]?.id;
      // 初始化的processName
      processName.value = res.data[0]?.itemList[0]?.itemList[0]?.name;
    } catch (error) {}
  };
  // 查看按钮(切换至详情页)
  const look = async (val: any) => {
    emit('look', val, columns.value[0].title, columns.value[1].title);
  };

  onMounted(() => {
    getTreeData();
  });
  // 监听树节点
  watch(
    () => selectTreeKey.value,
    val => {
      emit('getCategoryCode', val, parentId.value);
      switch (parentId.value) {
        case '120020001': //记录审批
          columns.value[0].title = t('记录名称');
          columns.value[1].title = t('版本号');
          break;
        case '120020002': //工艺审批
          columns.value[0].title = t('工艺名称');
          columns.value[1].title = t('版本号');
          break;
        case '120030001': //生产计划审批
          columns.value[0].title = t('产品名称');
          columns.value[1].title = t('生产批号');
          break;
        case '120040001': //批签发审批
          columns.value[0].title = t('成品名称');
          columns.value[1].title = t('生产批号');
          break;
        case '120020003': //生产BOM审批
          columns.value[0].title = t('生产BOM名称');
          columns.value[1].title = t('版本号');
          break;
        case '120020004': //操作规程审批
          columns.value[0].title = t('文件名称');
          columns.value[1].title = t('版本号');
          break;
        default:
          break;
      }
    },
  );
</script>
<style lang="less" scoped></style>
