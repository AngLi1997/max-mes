import { MODAL_STATUS } from '@/pages/System/dict/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Recordable, TableInstance, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

export type UseTableParams = {
  modalStatus: Ref<MODAL_STATUS>;
  dataSource: Ref<Recordable[]>;
  addDetailModalOpen: Ref<boolean>;
  isView: boolean;
  type1: string;
  deleteIds: Ref<any>;
};

export const useTable = ({ modalStatus, dataSource, addDetailModalOpen, isView, type1, deleteIds }: UseTableParams) => {
  const tableInstance = ref<TableInstance>();
  // 选择的某一行数据
  const rowData = ref<Recordable>({});

  const columns: TableColumn[] = [
    {
      title: t('数据标签'),
      dataIndex: 'dictLabel',
      fixed: 'left',
      width: 100,
      resizable: true,
    },
    {
      title: t('数据键值'),
      dataIndex: 'dictValue',
      width: 100,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      hideInTable: type1 === 'look',
      width: 30,
      actions: ({ record }) => [
        {
          label: t('编辑'),
          ifShow: !isView,
          onClick: () => {
            rowData.value = record;
            modalStatus.value = MODAL_STATUS.EDIT;
            addDetailModalOpen.value = true;
          },
        },
        {
          label: t('删除'),
          ifShow: !isView,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除该数据'),
              icon: h(ExclamationCircleOutlined),
              content: t('删除后无法恢复，是否删除？'),
              async onOk() {
                try {
                  dataSource.value = dataSource.value.filter(item => item.dictValue !== record.dictValue);
                  message.success(t('删除成功'));
                  // 暂存删除的id
                  if (record.id) {
                    deleteIds.value.push(record.id);
                  }
                  return Promise.resolve();
                } catch (error: any) {
                  message.error(error);
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

  return {
    tableInstance,
    columns,
    rowData,
  };
};
