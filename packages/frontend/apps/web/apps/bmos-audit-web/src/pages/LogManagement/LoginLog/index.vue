<template>
  <div class="container">
    <div class="table">
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        :row-selection="rowSelection"
        row-key="id"
        headerTitle=""
        :scroll="{ x: 844, y: 400 }"
        :showRefresh="false"
        :pagination="{
          pageSize: 20,
        }"
        :pageSizeChangeToFirst="true"
        :formProps="formProps"
        :show-index="true"
        @reset="reset">
        <template #toolbar>
          <Button v-hasAuth="111010001000001" @click="loginLogExport">{{ t('导出') }}</Button>
        </template>
      </BMTable>
    </div>

    <!-- 查看弹框 -->
    <Detail ref="detailRef" :rowData="rowData"></Detail>
  </div>
</template>
<script lang="tsx" setup>
  import type { DataRequestFn, FormProps, TableInstance } from '@bmos/components';
  import { BMTable, BMEllipsis, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reactive, ref, onMounted } from 'vue';
  import { getLoginLogList, LoginLogExport } from '../../../services';
  import dayjs, { Dayjs } from 'dayjs';
  import { fileStreamDownload } from '@bmos/utils';
  import { message, Button } from 'ant-design-vue';
  import Detail from './Detail/Detail.vue';
  type RangeValue = [Dayjs, Dayjs];
  const dates = ref<any>([dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')]);
  const value = ref<RangeValue>();
  const rowData = ref();
  const detailRef = ref();
  const tableInstance = ref<TableInstance>();
  const loginSelectedRows = ref<any>([]); //存登录日志多选的数据
  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      // span: 19,
    },
    // 是否展示更多
    showAdvancedButton: false,
    // 是否显示操作按钮
    showActionButtonGroup: true,
    baseColProps: {
      span: 6,
    },
    fieldMapToTime: [['chooseTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });

  // 多选
  const rowSelection = {
    onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
      loginSelectedRows.value = selectedRows;
    },
  };
  const loadData: DataRequestFn = async (params: any): Promise<any> => {
    // 默认查最近30天
    const data = {
      startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
      endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
    };
    const datas = {
      ...data,
      ...params,
    };
    const res = await getLoginLogList(datas);
    return res;
  };
  // 重置事件
  const reset = () => {
    dates.value = [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')];
  };
  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const colorList = ['#59BF78', '#FF5633'];

  const columns: TableColumn[] = [
    {
      title: t('用户账号'),
      align: 'left',
      dataIndex: 'loginName',
      width: 190,
      resizable: true,
    },
    {
      title: t('用户名称'),
      align: 'left',
      dataIndex: 'userName',
      width: 190,
      resizable: true,
    },
    {
      title: 'IP',
      align: 'left',
      dataIndex: 'ip',
      hideInSearch: true,
      width: 150,
      resizable: true,
    },
    {
      title: t('操作动作'),
      align: 'left',
      dataIndex: 'operationAction',
      hideInSearch: true,
      width: 120,
      resizable: true,
    },
    {
      title: t('操作状态'),
      align: 'left',
      dataIndex: 'operationState',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.operationState == true ? 0 : 1],
            }}></div>
          <div style={{ color: colorList[record.operationState == true ? 0 : 1] }}>
            {record.operationState == true ? t('成功') : t('失败')}
          </div>
        </div>
      ),
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
    // 只展示在搜索栏中(选择日期)
    {
      title: t('操作日期'),
      align: 'left',
      dataIndex: 'chooseTime',
      resizable: true,
      hideInTable: true,
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
            value: formModel.chooseTime || value.value,
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
      title: t('操作描述'),
      align: 'left',
      dataIndex: 'description',
      hideInSearch: true,
      width: 280,
      resizable: true,
    },

    {
      title: t('操作'),
      align: 'left',
      hideInSearch: true,
      // hideInTable: true,
      width: 120,
      resizable: true,
      fixed: 'right',
      customRender: ({ record }) => (
        <div class='action-list'>
          <Button
            v-hasAuth='111010001000002'
            style='max-width: 100px; min-width: 40px'
            type='link'
            onClick={() => {
              look(record);
            }}>
            <BMEllipsis tooltip={true} style='max-width: 100%'>
              {{
                default: () => t('查看'),
                title: () => t('查看'),
              }}
            </BMEllipsis>
          </Button>
        </div>
      ),
    },
  ];
  // 导出
  const loginLogExport = async () => {
    const loginSelectedRowsId: any = [];
    if (loginSelectedRows.value.length !== 0) {
      loginSelectedRows.value.forEach((item: any) => {
        loginSelectedRowsId.push(item.id);
      });
    }
    const res = await tableInstance.value?.queryFormRef?.getFormValues();

    try {
      const data = {
        startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
        endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
      };
      const datas = { ...data, ...res, selectIds: loginSelectedRowsId + '' };
      const res2: any = await LoginLogExport(datas);
      fileStreamDownload(res2);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查看
  const look = async (row: any) => {
    detailRef.value.openModal();
    rowData.value = row;
  };
  onMounted(() => {});
</script>
<style scoped lang="less">
  :deep(.action-list) {
    .audit-btn {
      padding-right: 12px;
      padding-left: 0;
    }
  }
  .container {
    height: 100%;
    display: flex;
    background-color: #fff;
    padding: 16px 16px 0px 16px;
    flex-direction: column;
    .table {
      flex: 1;
      overflow-y: hidden;
    }
  }

  .search {
    display: flex;
    align-items: center;
    > div {
      margin-right: 10px;
    }
  }
  :deep(.plat-table-thead .plat-table-cell-fix-left .plat-table-cell-content) {
    font-weight: 700;
  }
</style>
