import { SpecimenTypeEnum } from '@/types';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'batchNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
      resizable: true,
    },
    {
      title: t('标本类型'),
      dataIndex: 'sampleType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => record?.sampleType?.label ?? '-',
    },
    {
      title: t('标本分类'),
      dataIndex: 'sampleClassification',
      width: 100,
      resizable: true,
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
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      width: 140,
      resizable: true,
    },
    {
      title: t('姓名'),
      dataIndex: 'plasmaDonorName',
      width: 100,
      resizable: true,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.applicantTime),
    },
    {
      title: t('拒收原因'),
      dataIndex: 'refuseReasonName',
      width: 120,
      resizable: true,
    },
    {
      title: t('是否补样'),
      dataIndex: 'needSupplement',
      width: 100,
      resizable: true,
      customRender: ({ record }) => record?.needSupplement?.label ?? '-',
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
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 100,
      resizable: true,
    },
  ];

  return {
    tableRef,
    columns,
  };
};
