import { getInspectProgramResult } from '@/services';
import type { TableColumn } from '@bmos/components';
import { message } from 'ant-design-vue';

export const useTable = () => {
  const colorList = ['#59BF78', '#FF5633'];
  // 状态样式
  const style = {
    width: '7px',
    height: '7px',
    borderRadius: '50%',
    marginRight: '8px',
  };
  const InspectionDetailsModalRef = ref<any>();
  const inspectionRowData = ref<any>();
  const columns: TableColumn[] = [
    {
      align: 'center',
      title: t('序号'),
      width: 60,
      customRender: ({ index }) => <div>{index + 1}</div>,
    },
    {
      title: t('操作时间'),
      dataIndex: 'operationTime',
      width: 190,
      resizable: true,
      hideInSearch: true,
      formItemProps: {
        component: 'RangePicker',
      },
    },
    {
      title: t('操作类型'),
      dataIndex: 'operationType',
      resizable: true,
      width: 140,
      customRender: ({ record }) => <div>{record?.operationType?.name}</div>,
    },
    {
      title: t('具体操作'),
      dataIndex: 'specificOperationType',
      resizable: true,
      hideInSearch: true,
      width: 140,
      customRender: ({ record }) => <div>{record?.specificOperationType?.name}</div>,
    },
    {
      title: t('操作人员'),
      dataIndex: 'userName',
      hideInSearch: true,
      resizable: true,
      width: 140,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //物料编码
      title: t('物料编码'),
      dataIndex: 'materialCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //物料批号
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //物料件号
      title: t('物料件号'),
      dataIndex: 'materialNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //预定量
      title: t('预定量'),
      dataIndex: 'scheduled',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //可用量
      title: t('可用量'),
      dataIndex: 'available',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //皮重
      title: t('皮重'),
      dataIndex: 'tareWeight',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //毛重
      title: t('毛重'),
      dataIndex: 'grossWeight',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //单位
      title: t('单位'),
      dataIndex: 'unitName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //物料状态
      title: t('物料状态'),
      dataIndex: 'enable',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.enable == true ? 0 : 1],
            }}></div>
          <div style={{ color: colorList[record.enable == true ? 0 : 1] }}>
            {record.enable == true ? t('可用') : t('不可用')}
          </div>
        </div>
      ),
    },
    {
      //有效期至
      title: t('有效期至'),
      dataIndex: 'expirationTime',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //质量状态
      title: t('质量状态'),
      dataIndex: 'qualityStatus',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => <div>{record?.qualityStatus?.name}</div>,
    },
    {
      //产品名称
      title: t('产品名称'),
      dataIndex: 'productName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //产品编码
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //生产批号
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //暂存货位
      title: t('暂存货位'),
      dataIndex: 'materialPositionName',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //货位编码
      title: t('货位编码'),
      dataIndex: 'materialPositionCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //所属位置
      title: t('所属位置'),
      dataIndex: 'materialPositionPath',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //供应商
      title: t('供应商'),
      dataIndex: 'supplier',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => <div>{record?.supplier || '-'}</div>,
    },
    {
      //生产商
      title: t('生产商'),
      dataIndex: 'producer',
      width: 140,
      hideInSearch: true,
      resizable: true,
      customRender: ({ record }) => <div>{record?.producer || '-'}</div>,
    },
    {
      //原始编码
      title: t('原始编码'),
      dataIndex: 'originalCode',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //原厂批号
      title: t('原厂批号'),
      dataIndex: 'originalNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //备注
      title: t('备注'),
      dataIndex: 'remark',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //请验单号
      title: t('请验单号'),
      dataIndex: 'qualityInspectionNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      //报告单号
      title: t('报告单号'),
      dataIndex: 'reportNo',
      width: 140,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('检验信息'),
      fixed: 'right',
      hideInSearch: true,
      width: 100,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }: any) => [
        {
          label: t('查看'),
          onClick: async () => {
            try {
              const { data } = await getInspectProgramResult({ id: record.inspectId });
              inspectionRowData.value = data;
              InspectionDetailsModalRef.value.openModal();
            } catch (error: any) {
              message.error(error.message);
            }
          },
        },
      ],
    },
  ];
  return {
    columns,
    InspectionDetailsModalRef,
    inspectionRowData,
  };
};
