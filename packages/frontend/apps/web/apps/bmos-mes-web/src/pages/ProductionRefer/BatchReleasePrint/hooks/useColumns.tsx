import { getPlanPageTraceable } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { TableColumn } from '@bmos/components';
import { rootKey } from '../../ProductionHistory/hooks/useTree';
import { saveOperationHistory } from '../../utils';
import { OPERATION } from '../../utils/enum';

export const useColumns = () => {
  const { hasPermission } = usePermissionStore();
  const currentRecord = ref();
  const previewStatus = ref(false);
  const historyOpen = ref<boolean>(false);
  const operation = (record: any, type: number) => {
    switch (type) {
      case 0:
        currentRecord.value = record;
        previewStatus.value = true;
        saveOperationHistory(record, OPERATION.V);
        break;
      case 1:
        currentRecord.value = record;
        historyOpen.value = true;
        break;
      default:
        break;
    }
  };
  const column: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 250,
      resizable: true,
      sorter: true,
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 250,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 250,
      actions: ({ record }) => [
        {
          label: t('关联批次'),
          ifShow: hasPermission('120050002000001'),
          onClick: () => {
            operation(record, 0);
          },
        },
        {
          label: t('操作历史'),
          ifShow: hasPermission('120050002000002'),
          onClick: () => {
            operation(record, 1);
          },
        },
      ],
    },
  ];
  const requestPage = async (param: any) => {
    const data = { ...param };
    if (data.productIds === rootKey || data.productIds === void 0) {
      delete data.productIds;
    } else {
      if (data.categoryFlag) {
        data.productCategoryId = data.productIds;
        delete data.productIds;
      } else {
        data.productIds = [data.productIds];
      }
    }
    return await getPlanPageTraceable({ ...data, finishedProduct: true });
  };
  return {
    columns: [column],
    requests: [requestPage],
    currentRecord,
    previewStatus,
    historyOpen,
  };
};
