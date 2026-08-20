import { paginationBig } from '@/utils/paginationConfig';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';

export const useTable = () => {
  const { correspondingTypeDict, qualityStatusDict, warehouseDict } = getDicts();
  const pageRef = ref<any>(null);
  const rowData = ref<Recordable>({});
  const columnsFirst: TableColumn[] = [
    // 搜索项
    {
      title: t('血浆箱/托盘号'),
      dataIndex: 'primeContainerNo',
      hideInTable: true,
    },
    {
      title: t('血浆编号'),
      dataIndex: 'no',
      hideInTable: true,
    },
    {
      title: t('血浆状态'),
      dataIndex: 'plasmaStatus',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: qualityStatusDict,
        },
      },
    },
    {
      title: t('血浆批号'),
      dataIndex: 'inWarehouseBatchNo',
      hideInTable: true,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'plasmaDonorNo',
      hideInTable: true,
    },
    {
      title: t('对应类型'),
      dataIndex: 'corrRelationType',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: correspondingTypeDict,
        },
      },
    },
    {
      title: t('所在仓库'),
      dataIndex: 'warehouseId',
      hideInSearch: !getWarehouseConfigByCode.value,
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: {
          options: warehouseDict,
        },
      },
    },
    // 列表项
    {
      title: t('血浆基础信息'),
      dataIndex: 'basicInfo',
      hideInSearch: true,
      children: [
        {
          title: t('血浆批号'),
          dataIndex: 'inWarehouseBatchNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('血浆编号'),
          dataIndex: 'no',
          width: 180,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('对应编号'),
          dataIndex: 'corrPlasmaNo',
          width: 180,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('所在仓库'),
          dataIndex: 'warehouseId',
          hideInTable: !getWarehouseConfigByCode.value,
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return record?.warehouse?.name;
          },
        },
        {
          title: t('血浆箱/托盘号'),
          dataIndex: 'primeContainerNo',
          width: 170,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('采浆日期'),
          dataIndex: 'slurryDate',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
        {
          title: t('血浆状态'),
          dataIndex: 'plasmaStatus',
          width: 150,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return record?.plasmaStatus?.name;
          },
        },
        {
          title: t('对应类型'),
          dataIndex: 'corrRelationType',
          width: 140,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }: any) => {
            return record?.corrRelationType?.name;
          },
        },
      ].filter((item: any) => !item?.hideInTable),
    },
    {
      title: t('献浆者信息'),
      dataIndex: 'donorInfo',
      hideInSearch: true,
      children: [
        {
          title: t('姓名'),
          dataIndex: 'plasmaDonorName',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('献浆者编号'),
          dataIndex: 'plasmaDonorNo',
          width: 170,
          ellipsis: true,
          sorter: true,
          resizable: true,
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
      title: t('发布&化验信息'),
      dataIndex: 'releaseInfo',
      hideInSearch: true,
      children: [
        {
          title: t('化验总结果'),
          dataIndex: 'totalResult',
          width: 120,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.totalResult?.name;
          },
        },
        {
          title: t('HBsAg'),
          dataIndex: 'elisaHbsagResult',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHbsagResult?.name;
          },
        },
        {
          title: t('抗-HCV'),
          dataIndex: 'elisaHcvResult',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHcvResult?.name;
          },
        },
        {
          title: t('抗-HIV'),
          dataIndex: 'elisaHivResult',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaHivResult?.name;
          },
        },
        {
          title: t('抗-TP'),
          dataIndex: 'elisaTpResult',
          width: 90,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.elisaTpResult?.name;
          },
        },
        {
          title: t('ALT'),
          dataIndex: 'altResult',
          width: 80,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.altResult?.name;
          },
        },
        {
          title: t('蛋白含量'),
          dataIndex: 'proteinContentResult',
          width: 100,
          ellipsis: true,
          resizable: true,
          customRender: ({ record }) => {
            return record?.proteinContentResult?.name;
          },
        },
        {
          title: t('免疫类型'),
          dataIndex: 'immunityType',
          width: 100,
          ellipsis: true,
          resizable: true,
        },
        {
          title: t('效价'),
          dataIndex: 'titer',
          width: 100,
          ellipsis: true,
          sorter: true,
          resizable: true,
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    initialValues: {},
    showAdvancedButton: true,
    labelWidth: 105,
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
