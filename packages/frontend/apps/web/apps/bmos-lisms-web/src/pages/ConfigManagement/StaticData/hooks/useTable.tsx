import { useWarn } from '@/hooks';
import { deleteStaticDataConfig, postStaticDataConfigMenuTree } from '@/services';
import { useDict, usePermissionStore } from '@/stores';
import { OperationStatusMap } from '@/types';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { findItemByAttr } from '@bmos/utils';
import { message } from 'ant-design-vue';

export const useTable = ({
  addEditModalOpen,
  operationStatus,
  clearSelect,
}: {
  addEditModalOpen: Ref<boolean>;
  operationStatus: Ref<OperationStatusMap>;
  clearSelect: () => void;
}) => {
  const { hasPermission } = usePermissionStore();
  const { getDateFormat } = useConfig();
  const { warnModal } = useWarn();
  const pageRef = ref<any>();
  const { setDict } = useDict();
  const updateTableData = () => {
    clearSelect();

    pageRef.value?.fetchData(0);
  };

  const deleteConfig = async (rows: any[], treeNode: any) => {
    try {
      if (!rows.length) {
        return message.warning(t('请选择需要删除的配置'));
      }
      warnModal(t('是否删除数据?'), {
        async onOk() {
          try {
            await deleteStaticDataConfig({
              ids: rows.map(item => item.id),
              staticDataType: treeNode.menuName,
            });
            updateTableData();
            setDict(treeNode.menuName);
            return Promise.resolve();
          } catch (error: any) {
            error.message && message.error(error.message);
            return Promise.reject();
          }
        },
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('枚举值'),
      dataIndex: 'enumsValue',
      fixed: 'left',
      width: 200,
      customRender({ record }) {
        return record.generalConfigValueVO?.enumsValue;
      },
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      width: 200,
      customRender({ record }) {
        return record.generalConfigValueVO?.description;
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'updateBy',
      width: 200,
    },
    {
      title: t('更新日期'),
      dataIndex: 'updateTime',
      width: 200,
      customRender: ({ record }) => {
        return getDateFormat(record.updateTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 150,
      actions: ({ record }: any) => [
        {
          label: t('编辑'),
          ifShow: hasPermission('210080001000003'),
          onClick: () => {
            addEditModalOpen.value = true;
            operationStatus.value = OperationStatusMap.EDIT;
            firstRowData.value = record;
          },
        },
        {
          label: t('删除'),
          ifShow: hasPermission('210080001000002'),
          danger: true,
          onClick: () => {
            warnModal(t('是否删除该数据?'), {
              async onOk() {
                try {
                  const treeItem = findItemByAttr(treeData.value, 'menuIdentify', record.menuIdentify, 'childMenuList');
                  await deleteStaticDataConfig({
                    staticDataType: treeItem?.menuName,
                    ids: [record.id],
                  });
                  message.success(t('操作成功'));
                  setDict(treeItem?.menuName);
                  updateTableData();
                  return Promise.resolve();
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
  ];

  const treeData = ref<any[]>([]);
  const getTreeData = async () => {
    try {
      const { data } = await postStaticDataConfigMenuTree('1');
      treeData.value = data.map((item: any) => {
        return {
          ...item,
          disabled: true,
          childMenuList: item.childMenuList,
        };
      });
    } catch (error) {
      //
    }
  };
  onMounted(() => {
    getTreeData();
  });

  return {
    columnsFirst,
    firstRowData,
    pageRef,
    treeData,
    deleteConfig,
    updateTableData,
  };
};
