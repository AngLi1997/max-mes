import { getProductTreeApi } from '@/services';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import dayjs, { Dayjs } from 'dayjs';
import { ref } from 'vue';

export const useTable = () => {
  const fetchProductOptionTree = async () => {
    try {
      const { data } = await getProductTreeApi();
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      return loop(data);
    } catch (error) {}
  };
  type RangeValue = [Dayjs, Dayjs];
  const dates = ref<any>([dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')]);
  const value = ref<RangeValue>();
  const reset = () => {
    dates.value = [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')];
  };
  const productTreeData = ref<any[]>([]);
  fetchProductOptionTree().then((res: any) => {
    productTreeData.value = res;
  });
  const columns: TableColumn[] = [
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      hideInSearch: false,
      resizable: true,
      width: 190,
      formItemProps: {
        order: 3,
      },
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('操作时间'),
      dataIndex: 'operateTime',
      hideInSearch: false,
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: [dayjs().startOf('month').format('YYYY-MM-DD'), dayjs().endOf('month').format('YYYY-MM-DD')],

        component: 'RangePicker',
        order: 7,
        componentProps: ({ formModel }: any) => {
          return {
            format: 'YYYY-MM-DD',
            picker: 'date',
            valueFormat: 'YYYY-MM-DD',
            style: { width: '100%' },
            value: formModel.operateTime || value.value,
            disabledDate: (current: Dayjs) => {
              if (!dates.value || (dates.value as any).length === 0) {
                return false;
              }
              const tooLate = dates.value[0] && dayjs(current).diff(dates.value[0], 'days') > 30;
              const tooEarly = dates.value[0]
                ? dates.value[1] && dayjs(dates.value[1]).diff(current, 'days') > 30
                : dayjs(dates.value).startOf('month') > current;
              return tooEarly || tooLate;
            },
            onChange: (val: RangeValue) => {
              value.value = val;
            },
            onCalendarChange: (val: RangeValue) => {
              dates.value = val;
            },
          };
        },
      },
    },
    {
      title: t('操作类型'),
      dataIndex: 'operationType',
      hideInSearch: false,
      hideInTable: true,
      resizable: true,
      width: 190,
      formItemProps: {
        component: 'Select',
        order: 4,
        componentProps: () => ({
          options: [
            {
              label: t('入库'),
              value: 'INBOUND',
            },
            {
              label: t('出库'),
              value: 'OUTBOUND',
            },
            {
              label: t('盘增'),
              value: 'PLUS',
            },
            {
              label: t('盘减'),
              value: 'MINUS',
            },
          ],
        }),
      },
    },
    {
      title: t('操作类型'),
      dataIndex: 'operationTypeShowName',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('具体操作'),
      dataIndex: 'operateDetail',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('操作人员'),
      dataIndex: 'operatorName',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },

    {
      title: t('暂存货位'),
      dataIndex: 'materialPositionName',
      hideInSearch: true,
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('货位编码'),
      dataIndex: 'materialPositionCode',
      hideInSearch: true,
      resizable: true,
      width: 190,
      formItemProps: {
        defaultValue: '',
      },
    },
    {
      title: t('所属位置'),
      dataIndex: 'materialPositionPath',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('物料编码'),
      dataIndex: 'materialCode',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      hideInSearch: false,
      resizable: true,
      width: 190,
      formItemProps: {
        order: 2,
      },
    },
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productCode',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('物料信息'),
      dataIndex: 'materialKeyWords',
      hideInTable: true,
      formItemProps: {
        componentProps: {
          placeholder: t('名称/编码'),
        },
        order: 1,
      },
    },
    {
      title: t('产品信息'),
      dataIndex: 'productId',
      hideInTable: true,
      formItemProps: {
        component: 'TreeSelect',
        componentProps: () => {
          return {
            placeholder: t('请选择产品'),
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            treeData: productTreeData.value,
          };
        },
        order: 5,
      },
    },

    {
      title: t('生产批号'),
      dataIndex: 'productBatchNo',
      resizable: true,
      width: 190,
      formItemProps: {
        order: 6,
      },
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
  ];

  return { columns, reset };
};
