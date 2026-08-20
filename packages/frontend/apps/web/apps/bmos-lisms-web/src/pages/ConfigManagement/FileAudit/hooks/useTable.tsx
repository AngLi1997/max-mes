import { RemarkDetail } from '@/components/RemarkModal';
import { postConfigFileContent } from '@/services';
import { StatusType } from '@/types';
import { pdfPreview } from '@/utils';
import { BMStateTag, type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

export const useTable = () => {
  const pageRef = ref<any>();
  const { getDateFormat } = useConfig();
  const updateTableData = () => pageRef.value?.fetchData(0);
  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const remarkModalOpen = ref<boolean>(false);
  const remarkDetails = ref<RemarkDetail[]>([]);
  const columnsFirst: TableColumn[] = [
    {
      title: t('文件模板编号'),
      dataIndex: 'templateNo',
      width: 160,
    },
    {
      title: t('文件名称'),
      dataIndex: 'templateName',
      width: 170,
    },
    {
      title: t('文件类型'),
      dataIndex: 'fileTypeName',
      width: 140,
      hideInSearch: true,
    },
    {
      title: t('版本'),
      dataIndex: 'versionNumber',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('文件编号'),
      dataIndex: 'standardNumber',
      width: 170,
    },
    {
      title: t('文件版本号'),
      dataIndex: 'buildNumber',
      width: 170,
      hideInSearch: true,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectiveDate',
      width: 170,
      hideInSearch: true,
      customRender: ({ record }) => {
        return getDateFormat(record.effectiveDate);
      },
    },
    {
      title: t('提交人'),
      dataIndex: 'createBy',
      width: 140,
      hideInSearch: true,
    },
    {
      title: t('提交日期'),
      dataIndex: 'createTime',
      width: 170,
      sorter: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        return getDateFormat(record.createTime);
      },
    },
    {
      title: t('审核状态'),
      dataIndex: ['auditStatus', 'label'],
      width: 120,
      hideInSearch: true,
    },
    {
      title: t('审核结果'),
      dataIndex: 'auditResult',
      width: 120,
      hideInSearch: true,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.auditResult?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.auditResult?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'auditBy',
      width: 120,
      hideInSearch: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'auditTime',
      width: 170,
      sorter: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        return getDateFormat(record.auditTime);
      },
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 150,
      actions: ({ record }: any) => [
        {
          label: t('预览'),
          onClick: async () => {
            try {
              const res = await postConfigFileContent({
                templateNo: record.templateNo,
                versionNumber: record.versionNumber,
                includeBody: true,
              });
              await pdfPreview(res);
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        },
        {
          label: t('备注'),
          onClick: () => {
            remarkDetails.value = [
              {
                field: 'createRemark',
                value: record.createRemark,
                label: t('提交备注'),
              },
              {
                field: 'auditRemark',
                value: record.auditRemark,
                label: t('审核备注'),
              },
            ];
            remarkModalOpen.value = true;
          },
        },
      ],
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
    showAdvancedButton: false,
  });
  return {
    columnsFirst,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
    remarkModalOpen,
    remarkDetails,
  };
};
