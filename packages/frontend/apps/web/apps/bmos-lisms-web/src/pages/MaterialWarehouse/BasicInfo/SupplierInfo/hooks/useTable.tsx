import { RemarkDetail } from '@/components/RemarkModal';
import { useDict, usePermissionStore } from '@/stores';

import { OperationStatusMap } from '@/types';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (openModal: Function, deleteSupplier: Function) => {
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const { yesOrNoDict } = getDicts();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('供应商名称'),
      dataIndex: 'supplierName',
      width: 170,
      resizable: true,
    },
    {
      title: t('简称(中)'),
      dataIndex: 'cnShortName',
      width: 120,
      resizable: true,
    },
    {
      title: t('简称(英)'),
      dataIndex: 'enShortName',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商编号'),
      dataIndex: 'supplierNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商类型'),
      dataIndex: 'supplierTypeName',
      width: 120,
      resizable: true,
    },
    {
      title: t('负责人'),
      dataIndex: 'contactPerson',
      width: 120,
      resizable: true,
    },
    {
      title: t('联系方式'),
      dataIndex: 'contactPhone',
      width: 160,
      resizable: true,
    },
    {
      title: t('地址'),
      dataIndex: 'address',
      width: 200,
      resizable: true,
    },
    {
      title: t('审计要求'),
      dataIndex: 'requireAudit',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.requireAuditEnum?.label ?? '-',
    },
    {
      title: t('有效期'),
      dataIndex: 'expireDate',
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      width: 170,
      sorter: true,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.updateTime),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 160,
      actions: ({ record }) => [
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('供应商备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('210060001000003'),
          onClick: () => {
            openModal(OperationStatusMap.EDIT, record);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('210060001000002'),
          danger: true,
          onClick: () => {
            deleteSupplier([record]);
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelAlign: 'left',
    labelWidth: 100,
    schemas: [
      {
        label: t('供应商名称'),
        field: 'supplierName',
        component: 'Input',
      },
      {
        label: t('简称(英)'),
        field: 'enShortName',
        component: 'Input',
      },
      {
        label: t('供应商编号'),
        field: 'supplierNo',
        component: 'Input',
      },
      {
        label: t('供应商类型'),
        field: 'supplierTypeId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('供应商类型');
          },
        },
      },
      {
        label: t('审计要求'),
        field: 'requireAudit',
        component: 'Select',
        componentProps: {
          options: yesOrNoDict,
        },
      },
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
