import { reqPlatformUserListByMenuId } from '@/services';
import { InspectionProjectEnum } from '@/types';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import dayjs from 'dayjs';

export const useTable = () => {
  const { InspectionCountDict, InspectionProjectDict, InspectionResultDict } = getDicts();
  const { getDateFormat } = useConfig();
  const pageRef = ref<any>();
  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemName',
      fixed: 'left',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemCode',
      width: 100,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        defaultValue: InspectionProjectEnum.ProteinContent,
        componentProps: {
          options: InspectionProjectDict,
          allowClear: false,
        },
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      width: 160,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 100,
    },
    {
      title: t('检验次数'),
      dataIndex: 'inspectTimes',
      width: 100,
      customRender: ({ record }: any) => {
        return record.inspectTimes?.label ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: InspectionCountDict,
        },
      },
    },
    {
      title: t('结果值'),
      dataIndex: 'inspectValue',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'inspectResult',
      width: 100,
      customRender: ({ record }: any) => {
        return record.inspectResult?.label ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: InspectionResultDict,
        },
      },
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 100,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControlInfoStr',
      width: 100,
    },
    {
      title: t('检验人'),
      dataIndex: 'inspector',
      width: 100,
      formItemProps: {
        component: 'Select',
        componentProps: {
          request: async () => {
            try {
              const { data } = await reqPlatformUserListByMenuId('210070002');
              return data.map((userItem: any) => {
                return {
                  label: userItem.userName + '-' + userItem.loginName,
                  value: userItem.userId,
                };
              });
            } catch (error) {}
          },
        },
      },
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectTime',
      width: 140,
      sorter: true,
      customRender: ({ record }) => {
        return getDateFormat(record.inspectTime);
      },
      formItemProps: {
        component: 'RangePicker',
        defaultValue: [dayjs().format('YYYY-MM'), dayjs().format('YYYY-MM')],
        componentProps: {
          picker: 'month',
          valueFormat: 'YYYY-MM',
        },
      },
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    fieldMapToTime: [['inspectTime', ['inspectStartDate', 'inspectEndDate'], 'YYYY-MM']],
  });
  return {
    columnsFirst,
    pageRef,
    formFirstProps,
  };
};
