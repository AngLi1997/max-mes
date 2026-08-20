import { deleteSortingCategory, enableOrDisableSortingCategory } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { playAudio } from '@/utils';
import { paginationBig } from '@/utils/paginationConfig';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { FormProps, TableActionType, TableColumn } from '@bmos/components';
import { Modal, Switch, message } from 'ant-design-vue';
import { createVNode } from 'vue';

const { hasPermission } = usePermissionStore();

export const useTable = (openModal: any) => {
  const pageRef = ref<any>(null);

  const changeStatus = async (record: any) => {
    try {
      await enableOrDisableSortingCategory({
        id: record.id,
        useFlag: record.useFlag.value,
      });
      message.success(t('操作成功'));
      await pageRef.value?.fetchData();
    } catch (error: any) {
      await pageRef.value?.fetchData();
      error.message && message.error(error.message);
    }
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('分批标识'),
      dataIndex: 'batchLog',
      width: 170,
      resizable: true,
    },
    {
      title: t('分箱标识'),
      dataIndex: 'subBoxLog',
      width: 170,
      resizable: true,
    },
    {
      title: t('效价值下限'),
      dataIndex: 'titerDown',
      width: 130,
      resizable: true,
    },
    {
      title: t('效价值上限'),
      dataIndex: 'titerUp',
      width: 130,
      resizable: true,
    },
    {
      title: t('所属类型'),
      dataIndex: 'sortingType',
      width: 160,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record.sortingTypeName}</span>;
      },
    },
    {
      title: t('描述'),
      dataIndex: 'typeDescribe',
      width: 170,
      resizable: true,
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
            disabled={!hasPermission('170110006000004')}
            unCheckedValue={0}
            onChange={() => changeStatus(record)}
          />
        );
      },
    },
    {
      title: t('语音文件'),
      dataIndex: 'voiceFile',
      width: 80,
      resizable: true,
      customRender: ({ record }) => {
        return (
          record?.voiceFile && (
            <a onClick={() => playAudio(`${window.location.origin}/${record?.voiceFile}`)}>{t('试听')}</a>
          )
        );
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 120,
      actions: ({ record }, tableAction: TableActionType) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('170110006000002'),
          onClick: () => {
            // look(record);
            openModal(record, 'edit');
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('170110006000003') && !record?.builtSystem?.value,
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
                  await deleteSortingCategory(record.id);
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
