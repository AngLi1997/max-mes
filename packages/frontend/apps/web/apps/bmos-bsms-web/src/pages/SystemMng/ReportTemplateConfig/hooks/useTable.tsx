import { deleteReportTemplate, enableOrDisableReportTemplate } from '@/services';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, TableActionType, TableColumn } from '@bmos/components';
import { Modal, Switch, message } from 'ant-design-vue';
import { createVNode } from 'vue';

export const useTable = (openModal: any, showCriteria: any) => {
  const pageRef = ref<any>(null);

  const changeStatus = async (record: any) => {
    try {
      await enableOrDisableReportTemplate({
        id: record.id,
        useFlag: record.useFlag.value,
      });
      message.success(t('操作成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      pageRef.value?.fetchData();
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('序号'),
      dataIndex: 'orderNum',
      width: 80,
      resizable: true,
    },
    {
      title: t('判断依据'),
      dataIndex: 'judgmentBasis',
      width: 260,
      resizable: true,
      customRender: ({ record }: any) => {
        return <a onClick={() => showCriteria(record)}>{record.judgmentBasis}</a>;
      },
    },
    {
      title: t('启用'),
      dataIndex: 'useFlag',
      width: 80,
      resizable: true,
      customRender: ({ record }: any) => {
        return (
          <Switch
            v-model:checked={record.useFlag.value}
            checkedValue={1}
            unCheckedValue={0}
            onChange={() => changeStatus(record)}
          />
        );
      },
    },
    {
      title: t('添加日期'),
      dataIndex: 'updateTime',
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('编辑'),
          // ifShow: hasPermission('111020001000002'),
          onClick: () => {
            // look(record);
            openModal('edit', record);
          },
        },
        {
          label: t('删除'),
          // ifShow: hasPermission('111020001000002'),
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
                  await deleteReportTemplate(record.id);
                  message.success(t('删除成功！'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                  return Promise.reject(error);
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
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
