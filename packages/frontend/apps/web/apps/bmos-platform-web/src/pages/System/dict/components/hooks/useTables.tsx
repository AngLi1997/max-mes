import { reqPlatformDictDeletePOST, reqPlatformDictDetailDeletePOST } from '@/api';
import { usePermissionStore } from '@/stores/permission';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { Recordable, TableColumn } from '@bmos/components';
import { TableInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Modal, message } from 'ant-design-vue';
import { MODAL_STATUS } from '../../types';

export type UseTableParams = {
  emits: any;
};

export const useTables = ({ emits }: UseTableParams) => {
  const { hasPermission } = usePermissionStore();

  const tableInstance = ref<TableInstance>();

  // 第二个表格
  const addDetailModalOpen = ref<boolean>(false);
  const modalStatus = ref<MODAL_STATUS>(MODAL_STATUS.ADD);
  const rowData = ref<Recordable>({});

  const columns: TableColumn[][] = [
    [
      {
        title: t('字典名称'),
        dataIndex: 'dictName',
        fixed: 'left',
        width: 100,
        resizable: true,
      },
      {
        title: t('字典编码'),
        dataIndex: 'dictCode',
        width: 100,
        resizable: true,
      },
      {
        title: t('操作'),
        align: 'left',
        key: 'ACTION',
        fixed: 'right',
        width: 60,
        actions: ({ record }) => [
          {
            label: t('编辑'),
            ifShow: !record.state && hasPermission('100020009000007'),
            onClick: () => {
              // 跳转形式
              emits('eidtOrLook', record, 'edit');
            },
          },
          {
            label: t('查看'),
            ifShow: hasPermission('100020009000002'),
            onClick: () => {
              // 跳转形式
              emits('eidtOrLook', record, 'look');
            },
          },
          {
            label: t('删除'),
            ifShow: !record.state && hasPermission('100020009000008'),
            onClick: () => {
              Modal.confirm({
                title: t('是否删除该字典'),
                icon: h(ExclamationCircleOutlined),
                content: t('字典删除后无法恢复，是否删除？'),
                async onOk() {
                  try {
                    await reqPlatformDictDeletePOST(record.id);
                    message.success(t('删除成功'));
                    tableInstance.value?.fetchData(0);
                    tableInstance.value?.fetchData(1);
                    emits('delete', record, 'delete');
                    return Promise.resolve();
                  } catch (error: any) {
                    message.error(error.message);
                    return Promise.reject();
                  }
                },
                onCancel() { },
              });
            },
          },
        ],
      },
    ],
    [
      {
        title: t('数据标签'),
        dataIndex: 'dictLabel',
        fixed: 'left',
        width: 100,
        resizable: true,
        headerSearchComponent: 'Input',
      },
      {
        title: t('数据键值'),
        dataIndex: 'dictValue',
        width: 100,
        resizable: true,
        headerSearchComponent: 'Input',
      },
      {
        title: t('操作'),
        align: 'left',
        key: 'ACTION',
        fixed: 'right',
        width: 30,
        actions: ({ record }) => [
          {
            label: t('编辑'),
            ifShow: hasPermission('100020009000004'),
            onClick: () => {
              rowData.value = record;
              addDetailModalOpen.value = true;
              modalStatus.value = MODAL_STATUS.EDIT;
            },
          },
          {
            label: t('查看'),
            ifShow: hasPermission('100020009000005'),
            onClick: () => {
              rowData.value = record;
              addDetailModalOpen.value = true;
              modalStatus.value = MODAL_STATUS.VIEW;
            },
          },
          {
            label: t('删除'),
            ifShow: hasPermission('100020009000006'),
            onClick: () => {
              Modal.confirm({
                title: t('是否删除该字典数据'),
                icon: h(ExclamationCircleOutlined),
                content: t('字典数据删除后无法恢复，是否删除？'),
                async onOk() {
                  try {
                    await reqPlatformDictDetailDeletePOST(record.id);
                    message.success(t('删除成功'));
                    tableInstance.value?.fetchData(1);
                    return Promise.resolve();
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
    ],
  ];
  const titles = [t('字典信息'), t('数据列表')];

  return {
    columns,
    titles,
    tableInstance,
    addDetailModalOpen,
    modalStatus,
    rowData,
  };
};
