import { RemarkDetail } from '@/components/RemarkModal';
import { useDict, usePermissionStore } from '@/stores';
import { type FormProps, type Recordable, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = (openModal: Function) => {
  const { getDict } = useDict();
  const { hasPermission } = usePermissionStore();
  const { materialTypeDict } = getDicts();
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
      width: 220,
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
          label: t('物料接收'),
          ifShow: hasPermission('210060003000001'),
          onClick: () => {
            openModal(record);
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
        label: t('供应商'),
        field: 'supplierIdentify',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('供应商');
          },
        },
      },
      {
        label: t('物料名称'),
        field: 'materialName',
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
        label: t('物料编号'),
        field: 'materialNo',
        component: 'Input',
      },
      {
        label: t('物料单位'),
        field: 'unitId',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('物料单位');
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
