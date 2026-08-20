import { usePlasmaStation } from '@/stores/plasmaStation';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});

  const columnsFirst: TableColumn[] = [
    {
      title: t('检验时间'),
      dataIndex: 'testDate',
      width: 240,
      resizable: true,
      customRender: ({ record }) => {
        if (record.minTestDate && record.maxTestDate) {
          return `${record.minTestDate}~${record.maxTestDate}`;
        } else {
          return record.minTextDate || record.maxTestDate || '-';
        }
      },
    },
    {
      title: t('检品批号'),
      dataIndex: 'inspectionBatchNo',
      width: 170,
      resizable: true,
    },
    {
      title: t('份数'),
      dataIndex: 'inspectionNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('合格数'),
      dataIndex: 'qualifiedNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('不合格数'),
      dataIndex: 'unqualifiedNumber',
      width: 120,
      sorter: true,
      resizable: true,
    },
    {
      title: t('HBsAg'),
      dataIndex: 'hbsAg',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hbsAg?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hbsAg?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hbsAg?.effectiveDate || '-';
          },
        },
      ],
    },
    {
      title: t('HCV抗体'),
      dataIndex: 'hcv',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hcv?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hcv?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hcv?.effectiveDate || '-';
          },
        },
      ],
    },
    {
      title: t('HIV抗体'),
      dataIndex: 'hiv',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hiv?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hiv?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.hiv?.effectiveDate || '-';
          },
        },
      ],
    },
    {
      title: t('梅毒'),
      dataIndex: 'tp',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.tp?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.tp?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.tp?.effectiveDate || '-';
          },
        },
      ],
    },
    {
      title: t('ALT'),
      dataIndex: 'alt',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.alt?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.alt?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.alt?.effectiveDate || '-';
          },
        },
      ],
    },
    {
      title: t('PCR'),
      dataIndex: 'pcr',
      width: 450,
      resizable: true,
      children: [
        {
          title: t('试剂批号'),
          dataIndex: 'reagentNo',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcr?.reagentNo || '-';
          },
        },
        {
          title: t('试剂厂家'),
          dataIndex: 'name',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcr?.name || '-';
          },
        },
        {
          title: t('有效期'),
          dataIndex: 'effectiveDate',
          width: 200,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.pcr?.effectiveDate || '-';
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: false,
    actionColOptions: {
      span: 12,
    },
    schemas: [
      {
        label: t('浆站名称'),
        field: 'originOrgCode',
        component: 'Select',
        componentProps: {
          request: getPlasmaStations,
        },
      },
      {
        label: t('检品批号'),
        field: 'inspectionBatchNo',
        component: 'Input',
      },
    ],
  };

  return {
    pageRef,
    rowData,
    columnsFirst,
    formFirstProps,
  };
};
