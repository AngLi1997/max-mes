import type { BMStateTagType, FormProps, Recordable, TableColumn } from '@bmos/components';
import { BMStateTag, BMStateTagEnum } from '@bmos/components';
import { t } from '@bmos/i18n';
import { VersionStatus } from '../enum';

export const useTable = () => {
  const pageRef = ref<any>();

  const updateSecondTable = async () => {
    pageRef.value.fetchData(1);
  };

  // 第一个table 行数据
  const firstRowData = ref<any>({});
  const columnsFirst: TableColumn[] = [
    {
      title: t('名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'mergeCode',
      width: 200,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('规格'),
      dataIndex: 'specification',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生效版本'),
      dataIndex: 'activeVersion',
      width: 150,
      resizable: true,
      hideInSearch: true,
    },
  ];

  const formFirstProps: Ref<Partial<FormProps>> = ref({
    showAdvancedButton: false,
    actionColOptions: {
      span: 18,
    },
  });

  const VersionStatusClassMap: Map<
    VersionStatus,
    {
      type: BMStateTagType;
      stateName: string;
    }
  > = new Map([
    [
      VersionStatus.EDIT,
      {
        type: BMStateTagEnum.PRIMARY,
        stateName: t('编辑'),
      },
    ],
    [
      VersionStatus.APPROVAL,
      {
        type: BMStateTagEnum.WARNING,
        stateName: t('审核'),
      },
    ],
    [
      VersionStatus.CONFIRM,
      {
        type: BMStateTagEnum.CONFIRM,
        stateName: t('确认'),
      },
    ],
    [
      VersionStatus.INVALID,
      {
        type: BMStateTagEnum.DEFAULT,
        stateName: t('失效'),
      },
    ],
    [
      VersionStatus.VALID,
      {
        type: BMStateTagEnum.SUCCESS,
        stateName: t('生效'),
      },
    ],
    [
      VersionStatus.WAIT_VALID,
      {
        type: BMStateTagEnum.WARNING,
        stateName: t('待生效'),
      },
    ],
  ]);

  // 当前操作行数据
  const secondRowData = ref<Recordable>({});

  const columnsSecond: TableColumn[] = [
    {
      title: t('版本号'),
      dataIndex: 'version',
      fixed: 'left',
      width: 120,
      resizable: true,
    },
    {
      title: t('BOM名称'),
      dataIndex: 'productFormulaName',
      width: 200,
      resizable: true,
    },
    {
      title: t('生产BOM版本'),
      dataIndex: 'productFormulaVersionNo',
      width: 200,
      resizable: true,
    },
    {
      title: t('版本描述'),
      dataIndex: 'description',
      width: 200,
      resizable: true,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectDate',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('状态'),
      dataIndex: 'actionState',
      width: 110,
      resizable: true,
      fixed: 'right',
      customRender: ({ record }) => (
        <BMStateTag type={VersionStatusClassMap.get(record.actionState?.value)?.type}>
          {record.actionState?.label}
        </BMStateTag>
      ),
    },
  ];
  const selectCurrentNode = ref<any>({});

  return {
    columnsFirst,
    formFirstProps,
    columnsSecond,
    secondRowData,
    firstRowData,
    selectCurrentNode,
    pageRef,
    updateSecondTable,
  };
};
