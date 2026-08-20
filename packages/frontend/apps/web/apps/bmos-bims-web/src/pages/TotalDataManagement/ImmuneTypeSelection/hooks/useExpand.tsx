import { selectImmunityType } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';

const { hasPermission } = usePermissionStore();

export const useExpand = (fetchParentData: any) => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'checkItem',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.checkItem?.name;
      },
    },
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
      hideInSearch: true,
      width: 180,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      hideInSearch: true,
      width: 170,
      resizable: true,
    },
    {
      title: t('检验结果'),
      dataIndex: 'checkResultValue',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('试剂批号'),
      dataIndex: 'reagentBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('质控品批号'),
      dataIndex: 'qualityControllerBatchNo',
      width: 190,
      resizable: true,
    },
    {
      title: t('检验人'),
      dataIndex: 'checkBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('检验日期'),
      dataIndex: 'checkDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('复核人'),
      dataIndex: 'reCheckBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('复核日期'),
      dataIndex: 'reCheckDate',
      width: 170,
      sorter: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('选择'),
          ifShow: hasPermission('180030001000001') && record?.maintainer?.value !== 1,
          onClick: () => {
            Modal.confirm({
              title: t('是否将选择这条数据?'),
              icon: h(ExclamationCircleOutlined),
              async onOk() {
                try {
                  await selectImmunityType({ id: record.id });
                  message.success(t('操作成功'));
                  fetchParentData();
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

  const fetchData = async (params: any) => {
    pageRef.value.fetchData(0, params);
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    setRef,
    fetchData,
  };
};
