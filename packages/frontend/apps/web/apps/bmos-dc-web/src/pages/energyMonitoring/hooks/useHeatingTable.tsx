import { postQueryEnergyGetReport } from '@/services/modules/energyMonitoring';
import { DataRequestFn, TableColumn } from '@bmos/components';
import { PickerMode } from 'ant-design-vue/es/vc-picker/interface';
import dayjs from 'dayjs';
import { DateMap } from '../type';

export const useHeatingTable = () => {
  const tableRef = ref<any>();

  const date = ref<string>(dayjs().format('YYYY-MM-DD'));
  const picker = ref<PickerMode>('date');
  const changePicker = (value: PickerMode) => {
    picker.value = value;
  };
  const extraParams = computed(() => {
    return {
      reportType: DateMap.get(picker.value),
      time: date.value,
    };
  });
  const columns: TableColumn[] = [
    {
      title: t('数值'),
      dataIndex: 'value',
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      customRender: () => {
        return 'MJ';
      },
    },
    {
      title: t('日期时间'),
      dataIndex: 'time',
    },
  ];

  const request: DataRequestFn = async (params: any): Promise<any> => {
    try {
      const { data } = await postQueryEnergyGetReport({
        ...params,
      });
      return Promise.resolve({
        data: {
          list: data,
        },
      });
    } catch (error) {
      return Promise.resolve({
        data: {
          list: [],
        },
      });
    }
  };

  return {
    heatingTableRef: tableRef,
    heatingColumns: columns,
    heatingTableDate: date,
    heatingTablePicker: picker,
    heatingTableChangePicker: changePicker,
    heatingExtraParams: extraParams,
    heatingRequest: request,
  };
};
