import { deleteSupplier } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useTable = (openModal: any) => {
  const { supplierTypeDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('供应商名称'),
      dataIndex: 'name',
      width: 170,
      resizable: true,
    },

    {
      title: t('供应商编号'),
      dataIndex: 'code',
      width: 120,
      resizable: true,
    },
    {
      title: t('供应商类型'),
      dataIndex: 'type',
      width: 120,
      resizable: true,
      customRender: ({ record }) => {
        return record?.type?.name;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: supplierTypeDict,
        },
      },
    },
    {
      title: t('负责人'),
      dataIndex: 'principal',
      hideInSearch: true,
      width: 100,
      resizable: true,
    },
    {
      title: t('联系方式'),
      dataIndex: 'contactWay',
      hideInSearch: true,
      width: 130,
      resizable: true,
    },
    {
      title: t('地址'),
      dataIndex: 'address',
      hideInSearch: true,
      width: 180,
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
          ifShow: hasPermission('180050001000002'),
          onClick: () => {
            // look(record);
            openModal(record, 'update');
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('180050001000003'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  await deleteSupplier(record.id);
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
    showAdvancedButton: false,
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
