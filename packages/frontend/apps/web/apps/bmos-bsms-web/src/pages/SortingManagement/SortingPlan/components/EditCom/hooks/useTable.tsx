import { sortingPlanBatchAdd } from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { TableActionType, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';

export const useTable = (planBatchNo: string, type: 1 | 2, fetchDubData: any) => {
  const pageRef = ref<any>(null);
  // 多选
  const myselectedRows = ref<any>([]);
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
          disabled: record.batchNo == 'CKL-0513-01',
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
          myselectedRows.value = selectedRows;
        }
        // operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);

  const columnsFirst: TableColumn[] = [
    {
      title: type === 1 ? t('血浆编号') : t('标本编号'),
      dataIndex: 'itemNo',
      width: 200,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: type === 1 ? t('血浆箱号') : t('标本箱号'),
      dataIndex: 'containerNo',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('免疫类别'),
      dataIndex: 'immunityType',
      width: 170,
      hideInSearch: true,
      hideInTable: type === 2,
      resizable: true,
    },
    {
      title: t('效价'),
      dataIndex: 'titer',
      width: 80,
      sorter: true,
      hideInSearch: true,
      hideInTable: type === 2,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      hideInSearch: true,
      hideInTable: type === 2,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.bloodType?.name}</span>;
      },
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
      hideInSearch: true,
      hideInTable: type === 2,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.sex?.name}</span>;
      },
    },
    {
      title: t('重量'),
      dataIndex: 'weight',
      width: 80,
      sorter: true,
      hideInSearch: true,
      hideInTable: type === 2,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('添加'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            Modal.confirm({
              title: t('是否将这些数据加入计划?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  const data = {
                    planBatchNo: planBatchNo,
                    itemOrgNoList: [record.itemOrgNo],
                  };
                  await sortingPlanBatchAdd(data);
                  message.success(t('操作成功'));
                  fetchDubData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];

  const setRef = (el: any) => {
    pageRef.value = el;
  };

  const fetchData = async (params?: any) => {
    pageRef.value?.fetchData(0, params);
  };

  return {
    pageRef,
    myselectedRows,
    rowSelections,
    columnsFirst,
    setRef,
    fetchData,
  };
};
