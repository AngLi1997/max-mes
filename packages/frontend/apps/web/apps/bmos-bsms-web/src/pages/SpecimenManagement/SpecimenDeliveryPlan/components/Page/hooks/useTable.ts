import { deleteSampleDeliveryPlan } from '@/services';
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
      dataIndex: 'outPlanBatchNo',
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
        return record?.warehouse?.name;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    {
      title: t('出库类别'),
      dataIndex: 'outboundType',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.outboundType?.name;
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
      dataIndex: 'number',
      hideInSearch: true,
      width: 100,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期起'),
      dataIndex: 'slurryDateUp',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('采浆日期止'),
      dataIndex: 'slurryDateDown',
      hideInSearch: true,
      width: 140,
      sorter: true,
      resizable: true,
    },
    {
      title: t('出库日期'),
      dataIndex: 'outPlanDate',
      width: 140,
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
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.qualityStatus?.name;
      },
    },
    {
      title: t('申请状态'),
      dataIndex: 'applyStatus',
      hideInSearch: true,
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.applyStatus?.name;
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
      dataIndex: 'approveDate',
      hideInSearch: true,
      width: 140,
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
        return record?.auditStatus?.name;
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
          ifShow: hasPermission('170020009000005'),
          onClick: () => {
            // look(record);
            enterDetail(record.outPlanBatchNo);
          },
        },
        {
          label: t('编辑'),
          ifShow: hasPermission('170020009000004') && record?.applyStatus?.value === 0,
          onClick: () => {
            // edit(record);
            enterEdit(record.outPlanBatchNo);
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('170020009000006'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deleteSampleDeliveryPlan(record.outPlanBatchNo);
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  // return Promise.reject();
                }
              },
              onCancel() {},
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    fieldMapToTime: [['outPlanDate', ['outPlanDateUp', 'outPlanDateDown'], 'YYYY-MM-DD']],
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
