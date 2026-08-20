import { RemarkDetail } from '@/components/RemarkModal';
import { useDict, usePermissionStore } from '@/stores';
import { OperationStatusMap } from '@/types';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (openModal: Function, deleteSupplier: Function) => {
  const { materialTypeDict } = getDicts();
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
      resizable: true,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 120,
      resizable: true,
    },
    {
      title: t('简称(英)'),
      dataIndex: 'enShortName',
      width: 100,
      resizable: true,
    },
    {
      title: t('物料类型'),
      dataIndex: 'materialType',
      width: 120,
      resizable: true,
      customRender: ({ record }) => record?.materialType?.label ?? '-',
    },
    {
      title: t('关键物料品类'),
      dataIndex: 'keyMaterialCategory',
      width: 120,
      resizable: true,
      customRender: ({ record }) => record?.keyMaterialCategory?.label ?? '-',
    },
    {
      title: t('关键物料类型'),
      dataIndex: 'keyMaterialTypeName',
      width: 120,
      resizable: true,
    },
    {
      title: t('适用检验项目'),
      dataIndex: 'applyItemStr',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 140,
      resizable: true,
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 140,
      resizable: true,
    },
    {
      title: t('最低库存量'),
      dataIndex: 'minInventory',
      width: 100,
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
                label: t('物料备注'),
                field: 'remark',
                value: record.remark,
              },
            ];
            remarkModalOpen.value = true;
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('210060002000003'),
          onClick: () => {
            openModal(OperationStatusMap.EDIT, record);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('210060002000002'),
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
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
      },
      {
        label: t('物料名称'),
        field: 'materialName',
        component: 'Input',
      },
      {
        label: t('简称(英)'),
        field: 'enShortName',
        component: 'Input',
      },

      {
        label: t('物料类型'),
        field: 'materialType',
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
        },
      },
      {
        label: t('关键物料类型'),
        field: 'keyMaterialTypeId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('关键物料类型');
          },
        },
      },
      {
        label: t('供应商'),
        field: 'supplierIdentify',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('供应商');
          },
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
