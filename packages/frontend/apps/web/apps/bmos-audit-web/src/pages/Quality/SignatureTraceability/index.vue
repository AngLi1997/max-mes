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
        :pagination="{
          pageSize: 20,
        }"
        :showRefresh="false"
        :formProps="formProps"
        :show-index="true"
        @reset="reset">
        <template #toolbar>
          <Button v-hasAuth="111020003000001" @click="signatureExport">{{ t('导出') }}</Button>
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
  import { reactive, ref } from 'vue';
  import { GetSignatureList, SignatureExport, getMenuTreeList } from '@/services';
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
    const res = await GetSignatureList(datas);
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
      title: t('系统名称'),
      align: 'left',
      dataIndex: 'systemCode',
      width: 190,
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 2,
        componentProps: () => ({
          request: async () => {
            try {
              const { data } = await getMenuTreeList({});
              return data.map((item: any) => {
                return {
                  label: item.name,
                  value: item.id,
                };
              });
            } catch (error) {
              return [];
            }
          },
        }),
      },
      customRender: ({ record }) => record.systemName,
    },
    {
      title: t('签名类型'),
      align: 'left',
      dataIndex: 'signatureType',
      width: 160,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.signatureType?.label;
      },
    },
    {
      title: t('签名动作'),
      align: 'left',
      dataIndex: 'signatureAction',
      width: 180,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.signatureAction?.label;
      },
    },
    {
      title: t('签名人'),
      align: 'left',
      dataIndex: 'userName',
      width: 180,
      resizable: true,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('签名对象'),
      align: 'left',
      dataIndex: 'signatureData',
      hideInSearch: true,
      width: 250,
      resizable: true,
    },
    {
      title: t('签名日期'),
      align: 'left',
      dataIndex: 'chooseTime',
      width: 200,
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
      title: t('签名时间'),
      align: 'left',
      dataIndex: 'createTime',
      width: 180,
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
      title: t('签名对象详情'),
      align: 'left',
      dataIndex: 'signatureDataDetail',
      hideInSearch: true,
      width: 250,
      resizable: true,
    },
    {
      title: t('状态'), //是否成功
      align: 'left',
      dataIndex: 'success',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.success == true ? 0 : 1],
            }}></div>
          <div style={{ color: colorList[record.success == true ? 0 : 1] }}>
            {record.success == true ? t('成功') : t('失败')}
          </div>
        </div>
      ),
    },
    {
      title: t('备注'),
      align: 'left',
      dataIndex: 'remark',
      hideInSearch: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      hideInSearch: true,
      width: 120,
      fixed: 'right',
      resizable: true,
      customRender: ({ record }) => (
        <div class='action-list'>
          <Button
            v-hasAuth='111020003000002'
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
  // 签名导出
  const signatureExport = async () => {
    const signatureSelectedRowsId: any = [];
    if (loginSelectedRows.value.length !== 0) {
      loginSelectedRows.value.forEach((item: any) => {
        signatureSelectedRowsId.push(item.id);
      });
    }
    const res = await tableInstance.value?.queryFormRef?.getFormValues();

    try {
      const data = {
        startTime: dayjs().startOf('month').format('YYYY-MM-DD'),
        endTime: dayjs().endOf('month').format('YYYY-MM-DD'),
      };
      const datas = {
        ...data,
        ...res,
        selectIds: signatureSelectedRowsId + '',
      };
      const res2: any = await SignatureExport(datas);
      fileStreamDownload(res2);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查看
  const look = async (row: any) => {
    detailRef.value.openModal();
    rowData.value = {
      ...row,
      signatureType: row.signatureType?.label || '',
      signatureAction: row.signatureAction?.label || '',
    };
  };
</script>
<style scoped lang="less">
  :deep(.action-list) {
    .audit-btn {
      padding-right: 12px;
      padding-left: 0;
    }
  }
  .container {
    display: flex;
    flex-direction: column;
    background-color: #fff;
    height: 100%;
    padding: 16px 16px 0px 16px;
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
</style>
