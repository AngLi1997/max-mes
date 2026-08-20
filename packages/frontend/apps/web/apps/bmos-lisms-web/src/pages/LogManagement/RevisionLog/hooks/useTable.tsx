import { useDict } from '@/stores';
import { InspectionProjectEnum } from '@/types';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import dayjs from 'dayjs';

export const useTable = () => {
  const pageRef = ref<any>();
  const { decimalUnitDict, InspectionProjectDict } = getDicts();
  const { getDateFormat } = useConfig();
  const { getDict } = useDict();
  const columnsFirst: TableColumn[] = [
    {
      title: t('操作日期'),
      dataIndex: 'createTime',
      fixed: 'left',
      width: 140,
      sorter: true,
      customRender: ({ record }) => {
        return getDateFormat(record.createTime);
      },
      formItemProps: {
        order: 4,
        component: 'RangePicker',
        defaultValue: [dayjs().format('YYYY-MM'), dayjs().format('YYYY-MM')],
        componentProps: {
          picker: 'month',
          valueFormat: 'YYYY-MM',
        },
      },
    },
    {
      title: t('功能模块'),
      dataIndex: 'model',
      width: 100,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('页面'),
      dataIndex: 'inspectItemCode',
      width: 100,
      customRender: ({ record }: any) => {
        return InspectionProjectDict.find((item: any) => item.value === record.inspectItemCode)?.label ?? '-';
      },
      formItemProps: {
        order: 5,
        component: 'Select',
        defaultValue: InspectionProjectEnum.ProteinContent,
        componentProps: {
          options: InspectionProjectDict,
        },
      },
    },
    {
      title: t('修约规则'),
      dataIndex: 'roundingCode',
      width: 140,
      formItemProps: {
        order: 2,
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('修约规则');
          },
        },
      },
    },
    {
      title: t('修约规则名称'),
      dataIndex: 'roundingName',
      width: 140,
      hideInSearch: true,
    },
    {
      title: t('保留位数'),
      dataIndex: 'digits',
      width: 100,
      customRender: ({ record }: any) => {
        return record.digits?.label ?? '-';
      },
      formItemProps: {
        order: 6,
        component: 'Select',
        componentProps: {
          options: decimalUnitDict,
        },
      },
    },
    {
      title: t('字段名'),
      dataIndex: 'fieldName',
      width: 100,
      formItemProps: {
        order: 3,
      },
    },
    {
      title: t('修约数据'),
      dataIndex: 'roundingValue',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('原始数据'),
      dataIndex: 'inspectOriginValue',
      width: 100,
      hideInSearch: true,
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    fieldMapToTime: [['createTime', ['createBeginDate', 'createEndDate'], 'YYYY-MM']],
  });
  return {
    columnsFirst,
    pageRef,
    formFirstProps,
  };
};
