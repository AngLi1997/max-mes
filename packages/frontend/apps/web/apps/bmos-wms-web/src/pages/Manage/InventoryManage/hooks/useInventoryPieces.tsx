import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export type UseInventoryPiecesTableParams = {};

export const useInventoryPiecesTable = ({}: UseInventoryPiecesTableParams) => {
  const pageRef = ref<any>(null);
  const updateTable = () => {
    pageRef.value?.fetchData(0);
  };
  const columnsFirst: TableColumn[] = [
    {
      title: t('货品名称'),
      dataIndex: 'cargoName',
      fixed: 'left',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('货品编码'),
      dataIndex: 'mergeCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货品规格'),
      dataIndex: 'specification',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货品批号'),
      dataIndex: 'batchNo',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货品件号'),
      dataIndex: 'inventoryNo',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('物料量'),
      dataIndex: 'availableQuantity',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        return Number(record.availableQuantity) || Number(record.reserveQuantity);
      },
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('货位'),
      dataIndex: 'position',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
  ];

  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  });

  return {
    pageRef,
    updateTable,
    columnsFirst,
    formFirstProps,
  };
};
