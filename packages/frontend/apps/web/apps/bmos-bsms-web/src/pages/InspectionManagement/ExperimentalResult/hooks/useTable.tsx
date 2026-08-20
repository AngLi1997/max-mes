import { useDict } from '@/stores/dictStore';
import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { getImmuniTypeDict } = useDict();
  const { sampleTypeDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    // 列表项
    {
      title: t('标本基础信息'),
      dataIndex: 'plasmaBaseInfo',
      hideInSearch: true,
      children: [
        {
          title: t('标本批号'),
          dataIndex: 'inspectionBatchNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('标本编号'),
          dataIndex: 'sampleNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('标本类型'),
          dataIndex: 'sampleType',
          width: 140,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.sampleType?.name;
          },
        },
        {
          title: t('收检日期'),
          dataIndex: 'receiveDate',
          width: 140,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('检验日期'),
          dataIndex: 'testDate',
          width: 340,
          ellipsis: true,
          resizable: true,
        },
      ],
    },
    {
      title: t('献浆者信息'),
      dataIndex: 'donorInfo',
      hideInSearch: true,
      children: [
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.no;
          },
        },
        {
          title: t('姓名'),
          dataIndex: 'donorName',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.name;
          },
        },
        {
          title: t('性别'),
          dataIndex: 'sex',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.sex?.name;
          },
        },
        {
          title: t('血型'),
          dataIndex: 'bloodType',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.plasmaDonorInfo?.bloodType?.name;
          },
        },
      ],
    },
    {
      title: t('检验结果'),
      dataIndex: 'stockInfo',
      hideInSearch: true,
      children: [
        {
          title: t('蛋白质含量'),
          dataIndex: 'proteinContentResult',
          width: 130,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.proteinContentResult?.name;
          },
        },
        {
          title: 'ALT',
          dataIndex: 'altResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.altResult?.name;
          },
        },
        {
          title: t('HBsAg'),
          dataIndex: 'elisaHbsagResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHbsagResult?.name;
          },
        },
        {
          title: t('抗-HCV'),
          dataIndex: 'elisaHcvResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHcvResult?.name;
          },
        },
        {
          title: t('抗-HIV'),
          dataIndex: 'elisaHivResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHivResult?.name;
          },
        },
        {
          title: t('抗-TP'),
          dataIndex: 'elisaTpResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaTpResult?.name;
          },
        },
        {
          title: t('HBV DNA'),
          dataIndex: 'pcrHbvResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHbvResult?.name;
          },
        },
        {
          title: t('HCV RNA'),
          dataIndex: 'pcrHcvResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHcvResult?.name;
          },
        },
        {
          title: t('HIV RNA'),
          dataIndex: 'pcrHivResult',
          width: 120,
          ellipsis: true,
          sorter: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcrHivResult?.name;
          },
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.immunityType?.name;
          },
        },
        {
          title: t('效价值'),
          dataIndex: 'titerValue',
          width: 100,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
    // labelWidth: 105,
    // labelAlign: 'left',
    schemas: [
      {
        label: t('采浆日期'),
        field: 'slurryDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
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
        field: 'receiveDate',
        component: 'RangePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          picker: 'date',
          valueFormat: 'YYYY-MM-DD',
        },
      },
      {
        label: t('检验批号'),
        field: 'inspectionBatchNo',
        component: 'Input',
      },
      {
        label: t('免疫类型'),
        field: 'immunityType',
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getImmuniTypeDict();
          },
        },
      },
      {
        label: t('献浆者编号'),
        field: 'plasmaDonorNo',
        component: 'Input',
      },
    ],
    fieldMapToTime: [
      ['slurryDate', ['slurryDateUp', 'slurryDateDown'], 'YYYY-MM-DD'],
      ['receiveDate', ['receiveDateUp', 'receiveDateDown'], 'YYYY-MM-DD'],
    ],
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
