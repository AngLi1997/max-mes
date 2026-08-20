import { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useDetail = () => {
  const router = useRouter();

  // 对应血浆信息
  const columns: TableColumn[] = [
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
    },
    {
      title: t('分拣前箱号'),
      dataIndex: 'primeContainerNo',
    },
    {
      title: t('分拣后箱号'),
      dataIndex: 'containerNo',
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
    },
    {
      title: t('入库批号'),
      dataIndex: 'inWarehouseBatchNo',
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 80,
      actions: ({ record }: any) => [
        {
          label: t('查看'),
          onClick: () => {
            router.push({
              name: 'plasma-inventory-inquiry-detail',
              params: { plasmaOrgNo: record.plasmaOrgNo },
            });
          },
        },
      ],
    },
  ];

  // 对应标本信息
  const columnsSecond: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'sampleNo',
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      customRender: ({ record }) => {
        return record?.sampleType?.name;
      },
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
    },
    {
      title: t('对应编号'),
      dataIndex: 'orgNo',
    },
    {
      title: t('入库日期'),
      dataIndex: 'inWarehouseDate',
    },
    {
      title: t('操作'),
      fixed: 'right',
      key: 'ACTION',
      width: 80,
      actions: ({ record }: any) => [
        {
          label: t('查看'),
          onClick: () => {
            router.push({
              name: 'sample-query-detail',
              params: { orgSampleNo: record.orgSampleNo },
            });
          },
        },
      ],
    },
  ];
  return {
    columns,
    columnsSecond,
  };
};
