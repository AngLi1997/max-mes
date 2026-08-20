import { RemarkDetail } from '@/components/RemarkModal';
import { useConfig } from '@/stores';
import { useDict } from '@/stores/dictStore';
import { type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isNull } from '@bmos/utils';

export const useTable = () => {
  const { getDict } = useDict();
  const { getDateFormat } = useConfig();
  // 备注弹窗相关
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);

  const pageRef = ref<any>(null);
  // 物料到期预警
  const expiryColumns: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 100,
    },
    {
      title: t('物料规格'),
      dataIndex: 'specificationName',
      width: 100,
    },
    {
      title: t('库存数量'),
      dataIndex: 'inventory',
      width: 100,
    },
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      sorter: true,
      customRender: ({ record }: any) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      customRender: ({ record }: any) => getDateFormat(record.expireDate),
    },
    {
      title: t('剩余日期'),
      dataIndex: 'remainingDay',
      width: 120,
      sorter: true,
      customRender: ({ record }: any) => {
        if (!isNull(record.remainingDay)) {
          return (
            <span
              style={{
                color: '#FF0000',
              }}>
              {record.remainingDay}
            </span>
          );
        }
        return '-';
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: hasPermission('170020001000003') && record?.receiveStatus?.code == 0,
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('物料备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  // 物料最低库存预警
  const lowStockColumns: TableColumn[] = [
    {
      title: t('物料编号'),
      dataIndex: 'materialNo',
      width: 120,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
    },
    {
      title: t('物料批号'),
      dataIndex: 'batchNo',
      width: 120,
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierName',
      width: 120,
    },
    {
      title: t('物料单位'),
      dataIndex: 'unitName',
      width: 100,
    },
    {
      title: t('物料规格'),
      dataIndex: 'specificationName',
      width: 100,
    },
    {
      title: t('库存数量'),
      dataIndex: 'inventory',
      width: 100,
      customRender: ({ record }: any) => {
        if (!isNull(record.inventory)) {
          return (
            <span
              style={{
                color: '#FF0000',
              }}>
              {record.inventory}
            </span>
          );
        }
        return '-';
      },
    },
    {
      title: t('生产日期'),
      dataIndex: 'productionDate',
      width: 170,
      sorter: true,
      customRender: ({ record }: any) => getDateFormat(record.productionDate),
    },
    {
      title: t('有效日期'),
      dataIndex: 'expireDate',
      width: 170,
      sorter: true,
      customRender: ({ record }: any) => getDateFormat(record.expireDate),
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: hasPermission('170020001000003') && record?.receiveStatus?.code == 0,
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'remark',
                value: record.remark,
                label: t('物料备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];

  // 供应商到期预警
  const supplierExpiryColumns: TableColumn[] = [
    {
      title: t('供应商名称'),
      dataIndex: 'supplierName',
      width: 140,
      formItemProps: {
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'label',
            value: 'label',
          },
          request: async () => {
            return await getDict('供应商');
          },
        },
      },
    },
    {
      title: t('简称(中)'),
      dataIndex: 'cnShortName',
      width: 100,
    },
    {
      title: t('供应商编号'),
      dataIndex: 'supplierNo',
      width: 100,
    },
    {
      title: t('供应商类型'),
      dataIndex: 'supplierTypeName',
      width: 100,
    },
    {
      title: t('负责人'),
      dataIndex: 'contactPerson',
      width: 100,
    },
    {
      title: t('联系方式'),
      dataIndex: 'contactPhone',
      width: 140,
    },
    {
      title: t('地址'),
      dataIndex: 'address',
      width: 200,
    },
    {
      title: t('有效期'),
      dataIndex: 'expireDate',
      width: 160,
      sorter: true,
    },
    {
      title: t('剩余日期'),
      dataIndex: 'remainingDay',
      width: 120,
      sorter: true,
      customRender: ({ record }: any) => {
        if (!isNull(record.remainingDay)) {
          return (
            <span
              style={{
                color: '#FF0000',
              }}>
              {record.remainingDay}
            </span>
          );
        }
        return '-';
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('备注'),
          // ifShow: hasPermission('170020001000003') && record?.receiveStatus?.code == 0,
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
      ],
    },
  ];

  const setTableColumns = (columns: TableColumn[]) => {
    pageRef.value?.getTableRef(0)?.replaceColumn(columns);
  };

  const setQueryParams = (show: boolean) => {
    pageRef.value?.getQueryFormRef()?.updateSchema([
      {
        field: 'materialNo',
        vIf: show,
      },
      {
        field: 'materialName',
        vIf: show,
      },
      {
        field: 'batchNo',
        vIf: show,
      },
    ]);
    formFirstProps.value = {
      showAdvancedButton: show,
      actionColOptions: {
        span: show ? undefined : 18,
      },
    };
    // pageRef.value.getQueryFormRef().advanceState.actionSpan = show ? 6 : 18;
  };

  const formFirstProps = ref<Partial<FormProps>>({
    showAdvancedButton: true,
    schemas: [
      {
        label: t('物料编号'),
        field: 'materialNo',
        vIf: true,
        component: 'Input',
      },
      {
        label: t('物料名称'),
        field: 'materialName',
        vIf: true,
        component: 'Input',
      },
      {
        label: t('物料批号'),
        field: 'batchNo',
        vIf: true,
        component: 'Input',
      },
      {
        label: t('供应商'),
        field: 'supplierName',
        component: 'Select',
        componentProps: {
          fieldNames: {
            label: 'label',
            value: 'label',
          },
          request: async () => {
            return await getDict('供应商');
          },
        },
      },
    ],
  });

  return {
    pageRef,
    setTableColumns,
    setQueryParams,
    expiryColumns,
    lowStockColumns,
    supplierExpiryColumns,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
