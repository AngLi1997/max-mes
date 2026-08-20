import { deleteDeliveryPlan } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableActionType, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useTable = (enterDetail: any, enterEdit: any) => {
  const { auditResultDict, outTypeDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('出库批号'),
      dataIndex: 'batchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('出库仓库'),
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
      title: t('出库类型'),
      dataIndex: 'type',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.type?.name}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: outTypeDict,
        },
      },
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
    },
    {
      title: t('数量'),
      dataIndex: 'num',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('总重量'),
      dataIndex: 'weight',
      hideInSearch: true,
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outPlanDate',
      width: 170,
      sorter: true,
      resizable: true,
      formItemProps: {
        component: 'RangePicker',
        componentProps: () => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
          };
        },
      },
    },
    {
      title: t('质量状态'),
      dataIndex: 'qualityStatus',
      hideInSearch: true,
      width: 150,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.qualityStatus?.name}</span>;
      },
    },
    {
      title: t('申请状态'),
      dataIndex: 'approveStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.approveStatus?.name}</span>;
      },
    },
    {
      title: t('申请人'),
      dataIndex: 'approveBy',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('申请日期'),
      dataIndex: 'approveTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('审核状态'),
      dataIndex: 'auditStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.auditStatus?.name}</span>;
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('170100001000007'),
          onClick: () => {
            // look(record);
            enterDetail(record);
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('170100001000008') && record?.approveStatus?.value === 0,
          onClick: () => {
            // edit(record);
            enterEdit(record.id);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('170100001000009') && record?.approveStatus?.value === 0,
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deleteDeliveryPlan(record.batchNo);
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject();
                }
              },
              onCancel() { },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['outPlanDate', ['startDate', 'endDate'], 'YYYY-MM-DD']],
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
