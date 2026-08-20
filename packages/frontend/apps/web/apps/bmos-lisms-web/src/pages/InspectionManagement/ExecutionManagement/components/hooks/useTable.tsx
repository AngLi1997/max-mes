import { reqPlatformUserListByMenuId } from '@/services';
import { usePlasmaStation } from '@/stores';
import { InspectionProjectEnum } from '@/types';
import { type FormProps, type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { isEmpty } from '@bmos/utils';
import { FormItemRest, InputNumber } from 'ant-design-vue';

interface TableParams {
  props: any;
}
export const useTable = ({ props }: TableParams) => {
  const { InspectionCountDict, InspectionResultDict, InspectionResultDictOther, testArticleStatusDict } = getDicts();
  const { getPlasmaStations } = usePlasmaStation();
  const { getDateFormat } = useConfig();
  const pageRef = ref<any>();
  const updateTableData = () => pageRef.value?.fetchData(0);
  // 第一个table 行数据
  const firstRowData = ref<any>({});

  const columnsFirst = computed<TableColumn[]>(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
      case InspectionProjectEnum.ALT:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 110,
            fixed: 'left',
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 180,
            fixed: 'left',
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
            fixed: 'left',
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 140,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: `${t('结果值')}（g/L）`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
          {
            title: t('发布状态'),
            dataIndex: ['publishStatus', 'label'],
            width: 120,
          },
          {
            title: t('复核人'),
            dataIndex: 'checkBy',
            width: 120,
          },
          {
            title: t('复核日期'),
            dataIndex: 'checkTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.checkTime);
            },
          },
        ];
      case InspectionProjectEnum.HBsAg:
      case InspectionProjectEnum.AntiHCV:
      case InspectionProjectEnum.HIVAgAb:
      case InspectionProjectEnum.AntiTP:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 100,
            fixed: 'left',
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 180,
            fixed: 'left',
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
            fixed: 'left',
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 140,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('配板编号'),
            dataIndex: ['extraInfo', 'boardNo'],
            width: 150,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: t('孔位号'),
            dataIndex: ['extraInfo', 'holeNo'],
            width: 150,
          },
          {
            title: t('OD值'),
            dataIndex: ['extraInfo', 'od'],
            width: 150,
          },
          {
            title: t('比值'),
            dataIndex: ['extraInfo', 'ratio'],
            width: 150,
          },
          {
            title: `${t('结果值')}`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
            customRender: ({ record }) => {
              if (isEmpty(record.inspectValue)) return '-';
              return record.inspectValue === '-' ? `-(${t('阴性')})` : `+(${t('阳性')})`;
            },
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
            customRender: ({ record }) => {
              if (isEmpty(record.inspectValue)) return '-';
              return record.inspectValue === '-' ? `${t('阴性')}` : `${t('阳性')}`;
            },
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
          {
            title: t('发布状态'),
            dataIndex: ['publishStatus', 'label'],
            width: 120,
          },
          {
            title: t('复核人'),
            dataIndex: 'checkBy',
            width: 120,
          },
          {
            title: t('复核日期'),
            dataIndex: 'checkTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.checkTime);
            },
          },
        ];
      case InspectionProjectEnum.ProteinElectrophoresis:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 100,
            fixed: 'left',
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 180,
            fixed: 'left',
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
            fixed: 'left',
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 140,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: `${t('结果值')}（%）`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
          {
            title: t('发布状态'),
            dataIndex: ['publishStatus', 'label'],
            width: 120,
          },
          {
            title: t('复核人'),
            dataIndex: 'checkBy',
            width: 120,
          },
          {
            title: t('复核日期'),
            dataIndex: 'checkTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.checkTime);
            },
          },
        ];
      default:
        return [];
    }
  });
  const formFirstProps: Ref<Partial<FormProps>> = ref({
    labelWidth: 120,
    fieldMapToTime: [
      ['inspectDate', ['inspectDateStart', 'inspectDateEnd'], 'YYYY-MM-DD'],
      ['checkDate', ['checkDateStart', 'checkDateEnd'], 'YYYY-MM-DD'],
    ],
    schemas: [
      {
        field: 'orgSampleNo',
        label: t('标本编号'),
        component: 'Input',
      },
      {
        field: 'sampleBatchNo',
        label: t('标本批号'),
        component: 'Input',
      },
      {
        field: 'testArticleStatus',
        label: t('检品状态'),
        component: 'Select',
        componentProps: {
          options: testArticleStatusDict,
        },
      },
      {
        field: 'inspectTimes',
        label: t('检验次数'),
        component: 'Select',
        componentProps: {
          options: InspectionCountDict,
        },
      },
      {
        field: 'inspectValue',
        label: t('结果值'),
        vIf: [InspectionProjectEnum.ProteinContent, InspectionProjectEnum.ALT].includes(props.inspectItem.value),
        component: ({ formModel }: any) => {
          return (
            <FormItemRest>
              <div style={{ display: 'flex', alignItems: 'center', width: '100%' }}>
                <InputNumber
                  style={{ width: '49%' }}
                  v-model:value={formModel.inspectValueMin}
                  min={0}
                  precision={4}
                  placeholder={t('请输入')}
                />
                <span style={{ margin: '0 5px' }}>~</span>
                <InputNumber
                  style={{ width: '49%' }}
                  v-model:value={formModel.inspectValueMax}
                  min={0}
                  precision={4}
                  placeholder={t('请输入')}
                />
              </div>
            </FormItemRest>
          );
        },
      },
      {
        field: 'inspectResult',
        label: t('检验结果'),
        component: 'Select',
        componentProps: {
          request: async () => {
            switch (props.inspectItem.value) {
              case InspectionProjectEnum.HBsAg:
              case InspectionProjectEnum.AntiHCV:
              case InspectionProjectEnum.HIVAgAb:
              case InspectionProjectEnum.AntiTP:
                return InspectionResultDictOther;
              default:
                return InspectionResultDict;
            }
          },
        },
      },
      {
        label: t('试剂批号'),
        field: 'reagentBatchNo',
        component: 'Input',
      },
      {
        label: t('质控品批号'),
        field: 'qcBatchNo',
        component: 'Input',
      },
      {
        field: 'inspector',
        label: t('检验人'),
        component: 'Select',
        componentProps: {
          request: async () => {
            try {
              let menuId = '210030004';
              switch (props.inspectItem.value) {
                case InspectionProjectEnum.ProteinElectrophoresis:
                  menuId = '210030004';
                  break;
                case InspectionProjectEnum.ALT:
                  menuId = '210030005';
                  break;
                case InspectionProjectEnum.HBsAg:
                  menuId = '210030006';
                  break;
                case InspectionProjectEnum.AntiHCV:
                  menuId = '210030007';
                  break;
                case InspectionProjectEnum.HIVAgAb:
                  menuId = '210030008';
                  break;
                case InspectionProjectEnum.AntiTP:
                  menuId = '210030009';
                  break;
              }
              const { data } = await reqPlatformUserListByMenuId(menuId);
              return data.map((userItem: any) => {
                return {
                  label: userItem.userName + '-' + userItem.loginName,
                  value: userItem.userId,
                };
              });
            } catch (error) {}
          },
        },
      },
      {
        field: 'inspectDate',
        label: t('检验日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
      },
      {
        field: 'checkDate',
        label: t('复核日期'),
        component: 'RangePicker',
        componentProps: {
          showTime: false,
        },
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
    ],
  });

  const columnsSecond = computed<TableColumn[]>(() => {
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.ProteinContent:
      case InspectionProjectEnum.ALT:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 200,
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 200,
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: `${t('结果值')}`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 170,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
        ];
      case InspectionProjectEnum.HBsAg:
      case InspectionProjectEnum.AntiHCV:
      case InspectionProjectEnum.HIVAgAb:
      case InspectionProjectEnum.AntiTP:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 140,
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 240,
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('配板编号'),
            dataIndex: ['extraInfo', 'boardNo'],
            width: 150,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: t('孔位号'),
            dataIndex: ['extraInfo', 'holeNo'],
            width: 150,
          },
          {
            title: t('OD值'),
            dataIndex: ['extraInfo', 'od'],
            width: 150,
          },
          {
            title: t('比值'),
            dataIndex: ['extraInfo', 'ratio'],
            width: 150,
          },
          {
            title: `${t('结果值')}`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
            customRender: ({ record }) => {
              if (isEmpty(record.inspectValue)) return '-';
              return record.inspectValue === '-' ? `${t('阴性')}` : `${t('阳性')}`;
            },
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 170,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
        ];
      case InspectionProjectEnum.ProteinElectrophoresis:
        return [
          {
            title: t('检验项目'),
            dataIndex: 'inspectItemName',
            width: 200,
          },
          {
            title: t('标本编号'),
            dataIndex: 'orgSampleNo',
            width: 200,
          },
          {
            title: t('标本批号'),
            dataIndex: 'sampleBatchNo',
            width: 160,
          },
          {
            title: t('检品状态'),
            dataIndex: ['testArticleStatus', 'label'],
            width: 120,
          },
          {
            title: t('检验次数'),
            dataIndex: ['inspectTimes', 'label'],
            width: 120,
          },
          {
            title: `${t('结果值')}`,
            dataIndex: 'inspectValue',
            width: 150,
            sorter: true,
          },
          {
            title: t('检验结果'),
            dataIndex: ['inspectResult', 'label'],
            width: 120,
          },
          {
            title: t('试剂批号'),
            dataIndex: 'reagentBatchNo',
            width: 120,
          },
          {
            title: t('质控品批号'),
            dataIndex: 'qcBatchNo',
            width: 120,
          },
          {
            title: t('内质控值'),
            dataIndex: 'qcValue',
            width: 120,
          },
          {
            title: t('质控品含量'),
            dataIndex: 'qcContent',
            width: 120,
          },
          {
            title: t('是否在控'),
            dataIndex: ['inControl', 'label'],
            width: 120,
          },
          {
            title: t('检验设备'),
            dataIndex: 'instrument',
            width: 120,
          },
          {
            title: t('来源单位'),
            dataIndex: 'originOrgCode',
            width: 170,
          },
          {
            title: t('检验人'),
            dataIndex: 'inspector',
            width: 120,
          },
          {
            title: t('检验日期'),
            dataIndex: 'inspectTime',
            width: 170,
            sorter: true,
            customRender: ({ record }) => {
              return getDateFormat(record.inspectTime);
            },
          },
        ];
      default:
        return [];
    }
  });
  return {
    columnsFirst,
    columnsSecond,
    firstRowData,
    pageRef,
    updateTableData,
    formFirstProps,
  };
};
