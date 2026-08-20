import { usePermissionStore } from '@/stores/permission';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { hasPermission } = usePermissionStore();

export const useExpand = (enterView: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const operationSelectedRows = ref<any>([]);

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (record: any) => {
        return {
          disabled: record?.auditStatus && record?.auditStatus?.value != 2,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          operationSelectedRows.value = selectedRows;
        }
      },
    },
    null,
  ]);

  const columnsFirst: TableColumn[] = [
    {
      title: t('血浆基础信息'),
      dataIndex: 'plasmaInfo',
      children: [
        {
          title: t('血浆编号'),
          dataIndex: 'plasmaNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('血浆箱号'),
          dataIndex: 'containerNo',
          width: 170,
          resizable: true,
        },
        {
          title: t('重量'),
          dataIndex: 'weight',
          width: 100,
          sorter: true,
          resizable: true,
        },
        {
          title: t('血浆类型'),
          dataIndex: 'plasmaType',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.plasmaType?.name}</span>;
          },
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 100,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
        {
          title: t('血浆外观'),
          dataIndex: 'appearanceResult',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return (
              <span style={{ color: record?.appearanceResult?.value !== 1 ? 'red' : 'black' }}>
                {record?.appearanceResult?.name}
              </span>
            );
          },
        },
      ],
    },
    {
      title: t('不合格审核信息'),
      dataIndex: 'rejectInfo',
      hideInSearch: true,
      children: [
        {
          title: t('提交人'),
          dataIndex: 'applyByName',
          width: 100,
          resizable: true,
        },
        {
          title: t('提交日期'),
          dataIndex: 'applyDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
        {
          title: t('待审血浆外观'),
          dataIndex: 'applyAppearance',
          width: 150,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.applyAppearance?.name}</span>;
          },
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditStatus',
          width: 100,
          resizable: true,
          customRender: ({ record }) => {
            return <span>{record?.auditStatus?.name}</span>;
          },
        },
        // {
        //   title: t('审核结果'),
        //   dataIndex: 'rejectResult',
        //   width: 120,
        //   resizable: true,
        // },
        {
          title: t('审核人'),
          dataIndex: 'auditByName',
          width: 100,
          resizable: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditDate',
          width: 140,
          sorter: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170040006000004'),
          onClick: () => {
            enterView(record);
          },
        },
      ],
    },
  ];

  // const formFirstProps: Partial<FormProps> = {
  //   showAdvancedButton: true,
  // };

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
    rowSelections,
    operationSelectedRows,
  };
};
