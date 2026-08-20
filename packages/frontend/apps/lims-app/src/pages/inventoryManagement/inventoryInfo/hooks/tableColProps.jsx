import { t } from '@/utils/useBmosI18n.js';

export const tableColProps = (goodsLocation) => {
  const msg_tableColProps = [
    {
      prop: 'materialName',
      label: t('物料名称'),
      width: 350,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'mergeCode',
      label: t('物料编码'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'materialBatchNo',
      label: t('物料批号'),
      width: 400,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'materialNo',
      label: t('物料件号'),
      width: 400,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'quantity',
      label: t('物料量'),
      width: 300,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'unit',
      label: t('单位'),
      width: 150,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'ACTION',
      label: t('操作'),
      width: 120,
      thProps: {
        align: 'left',
      },
      actions: ({ row }) => {
        return [
          {
            label: t('查看详情'),
            onClick: async () => {
              const newQuery = {
                ...row,
                goodsLocation: goodsLocation.value,
              };
              const query = Object.keys(newQuery)
                .map(
                  key =>
                    `${encodeURIComponent(key)}=${encodeURIComponent(newQuery[key])}`,
                )
                .join('&');
              uni.navigateTo({
                url: `/pages/inventoryManagement/materialDetails/index?${query}`,
              });
            },
          },
        ];
      },
    },
  ];
  const statistics_tableColProps = [
    {
      prop: 'materialName',
      label: t('物料名称'),
      width: 236,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'mergeCode',
      label: t('物料编码'),
      width: 236,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'materialBatchNo',
      label: t('物料批号'),
      width: 236,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'availableQuantity',
      label: t('可用量'),
      width: 180,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return (
          <span>
            {row.availableQuantity}
            {row.unit}
          </span>
        );
      },
    },
    {
      prop: 'reserveQuantity',
      label: t('预定量'),
      width: 180,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return (
          <span>
            {row.reserveQuantity}
            {row.unit}
          </span>
        );
      },
    },
    {
      prop: 'size',
      label: t('件数'),
      width: 180,
      thProps: {
        align: 'left',
      },
    },
  ];
  return {
    msg_tableColProps,
    statistics_tableColProps,
  };
};
