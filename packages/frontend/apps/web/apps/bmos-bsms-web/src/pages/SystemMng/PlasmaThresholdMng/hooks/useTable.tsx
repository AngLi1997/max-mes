import { usePermissionStore } from '@/stores/permission';
import type { FormProps, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useTable = (openModal: any) => {
  const pageRef = ref<any>(null);
  const columnsFirst: TableColumn[] = [
    {
      title: t('阈值类型'),
      dataIndex: 'thresholdType',
      width: 170,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        return <span>{record?.thresholdType?.name}</span>;
      },
    },
    {
      title: t('存储有效期(天)'),
      dataIndex: 'effectiveTime',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('提前预警天数'),
      dataIndex: 'warningTime',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('起始时间'),
      dataIndex: 'originType',
      width: 170,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originType?.name}</span>;
      },
    },
    {
      title: t('创建人'),
      dataIndex: 'createByName',
      width: 140,
      resizable: true,
    },
    {
      title: t('创建日期'),
      dataIndex: 'createTime',
      width: 140,
      resizable: true,
      formItemProps: {
        field: 'createDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('170110003000001'),
          onClick: () => {
            openModal(record, 'edit');
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    labelAlign: 'left',
    actionColOptions: {
      span: 12,
    },
    fieldMapToTime: [['createDate', ['createDateUp', 'createDateDown'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
  };
};
