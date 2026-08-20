import type { FormProps, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const {
    auditResultDict,
    effectPriceImmuTypeDict,
    inspectionResultReleaseStatusDict,
    qualifiedStatusDict,
    sampleTypeDict,
  } = getDicts();
  const pageRef = ref<any>(null);

  const fetchData = async (index: number = 0, params?: any) => {
    pageRef.value.fetchData(index, params);
  };

  const columnsFirst: TableColumn[] = [
    {
      title: t('基本信息'),
      dataIndex: 'basicInfo',
      children: [
        {
          title: t('来源单位'),
          dataIndex: 'originOrg',
          width: 220,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本批号'),
          dataIndex: 'sampleBatchNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          width: 160,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleType?.name;
          },
        },
        {
          title: t('采样日期'),
          dataIndex: 'slurryDate',
          width: 170,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('收检日期'),
          dataIndex: 'acceptanceDate',
          width: 170,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('发布状态'),
          dataIndex: 'publishStatus',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.publishStatus?.name;
          },
        },
      ],
    },
    {
      title: t('献浆者信息'),
      dataIndex: 'donorInfo',
      children: [
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('姓名'),
          dataIndex: 'plasmaDonorName',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sex?.name;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.bloodType?.name;
          },
        },
      ],
    },
    {
      title: t('蛋白质含量检验信息'),
      dataIndex: 'proteinInfo',
      children: [
        {
          title: t('蛋白质含量'),
          dataIndex: 'protein',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.protein?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'proteinCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'proteinReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('转氨酶检验信息'),
      dataIndex: 'aminoAcidInfo',
      children: [
        {
          title: t('ALT'),
          dataIndex: 'alt',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.alt?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'altCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'altReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'altReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'altQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('ELISA乙肝检验信息'),
      dataIndex: 'elisaInfo',
      children: [
        {
          title: t('HBsAg'),
          dataIndex: 'hbsAg',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hbsAg?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'hbsAgCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'hbsAgReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'hbsAgReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'hbsAgQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('ELISA丙肝检验信息'),
      dataIndex: 'elisaHCVInfo',
      children: [
        {
          title: t('抗-HCV'),
          dataIndex: 'hcv',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hcv?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'hcvCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'hcvReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'hcvReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'hcvQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('ELISA艾滋检验信息'),
      dataIndex: 'elisaHIVInfo',
      children: [
        {
          title: t('抗-HIV'),
          dataIndex: 'hiv',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hiv?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'hivCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'hivReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'hivReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'hivQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('ELISA梅毒检验信息'),
      dataIndex: 'elisaTPInfo',
      children: [
        {
          title: t('抗-TP'),
          dataIndex: 'tp',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.tp?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'tpCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'tpReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'tpReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'tpQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('PCR乙肝检验信息'),
      dataIndex: 'pcrInfo',
      children: [
        {
          title: t('HBV DNA'),
          dataIndex: 'pcrHbv',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHbv?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'pcrHbvCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'pcrHbvReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'pcrHbvReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'pcrHbvQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('PCR丙肝检验信息'),
      dataIndex: 'pcrHCVInfo',
      children: [
        {
          title: t('HCV RNA'),
          dataIndex: 'pcrHcv',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHcv?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'pcrHcvCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'pcrHcvReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'pcrHcvReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'pcrHcvQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('PCR艾滋检验信息'),
      dataIndex: 'pcrHIVInfo',
      children: [
        {
          title: t('HIV RNA'),
          dataIndex: 'pcrHiv',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHiv?.name;
          },
        },
        {
          title: t('检验人'),
          dataIndex: 'pcrHivCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'pcrHivReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'pcrHivReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'pcrHivQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('效价检验信息'),
      dataIndex: 'effectPriceInfo',
      children: [
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 160,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.immunityType?.name;
          },
        },
        {
          title: t('检验免疫类型'),
          dataIndex: 'titerType',
          width: 140,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.titerType?.name;
          },
        },
        {
          title: t('效价值'),
          dataIndex: 'titer',
          width: 100,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('检验人'),
          dataIndex: 'titerCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('复核人'),
          dataIndex: 'titerReCheckBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('试剂批号'),
          dataIndex: 'titerReagentBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('质控品批号'),
          dataIndex: 'titerQualityControllerBatchNo',
          width: 190,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('发布审核信息'),
      dataIndex: 'publishInfo',
      children: [
        {
          title: t('总发布人'),
          dataIndex: 'publishBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('总发布日期'),
          dataIndex: 'publishDate',
          width: 120,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('审核状态'),
          dataIndex: 'auditStatus',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.auditStatus?.name;
          },
        },
        {
          title: t('审核人'),
          dataIndex: 'auditBy',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('审核日期'),
          dataIndex: 'auditDate',
          width: 170,
          sorter: true,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    labelWidth: 120,
    schemas: [
      {
        label: t('标本批号'),
        field: 'sampleBatchNo',
        component: 'Input',
      },
      {
        label: t('标本编号'),
        field: 'sampleNo',
        component: 'Input',
      },
      {
        label: t('标本类型'),
        field: 'sampleType',
        component: 'Select',
        componentProps: {
          options: sampleTypeDict,
        },
      },
      {
        label: t('收检日期'),
        field: 'acceptanceDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('发布状态'),
        field: 'publishStatus',
        component: 'Select',
        componentProps: {
          options: inspectionResultReleaseStatusDict,
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
      {
        label: t('蛋白质含量'),
        field: 'protein',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('ALT'),
        field: 'alt',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('HBsAg'),
        field: 'hbsAg',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('抗-HCV'),
        field: 'hcv',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('抗-HIV'),
        field: 'hiv',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('抗-TP'),
        field: 'tp',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('HBV DNA'),
        field: 'pcrHbv',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('HCV RNA'),
        field: 'pcrHcv',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('HIV RNA'),
        field: 'pcrHiv',
        component: 'Select',
        componentProps: {
          options: qualifiedStatusDict,
        },
      },
      {
        label: t('检验免疫类型'),
        field: 'titerType',
        component: 'Select',
        componentProps: {
          options: effectPriceImmuTypeDict,
          // request: async () => {
          //   return await getImmuniTypeDict();
          // },
        },
      },
      {
        label: t('审核状态'),
        field: 'auditStatus',
        component: 'Select',
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('审核日期'),
        field: 'auditDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
    ],
    fieldMapToTime: [
      ['auditDate', ['auditDateUp', 'auditDateDown'], 'YYYY-MM-DD'],
      ['acceptanceDate', ['acceptanceDateUp', 'acceptanceDateDown'], 'YYYY-MM-DD'],
    ],
  };

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    fetchData,
  };
};
