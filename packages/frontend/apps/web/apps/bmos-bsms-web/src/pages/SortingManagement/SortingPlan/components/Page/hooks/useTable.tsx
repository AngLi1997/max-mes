import { deleteSortingPlan } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, TableActionType, TableColumn } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { createVNode } from 'vue';

const { hasPermission } = usePermissionStore();

export const useTable = (enterDetail: any, enterEdit: any) => {
  const { warehouseDict } = getDicts();
  const pageRef = ref<any>(null);

  const type = ref(1);

  const columnsFirst = reactive<TableColumn[]>([
    {
      title: t('计划批号'),
      dataIndex: 'batchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('计划描述'),
      dataIndex: 'planDescription',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('计划类型'),
      dataIndex: 'planType',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: !getWarehouseConfigByCode.value,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.warehouse?.name}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('预计出库日期'),
      dataIndex: 'expectedDate',
      width: 150,
      hideInTable: type.value !== 1,
      hideInSearch: true,
      sorter: true,
      resizable: true,
    },
    {
      title: t('创建人'),
      dataIndex: 'createByName',
      width: 100,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('创建日期'),
      dataIndex: 'createTime',
      width: 140,
      hideInSearch: true,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 220,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('查看'),
          ifShow: hasPermission('170080002000003'),
          onClick: () => {
            // look(record);
            enterDetail(record.batchNo);
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('170080002000004'),
          onClick: () => {
            // look(record);
            enterEdit(record.batchNo);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('170080002000005'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: createVNode(ExclamationCircleOutlined),
              closable: true,
              content: '',
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: async () => {
                try {
                  await deleteSortingPlan(record.batchNo);
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ]);

  const changeType = (value: number) => {
    columnsFirst[4].hideInTable = value !== 1;
    type.value = value;
  };

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: getWarehouseConfigByCode.value ? 12 : 18,
    },
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    paginationFirst,
    changeType,
  };
};
