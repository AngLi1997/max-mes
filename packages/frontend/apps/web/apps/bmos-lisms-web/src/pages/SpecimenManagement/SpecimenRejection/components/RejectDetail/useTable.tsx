import { useConfig } from '@/stores';
import { SpecimenTypeEnum, StatusType } from '@/types';
import { BMStateTag, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
      resizable: true,
    },
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 160,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.sampleType?.label ?? '-';
      },
    },
    {
      title: t('标本分类'),
      dataIndex: 'sampleClassification',
      width: 100,
      customRender: ({ record }: any) => {
        if (record.sampleClassification?.value === SpecimenTypeEnum.SERUM_SPECIMEN) {
          return (
            <span
              style={{
                color: getConfigEnumsValueByParamId('血清标本颜色'),
              }}>
              {record.sampleClassification?.label}
            </span>
          );
        }
        return record.sampleClassification?.label ?? '-';
      },
    },
    {
      title: t('拒收原因'),
      dataIndex: 'refuseReasonName',
      width: 160,
      resizable: true,
    },
    {
      title: t('是否补样'),
      dataIndex: 'needSupplement',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.needSupplement?.label ?? '-';
      },
    },
    {
      title: t('拒收人'),
      dataIndex: 'applicant',
      width: 100,
      resizable: true,
    },
    {
      title: t('拒收日期'),
      dataIndex: 'applicantTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('审核状态'),
      dataIndex: 'status',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.status?.label ?? '-';
      },
    },
    {
      title: t('审核结果'),
      dataIndex: 'auditResult',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.auditResult?.value;
        return status ? <BMStateTag type={StatusType[status]}>{record?.auditResult?.label}</BMStateTag> : '-';
      },
    },
    {
      title: t('审核人'),
      dataIndex: 'reviewer',
      width: 100,
      resizable: true,
    },
    {
      title: t('审核日期'),
      dataIndex: 'reviewerTime',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.reviewerTime),
    },
  ];

  return {
    tableRef,
    columns,
  };
};
