import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (enterView: any) => {
  const { outTypeDict, qualityStatusDict } = getDicts();
  const pageRef = ref<any>(null);

  const columnsFirst: TableColumn[] = [
    {
      title: t('出库批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('出库类型'),
      dataIndex: 'type',
      width: 170,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: outTypeDict,
        },
      },
    },
    {
      title: t('质量状态'),
      dataIndex: 'qualityStatus',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.qualityStatus?.name ?? '-';
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: qualityStatusDict,
        },
      },
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('总重量'),
      dataIndex: 'weight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('异常数量'),
      dataIndex: 'errorNum',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('创建日期'),
      dataIndex: 'createTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.status?.name ?? '-';
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            // look(record);
            enterView(record);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
