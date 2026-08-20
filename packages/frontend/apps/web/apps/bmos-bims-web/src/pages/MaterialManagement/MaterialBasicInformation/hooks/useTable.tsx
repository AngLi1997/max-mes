import { deleteMaterial, getSupplierSelectList } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useTable = (openModal: any) => {
  const { materialTypeDict, unitDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('物料名称'),
      dataIndex: 'name',
      width: 120,
      resizable: true,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('物料分类'),
      dataIndex: 'type',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name;
      },
      formItemProps: {
        order: 3,
        component: 'Select',
        componentProps: {
          options: materialTypeDict,
        },
      },
    },
    {
      title: t('物料编码'),
      dataIndex: 'code',
      width: 130,
      resizable: true,
      formItemProps: {
        order: 2,
      },
    },
    {
      title: t('供应商'),
      dataIndex: 'supplierId',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.supplierName;
      },
      formItemProps: {
        component: 'Select',
        order: 5,
        componentProps: {
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          request: async () => {
            const { data } = await getSupplierSelectList();
            return data;
          },
        },
      },
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.unit?.name;
      },
      formItemProps: {
        component: 'Select',
        order: 4,
        componentProps: {
          options: unitDict,
        },
      },
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      hideInSearch: true,
      width: 130,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }, { fetchData }) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('180050002000002'),
          onClick: () => {
            // look(record);
            openModal(record, 'update');
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('180050002000003'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deleteMaterial(record.id);
                  message.success(t('删除成功'));
                  fetchData();
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error.message);
                  return Promise.reject();
                }
              },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    // actionColOptions: {
    //   span: 24,
    // },
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
