import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = (openCntModal: any, enterView: any) => {
  const { warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  // const toNote = (record: any) => {
  //   router.push({
  //     name: 'view-com-detail',
  //     params: { id: record?.noteId },
  //   });
  // };

  const columnsFirst: TableColumn[] = [
    // 筛选项
    {
      title: t('来源单位'),
      dataIndex: 'originOrg',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('核查批号'),
      dataIndex: 'checkNo',
      hideInTable: true,
      formItemProps: {
        // formItemProps: {
        //   autoLink: false,
        // },
        component: 'Input',
      },
    },
    {
      title: t('检品批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInTable: true,
      formItemProps: {
        component: 'Input',
      },
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditTime',
      hideInTable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    },
    {
      title: t('报告编号'),
      dataIndex: 'reportNo',
      hideInTable: true,
      formItemProps: {
        component: 'Input',
      },
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    // 列表项
    {
      title: t('基础信息'),
      dataIndex: 'baseInfo',
      hideInSearch: true,
      children: [
        {
          title: t('报告编号'),
          dataIndex: 'reportNo',
          width: 160,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          width: 220,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('检品批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 150,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('核查批号'),
          dataIndex: 'checkNo',
          width: 150,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return <span>{record?.warehouse?.name}</span>;
          },
        },
      ].filter(item => !item.hideInTable),
    },
    {
      title: t('检疫期数据'),
      dataIndex: 'quarantineInfo',
      hideInSearch: true,
      children: [
        {
          title: t('核查份数'),
          dataIndex: 'checkNumber',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'checkWeight',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('合格率'),
          dataIndex: 'passRate',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('合格份数'),
          dataIndex: 'passNum',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.passNum ? <a onClick={() => openCntModal(record, 1)}>{record?.passNum}</a> : 0;
          },
        },
        {
          title: t('合格重量'),
          dataIndex: 'passWeight',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('不合格份数'),
          dataIndex: 'unPassNum',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.unPassNum ? <a onClick={() => openCntModal(record, 2)}>{record?.unPassNum}</a> : 0;
          },
        },
        {
          title: t('不合格重量'),
          dataIndex: 'unPassWeight',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('未通过份数'),
          dataIndex: 'unResNum',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.unResNum ? <a onClick={() => openCntModal(record, 3)}>{record?.unResNum}</a> : 0;
          },
        },
        {
          title: t('未通过重量'),
          dataIndex: 'unResWeight',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('检疫期报告'),
      dataIndex: 'quarantineReport',
      hideInSearch: true,
      children: [
        {
          title: t('报告人'),
          dataIndex: 'reportUser',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('报告日期'),
          dataIndex: 'reportTime',
          width: 180,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('送审人'),
          dataIndex: 'sendUser',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('送审日期'),
          dataIndex: 'sendTime',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditStatus',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.auditStatus?.name}</span>;
          },
        },
        {
          title: t('审核人'),
          dataIndex: 'auditUser',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditTime',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        // {
        //   title: t('放行单'),
        //   dataIndex: 'noteAuditStatus',
        //   width: 100,
        //   ellipsis: true,
        //   resizable: true,
        //   customRender: ({ record }) => {
        //     return <a onClick={() => toNote(record)}>{record?.noteAuditStatus?.name ?? '-'}</a>;
        //   },
        // },
      ],
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('详情'),
          ifShow: hasPermission('170050005000005'),
          onClick: () => {
            // look(record);
            enterView(record, 1);
          },
        },
      ],
    },
  ];

  // 选中的数据
  const operationSelectedRow = ref<any>({});

  // 单选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: true,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys?.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 100,
    labelAlign: 'left',
    fieldMapToTime: [['auditTime', ['auditBeginTime', 'auditEndTime'], 'YYYY-MM-DD']],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    rowSelections,
    operationSelectedRow,
  };
};
