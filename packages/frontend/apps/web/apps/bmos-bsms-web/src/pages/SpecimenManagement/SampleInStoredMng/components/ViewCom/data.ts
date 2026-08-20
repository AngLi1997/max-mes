import { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const columns: TableColumn[] = [
  {
    title: t('操作人'),
    dataIndex: 'createBy',
  },
  {
    title: t('操作日期'),
    dataIndex: 'createTime',
  },
  {
    title: t('操作事项'),
    dataIndex: 'content',
  },
  {
    title: t('操作备注'),
    dataIndex: 'remark',
  },
];
