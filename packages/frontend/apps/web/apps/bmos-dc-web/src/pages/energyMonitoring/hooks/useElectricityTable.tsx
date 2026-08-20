import { postQueryEnergyGetBuildReport } from '@/services/modules/energyMonitoring';
import { DataRequestFn, TableColumn } from '@bmos/components';
import { PickerMode } from 'ant-design-vue/es/vc-picker/interface';
import dayjs from 'dayjs';
import { DateMap } from '../type';

export const useElectricityTable = () => {
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
        return 'KW.h';
      },
    },
    {
      title: t('日期时间'),
      dataIndex: 'time',
    },
  ];

  const request: DataRequestFn = async (params: any): Promise<any> => {
    try {
      const { data } = await postQueryEnergyGetBuildReport({
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
    electricityTableRef: tableRef,
    electricityColumns: columns,
    electricityTableDate: date,
    electricityTablePicker: picker,
    electricityTableChangePicker: changePicker,
    electricityExtraParams: extraParams,
    electricityRequest: request,
  };
};
