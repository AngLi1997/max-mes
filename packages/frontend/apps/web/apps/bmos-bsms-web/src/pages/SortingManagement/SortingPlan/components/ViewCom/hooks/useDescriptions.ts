import { t } from '@bmos/i18n';

export const useDescriptions = () => {
  const descriptionItems = reactive([
    {
      label: t('计划批号'),
      field: 'batchNo',
    },
    {
      label: t('分拣仓库'),
      field: 'warehouse',
      vIf: getWarehouseConfigByCode.value,
      render: (data: any) => {
        return data?.warehouse?.name ?? '-';
      },
    },
    {
      label: t('计划类型'),
      field: 'planType',
    },
    {
      label: t('计划描述'),
      field: 'planDescription',
    },
    {
      label: t('预计出库日期'),
      field: 'expectedDate',
      showFn: (type: number) => {
        return type === 1;
      },
    },
    {
      label: t('创建人'),
      field: 'createByName',
    },
    {
      label: t('创建日期'),
      field: 'createTime',
    },
  ]);

  return { descriptionItems };
};
