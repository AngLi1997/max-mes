import { useConfig, useDict, usePermissionStore, usePlasmaStation } from '@/stores';
import { InspectionProjectEnum, SpecimenTypeEnum } from '@/types';
import type { FormProps, Recordable, TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { BMIcons } from '@bmos/icons';
import { isEmpty } from '@bmos/utils';
import { Space } from 'ant-design-vue';

export const useTable = () => {
  const router = useRouter();
  const { inspectionStatusDict, sampleCategoryDict, testArticleStatusDict } = getDicts();
  const { hasPermission } = usePermissionStore();
  const { getDict } = useDict();
  const { getConfigEnumsValueByParamId, getDateFormat } = useConfig();
  const { getPlasmaStations } = usePlasmaStation();
  const pageRef = ref<any>();
  const updateTableData = () => pageRef.value?.fetchData(0);
  // 第一个table 行数据
  const firstRowData = ref<any>({});

  const unqualifiedModal = ref<boolean>(false);
  const unqualifiedTableData = ref<Recordable[]>([]); // 不合格项

  const inspectItemListRender = (record: any, code = InspectionProjectEnum.ProteinContent): any => {
    const inspectItem: any = record?.inspectItemList?.find((item: any) => item.code === code);
    if (isEmpty(inspectItem)) return '-';
    const { inspectStatus, unqualified } = inspectItem;
    // @ts-ignore
    const inspectionStatusItem = inspectionStatusDict.find((item: any) => item.value === inspectStatus?.value);
    if (isEmpty(inspectionStatusItem)) return '-';
    return (
      <Space>
        {/* @ts-ignore */}
        <BMIcons icon={inspectionStatusItem?.icon} />
        {unqualified ? (
          <BMIcons
            icon='Waring'
            style={{
              color: '#FF5E3D',
              fontSize: '16px',
            }}
            onClick={() => {
              firstRowData.value = record;
              unqualifiedTableData.value = [
                {
                  ...(inspectItem as Recordable),
                  sampleNo: record.sampleNo,
                  orgSampleNo: record.orgSampleNo,
                },
              ];
              unqualifiedModal.value = true;
            }}
          />
        ) : (
          ''
        )}
      </Space>
    );
  };

  const editModal = ref<boolean>(false);
  const columnsFirst: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 200,
    },
    {
      title: t('标本箱号'),
      dataIndex: 'boxId',
      width: 100,
    },
    {
      title: t('标本类型'),
      dataIndex: ['sampleType', 'label'],
      width: 150,
    },
    {
      title: t('标本分类'),
      dataIndex: 'sampleClassification',
      width: 170,
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
      dataIndex: 'donorNo',
      width: 170,
    },
    {
      title: t('姓名'),
      dataIndex: 'donorName',
      width: 100,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'donorTime',
      width: 170,
      sorter: true,
      customRender: ({ record }) => {
        return getDateFormat(record.donorTime);
      },
    },
    {
      title: t('免疫类型'),
      dataIndex: ['immunityType', 'label'],
      width: 120,
    },
    {
      title: t('检品状态'),
      dataIndex: ['testArticleStatus', 'label'],
      width: 120,
    },
    {
      title: t('检验状态'),
      dataIndex: ['inspectStatus', 'label'],
      width: 120,
    },
    {
      title: t('蛋白质含量'),
      dataIndex: 'ProteinContent',
      width: 120,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record);
      },
    },
    {
      title: t('ALT'),
      dataIndex: 'ALT',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.ALT);
      },
    },
    {
      title: t('HBsAg'),
      dataIndex: 'HBsAg',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.HBsAg);
      },
    },
    {
      title: t('抗-HCV'),
      dataIndex: 'AntiHCV',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.AntiHCV);
      },
    },
    {
      title: t('抗-HIV'),
      dataIndex: 'HIVAgAb',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.HIVAgAb);
      },
    },
    {
      title: t('抗-TP'),
      dataIndex: 'AntiTP',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.AntiTP);
      },
    },
    {
      title: t('蛋白电泳'),
      dataIndex: 'ProteinElectrophoresis',
      width: 100,
      customRender: ({ record }: any) => {
        return inspectItemListRender(record, InspectionProjectEnum.ProteinElectrophoresis);
      },
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 170,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 200,
      actions: ({ record }: any) => [
        {
          label: t('查看详情'),
          ifShow: hasPermission('210030001000001'),
          onClick: () => {
            router.push({
              name: 'inspection-management-task-center-detail',
              query: {
                sampleNo: record.sampleNo,
              },
            });
          },
        },
        {
          label: t('项目编辑'),
          ifShow: hasPermission('210030001000002') && !record.auditStatus,
          onClick: () => {
            firstRowData.value = record;
            editModal.value = true;
          },
        },
      ],
    },
  ];
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
    fieldMapToTime: [['donorTime', ['donorDateStart', 'donorDateEnd'], 'YYYY-MM-DD']],
    schemas: [
      {
        field: 'sampleBatchNo',
        label: t('标本批号'),
        component: 'Input',
      },
      {
        field: 'orgSampleNo',
        label: t('标本编号'),
        component: 'Input',
      },
      {
        field: 'originOrgCode',
        label: t('来源单位'),
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getPlasmaStations();
          },
        },
      },
      {
        field: 'boxId',
        label: t('标本箱号'),
        component: 'Input',
      },
      {
        field: 'sampleClassification',
        label: t('标本分类'),
        component: 'Select',
        componentProps: {
          options: sampleCategoryDict,
        },
      },
      {
        field: 'donorName',
        label: t('献浆者姓名'),
        component: 'Input',
      },
      {
        field: 'donorNo',
        label: t('献浆者编号'),
        component: 'Input',
      },
      {
        field: 'donorTime',
        label: t('采浆日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
      },
      {
        field: 'immunityType',
        label: t('免疫类型'),
        component: 'Select',
        componentProps: {
          request: async () => {
            return await getDict('免疫类型');
          },
        },
      },
      {
        field: 'testArticleStatus',
        label: t('检品状态'),
        component: 'Select',
        componentProps: {
          options: testArticleStatusDict,
        },
      },
    ],
  });
  return {
    columnsFirst,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
    unqualifiedTableData,
    unqualifiedModal,
    editModal,
  };
};
