import { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const columns: TableColumn[] = [
  {
    title: t('操作人'),
    dataIndex: 'createBy',
    width: 100,
    resizable: true,
  },
  {
    title: t('操作日期'),
    dataIndex: 'createTime',
    width: 150,
    resizable: true,
  },
  {
    title: t('操作事项'),
    dataIndex: 'content',
    width: 200,
    resizable: true,
  },
  {
    title: t('操作备注'),
    dataIndex: 'remark',
    width: 200,
    resizable: true,
  },
];
