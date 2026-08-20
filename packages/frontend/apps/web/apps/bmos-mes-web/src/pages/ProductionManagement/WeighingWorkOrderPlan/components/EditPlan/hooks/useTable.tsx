import { reqWeighingWorkOrderPlanRequirementList } from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message, Modal } from 'ant-design-vue';
import { ref } from 'vue';

export const useTable = (isEdit: Ref<boolean>, changeFn: () => void) => {
  const tableRef = ref<any>();

  const tableData = ref<any[]>([]);

  const addRequirementIds = ref<any[]>([]);
  const deleteRequirementIds = ref<any[]>([]);

  // 表格列定义
  const columns: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 130,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialMergeCode',
      width: 150,
    },
    {
      title: t('物料规格'),
      dataIndex: 'materialSpecification',
      width: 100,
    },
    {
      title: t('称量中心'),
      dataIndex: 'weighCentreName',
      width: 150,
    },
    {
      title: t('物料批号'),
      dataIndex: 'storageMaterialBatchNo',
      width: 150,
    },
    {
      title: t('需求量'),
      dataIndex: 'formulaQuantity',
      width: 100,
      customRender: ({ record }: any) => `${record.formulaQuantity}${record.unit || ''}`,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 150,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 150,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 150,
    },
    {
      title: t('计划生产时间'),
      dataIndex: 'planDate',
      width: 150,
    },
    {
      title: t('需求用途'),
      dataIndex: 'requirementUsage',
      width: 150,
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 120,
    },
    {
      title: t('操作'),
      hideInTable: !isEdit.value,
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('删除'),
          danger: true,
          // disabled: tableData.value.length <= 1,
          // code: '120030013000001',
          onClick: () => {
            if (tableData.value.length <= 1) {
              message.error(t('至少保留一个生产称量需求'));
              return;
            }
            Modal.confirm({
              title: t('提示'),
              icon: h(ExclamationCircleOutlined),
              content: t('确认要删除该需求吗？'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: () => {
                tableData.value = tableData.value.filter((item: any) => item.id !== record.id);
                if (addRequirementIds.value.includes(record.id)) {
                  addRequirementIds.value = addRequirementIds.value.filter((item: any) => item !== record.id);
                } else {
                  deleteRequirementIds.value.push(record.id);
                }
                changeFn();
              },
            });
          },
        },
      ],
    },
  ];

  const loading = ref(false);

  const loadTableData = async (id: string) => {
    try {
      loading.value = true;
      const { data } = await reqWeighingWorkOrderPlanRequirementList({ ticketId: id });
      tableData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      loading.value = false;
    }
  };

  return {
    columns,
    tableRef,
    tableData,
    loading,
    loadTableData,
    addRequirementIds,
    deleteRequirementIds,
  };
};
