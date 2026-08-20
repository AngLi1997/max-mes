import { useConfig } from '@/stores';
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
      width: 160,
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
      customRender: ({ record }) => {
        return record?.sampleType?.label ?? '-';
      },
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
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      resizable: true,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgName',
      width: 100,
      resizable: true,
    },
    {
      title: t('免疫类型'),
      dataIndex: 'immunityType',
      width: 140,
      resizable: true,
      customRender: ({ record }) => {
        return record?.immunityType?.label ?? '-';
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
