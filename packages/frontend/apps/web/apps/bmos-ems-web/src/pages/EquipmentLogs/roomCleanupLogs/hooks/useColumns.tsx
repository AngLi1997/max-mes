import { datasetAllProductTree, getPlatformFactoryRoomLogExport } from '@/services';
import type { FormProps, TableColumn } from '@bmos/components';
import { fileStreamDownload } from '@bmos/utils';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
export const useColumns = ({ UseParams }: any) => {
  const { queryParams } = UseParams;
  const columns: TableColumn[] = [
    {
      title: t('房间编码'),
      dataIndex: 'roomCode',
      fixed: 'left',
      width: 140,
      resizable: true,
    },
    {
      title: t('房间名称'),
      dataIndex: 'roomName',
      fixed: 'left',
      width: 140,
      resizable: true,
    },
    {
      title: t('清场类型'),
      dataIndex: 'type',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }: any) => {
        return record?.type?.label;
      },
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 140,
      resizable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: () => ({
          showSearch: true,
          treeNodeFilterProp: 'showName',
          fieldNames: {
            label: 'showName',
            value: 'name',
          },
          request: async () => {
            const { data } = await datasetAllProductTree();
            return data;
          },
        }),
      },
    },
    {
      title: t('清场工序'),
      dataIndex: 'procedureName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('清场开始时间'),
      dataIndex: 'beginTime',
      width: 160,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('清场结束时间'),
      dataIndex: 'endTime',
      width: 160,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('清场有效期至'),
      dataIndex: 'expireTime',
      width: 160,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('清场人'),
      dataIndex: 'operator',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('复核人'),
      dataIndex: 'verifier',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('备注'),
      dataIndex: 'description',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('选择时间'),
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
  ];
  const formFirstProps = reactive<Partial<FormProps>>({
    // showAdvancedButton: false, //展示更多
    actionColOptions: {},
    baseColProps: {
      span: 6,
    },
    fieldMapToTime: [['operateTime', ['startTime', 'endTime'], 'YYYY-MM-DD']],
  });

  //导出
  const exportTable = async (params: any, type: string) => {
    const data = type === 'screen' ? params.queryFormRef?.getFormValues() : queryParams.value;
    const data2 = { ...data, all: type === 'screen' ? true : undefined };
    try {
      const res: any = await getPlatformFactoryRoomLogExport(data2);
      fileStreamDownload(res);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  return {
    columns,
    formFirstProps,
    exportTable,
  };
};
