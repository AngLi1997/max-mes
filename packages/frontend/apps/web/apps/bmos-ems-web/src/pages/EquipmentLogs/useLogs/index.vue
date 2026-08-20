<!-- 设备使用日志 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as any]"
    :requests="[reqUseLogsListReq as any]"
    :columns="[columnsFirst as any]">
    <template #tableHeaderToolbar0="{ instance }:any">
      <Dropdown :trigger="['click']">
        <Button v-hasAuth="160020002000001" type="primary">
          {{ t('导出') }}
        </Button>
        <template #overlay>
          <Menu>
            <MenuItem key="1" @click="export1(instance, 'screen')">{{ t('导出筛选数据') }}</MenuItem>
            <MenuItem key="2" @click="export1(instance, 'currentPage')">{{ t('导出当前页数据') }}</MenuItem>
          </Menu>
        </template>
      </Dropdown>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('设备使用日志')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { reqEquipmentLogOperatePage, reqEquipmentLogOperateExport } from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { FormProps } from '@bmos/components';
  import { reactive, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { Dropdown, Menu, MenuItem, message } from 'ant-design-vue';
  import { fileStreamDownload } from '@bmos/utils';
  import dayjs from 'dayjs';
  // 获取路由上的 query 参数
  const route = useRoute();
  const pageRef = ref<any>();
  const queryParams = ref<any>(); //存查询过的参数
  const formFirstProps = reactive<Partial<FormProps>>({
    // showAdvancedButton: false, //展示更多
    actionColOptions: {},
    baseColProps: {
      span: 6,
    },
    fieldMapToTime: [['operateTime', ['operateBeginTime', 'operateEndTime'], 'YYYY-MM-DD']],
  });
  const changeTypeOptions = [
    {
      label: t('手动变更'),
      value: 'MANUAL',
    },
    {
      label: t('业务流转'),
      value: 'BUSINESS',
    },
    {
      label: t('效期到期'),
      value: 'EXPIRE',
    },
  ];
  const columnsFirst = ref<any>([
    {
      title: t('设备名称'),
      dataIndex: 'equipmentName',
      fixed: 'left',
      width: 180,
      resizable: true,
    },
    {
      title: t('设备编号'),
      dataIndex: 'equipmentCode',
      width: 180,
      resizable: true,
    },
    {
      title: t('操作内容'),
      dataIndex: 'operateContent',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('记录方式'),
      dataIndex: 'changeType',
      resizable: true,
      width: 150,
      formItemProps: {
        order: 4,
        component: 'Select',
        componentProps: () => ({
          options: changeTypeOptions,
        }),
      },
      customRender: ({ record }: any) => {
        return changeTypeOptions.find(item => item.value === record.changeType)?.label ?? '-';
      },
    },
    {
      title: t('操作时间'),
      align: 'left',
      dataIndex: 'operateTime',
      width: 180,
      resizable: true,
      hideInTable: true,
      formItemProps: {
        colProps: { span: 6 },
        component: 'RangePicker',
        defaultValue: [dayjs().subtract(29, 'day').format('YYYY-MM-DD'), dayjs().format('YYYY-MM-DD')],
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    },
    {
      title: t('设备地点'),
      dataIndex: 'position',
      width: 150,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      resizable: true,
      width: 160,
    },
    {
      title: t('使用开始时间'),
      dataIndex: 'beginTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('开始操作人'),
      dataIndex: 'beginOperatorName',
      width: 170,
      resizable: true,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: t('使用结束时间'),
      dataIndex: 'endTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作人'),
      dataIndex: 'endOperatorName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('复核人'),
      dataIndex: 'reviewerName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作时间'),
      dataIndex: 'createTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
  ]);
  // 获取表格数据
  const reqUseLogsListReq = async (params: any) => {
    queryParams.value = params;
    return await reqEquipmentLogOperatePage(queryParams.value);
    // return new Promise(resolve => {
    //   setTimeout(() => {
    //     resolve({
    //       data: [
    //         {
    //           id: '1',
    //           deviceName: '血浆袋清洗剂',
    //           deviceCode: '0801001',
    //           position: '融浆间',
    //           batchNo: '人凝血因子VIII',
    //           productsName: '产品名称1',
    //           beginTime: '2022-02-20 12:21:12',
    //           beginOperator: '张三-zhangsan',
    //           endTime: '2022-02-21 12:21:12',
    //           endOperator: '李四-lisi',
    //         },
    //       ],
    //     });
    //   }, 500);
    // });
  };
  // 截取
  const getContentBetweenChars = (str: any) => {
    return decodeURI(str.match(/filename=(\S*).xlsx/)[1]);
  };
  // 导出筛选数据/导出当前页数据
  const export1 = async (instance: any, type: any) => {
    const data = type === 'screen' ? instance.queryFormRef?.getFormValues() : queryParams.value;
    const data2 = { ...data, all: type === 'screen' ? true : undefined };
    try {
      const res: any = await reqEquipmentLogOperateExport(data2);
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {
    if (JSON.stringify(route.query) !== '{}') {
      pageRef.value?.getQueryFormRef(0).setFieldsValue({
        equipmentName: route.query?.name,
        equipmentCode: route.query?.code,
        position: route.query?.position,
      });
    }
  });
</script>
