<!-- 状态变更日志 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as any]"
    :requests="[reqStatusChangeListReq as any]"
    :columns="[columnsFirst as any]">
    <template #tableHeaderToolbar0="{ instance }: any">
      <Dropdown :trigger="['click']">
        <Button v-hasAuth="160020001000001" type="primary">
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
      <BMTableTitle :title="t('状态变更日志')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import { reqEquipmentLogStatusPage, reqEquipmentLogStatusExport } from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { FormProps } from '@bmos/components';
  import { reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import { Dropdown, Menu, MenuItem, message } from 'ant-design-vue';
  import { fileStreamDownload } from '@bmos/utils';
  import dayjs from 'dayjs';

  const pageRef = ref<any>();
  const queryParams = ref<any>(); //存查询过的参数
  const formFirstProps = reactive<Partial<FormProps>>({
    // showAdvancedButton: false, //展示更多
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 6,
    },
    fieldMapToTime: [['operateTime', ['operateBeginTime', 'operateEndTime'], 'YYYY-MM-DD']],
  });
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
      formItemProps: {
        order: 5,
      },
    },
    {
      title: t('状态名称'),
      dataIndex: 'operateName',
      width: 180,
      resizable: true,
      formItemProps: {
        order: 2,
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('校准'),
              value: 'CALIBRATION',
            },
            {
              label: t('清洁'),
              value: 'CLEAN',
            },
            {
              label: t('消毒'),
              value: 'DISINFECTION',
            },
            {
              label: t('使用'),
              value: 'OPERATE',
            },
          ],
        }),
      },
      customRender: ({ record }: any) => {
        return record?.operateName?.label;
      },
    },
    {
      title: t('变更类型'),
      dataIndex: 'changeType',
      resizable: true,
      width: 180,
      formItemProps: {
        order: 4,
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('业务流转'),
              value: 'BUSINESS',
            },
            {
              label: t('手动变更'),
              value: 'MANUAL',
            },
            {
              label: t('效期到期'),
              value: 'EXPIRE',
            },
          ],
        }),
      },
      customRender: ({ record }: any) => {
        return record?.changeType?.label;
      },
    },
    {
      title: t('变更前状态'),
      dataIndex: 'preStatusName',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.preStatusName?.label;
      },
    },
    {
      title: t('变更后状态'),
      dataIndex: 'statusName',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.statusName?.label;
      },
    },
    {
      title: t('状态效期'),
      dataIndex: 'expireDateTime',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('变更时间'),
      align: 'left',
      dataIndex: 'operateTime',
      width: 190,
      resizable: true,
      formItemProps: {
        order: 3,
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
      title: t('操作人'),
      dataIndex: 'operatorName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
  ]);
  // 获取表格数据
  const reqStatusChangeListReq = async (params: any) => {
    queryParams.value = params;
    return await reqEquipmentLogStatusPage(queryParams.value);
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
      const res: any = await reqEquipmentLogStatusExport(data2);
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };
</script>
