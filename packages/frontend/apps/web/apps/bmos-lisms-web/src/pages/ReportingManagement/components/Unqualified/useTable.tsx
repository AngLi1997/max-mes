import { InspectionResultEnum, StatusType } from '@/types';
import { BMStateTag, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getDateFormat } = useConfig();
  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 220,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNo',
      width: 160,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'slurryDate',
      width: 170,
      customRender: ({ record }) => getDateFormat(record.slurryDate),
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 160,
    },
    {
      title: t('检品状态'),
      dataIndex: 'testArticleStatus',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof StatusType = record?.testArticleStatus?.value;
        if (!status) {
          return '-';
        }
        return <BMStateTag type={StatusType[status]}>{record?.testArticleStatus?.label}</BMStateTag>;
      },
    },
    {
      title: t('检验结论'),
      dataIndex: 'result',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.result?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.result?.label}
          </span>
        );
      },
    },
    {
      title: t('蛋白质含量'),
      dataIndex: 'protein',
      width: 110,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.protein?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.protein?.label}
          </span>
        );
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'alt',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.alt?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.alt?.label}
          </span>
        );
      },
    },
    {
      title: t('HBsAg'),
      dataIndex: 'hbsag',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hbsag?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hbsag?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-HCV'),
      dataIndex: 'hcv',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hcv?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hcv?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-HIV'),
      dataIndex: 'hiv',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.hiv?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.hiv?.label}
          </span>
        );
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'tp',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.tp?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.tp?.label}
          </span>
        );
      },
    },
    {
      title: t('蛋白电泳'),
      dataIndex: 'proteinElectrophoresis',
      width: 100,
      customRender: ({ record }) => {
        const status: keyof typeof InspectionResultEnum = record?.proteinElectrophoresis?.value;
        if (!status) {
          return '-';
        }
        return (
          <span style={{ color: status === InspectionResultEnum.UNQUALIFIED ? 'red' : undefined }}>
            {record?.proteinElectrophoresis?.label}
          </span>
        );
      },
    },
  ];

  return {
    tableRef,
    columns,
  };
};
