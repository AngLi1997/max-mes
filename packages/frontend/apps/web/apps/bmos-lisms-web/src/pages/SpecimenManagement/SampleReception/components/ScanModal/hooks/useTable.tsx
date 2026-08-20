import { StatusType } from '@/types';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMStateTag, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 170,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 150,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 130,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      sorter: true,
    },
    {
      title: t('检品状态'),
      dataIndex: 'testArticleStatus',
      width: 120,
      customRender: ({ record }: any) => {
        const status: keyof typeof StatusType = record?.testArticleStatus?.value;
        if (!status) {
          return '-';
        }
        return (
          <BMStateTag type={StatusType[status]}>
            <span>{record?.testArticleStatus?.label}</span>
            {status === 'REJECT' && <ExclamationCircleOutlined />}
          </BMStateTag>
        );
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
