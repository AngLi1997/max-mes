import { reqPlatformUserListByMenuId } from '@/services';
import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import dayjs from 'dayjs';

export const useTable = () => {
  const { auditResultDict, pageDict } = getDicts();

  const { getDateFormat } = useConfig();
  const pageRef = ref<any>();
  const columnsFirst: TableColumn[] = [
    {
      title: t('功能模块'),
      dataIndex: 'auditMode',
      fixed: 'left',
      width: 100,
      formItemProps: {
        order: 1,
      },
    },
    {
      title: t('页面'),
      dataIndex: 'auditType',
      width: 100,
      formItemProps: {
        order: 2,
        component: 'Select',
        defaultValue: 'SAMPLE_RECEIVE',
        componentProps: {
          options: pageDict,
        },
      },
    },
    {
      title: t('审核结果'),
      dataIndex: 'auditResult',
      width: 100,
      customRender: ({ record }: any) => {
        return record.auditResult?.label ?? '-';
      },
      formItemProps: {
        order: 5,
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
    },
    {
      title: t('操作人'),
      dataIndex: 'auditBy',
      width: 100,
      formItemProps: {
        order: 4,
        component: 'Select',
        componentProps: {
          request: async () => {
            try {
              const { data } = await reqPlatformUserListByMenuId('210070001');
              return data.map((userItem: any) => {
                return {
                  label: userItem.userName + '-' + userItem.loginName,
                  value: userItem.userId,
                };
              });
            } catch (error) {}
          },
        },
      },
    },
    {
      title: t('操作日期'),
      dataIndex: 'auditTime',
      width: 100,
      sorter: true,
      customRender: ({ record }) => {
        return getDateFormat(record.auditTime);
      },
      formItemProps: {
        order: 3,
        component: 'RangePicker',
        defaultValue: [dayjs().format('YYYY-MM'), dayjs().format('YYYY-MM')],
        componentProps: {
          picker: 'month',
          valueFormat: 'YYYY-MM',
        },
      },
    },
    {
      title: 'IP',
      dataIndex: 'ip',
      width: 100,
      hideInSearch: true,
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    fieldMapToTime: [['auditTime', ['auditStartDate', 'auditEndDate'], 'YYYY-MM']],
  });
  return {
    columnsFirst,
    pageRef,
    formFirstProps,
  };
};
