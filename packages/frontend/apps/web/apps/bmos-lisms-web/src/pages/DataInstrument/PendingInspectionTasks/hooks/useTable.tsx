import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const { InspectionProjectDict } = getDicts();
  const router = useRouter();
  const pageRef = ref<any>();
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemName',
      width: 100,
    },
    {
      title: t('待检数量'),
      dataIndex: 'num',
      width: 100,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 100,
    },
    {
      title: t('接收人'),
      dataIndex: 'receiveBy',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('接收日期'),
      dataIndex: 'receiveTime',
      width: 100,
      sorter: true,
      customRender: ({ record }) => {
        return getDateFormat(record.receiveTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }: any) => [
        {
          label: t('前往页面'),
          onClick: () => {
            const item = InspectionProjectDict.find((item: any) => item.value === record.inspectItemCode);
            router.push({
              name: item?.routeName,
            });
          },
        },
      ],
    },
  ];
  return {
    columnsFirst,
    pageRef,
  };
};
