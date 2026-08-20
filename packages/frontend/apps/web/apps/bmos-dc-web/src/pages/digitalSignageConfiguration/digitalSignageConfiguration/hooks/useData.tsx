import {
  dashboardDeleteById,
  getDashboardInstanceById,
  getDashboardViewDataById,
  queryDashboardCreate,
  queryDashboardListAll,
  queryDashboardUpdate,
} from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { RenderCallbackParams } from '@bmos/components';
import { FormProps } from '@bmos/components';
import { Modal, message } from 'ant-design-vue';
import { initEChart } from './initEChart';
import { utils } from './utils';
export const useData = () => {
  const boardList = ref<any>([]);
  const filedIdPool = ref(0);
  const clickFiledId = ref('');
  const clickItemId = ref('');
  const activeKey = ref('data');
  const formRef = ref();
  const showChart = ref(false);
  const loading = ref(false);
  const eChartOptions = ref();
  const peiData = ref<any>([]);
  const {
    getDatasetIdOptions,
    getDatapointIdOptions,
    setFormValue,
    formUpdateSchema,
    fetchProductOptionTree,
    getProcessList,
  } = utils(formRef);
  const getAllData = async () => {
    const { data } = await queryDashboardListAll();
    boardList.value = data.map((item: any) => {
      item.filedId = item.id;
      return item;
    });
    filedIdPool.value = boardList.value.length;
  };
  // 增加看板
  const addBoard = () => {
    boardList.value.push({
      filedId: ++filedIdPool.value,
    });
  };
  // 删除看板
  const boardDelete = (filedId: string, id: string) => {
    Modal.confirm({
      title: t('是否删除该数据'),
      icon: h(ExclamationCircleOutlined),
      content: t('删除后无法恢复，是否删除？'),
      async onOk() {
        try {
          if (id) {
            await dashboardDeleteById({ id });
          }
          boardList.value = boardList.value.filter((item: any) => item.filedId != filedId);
          if (clickItemId.value == id) {
            clickItemId.value = '';
            clickFiledId.value = '';
            formRef.value.resetForm();
          }
          return Promise.resolve();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };
  // 点击/切换看板
  const clickBoardChange = async (item: any) => {
    try {
      if (!item.id) {
        // 没有id为新增看板,重置表单
        formRef.value.resetForm();
      } else {
        const { data } = await getDashboardInstanceById({ id: item.id });
        setFormValue(data);
        // 刷新图表
        const res = await getDashboardViewDataById({ id: item.id });
        showChart.value = false;
        initEChart(eChartOptions, data, res.data, peiData);
        nextTick(() => {
          showChart.value = true;
        });
      }
      // 获取详情
      clickFiledId.value = item.filedId;
      clickItemId.value = item.id;
      showChart.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
    // 回显表单
  };
  // 更换表单类型
  const typeChange = () => { };
  // 更新图表
  const formSubmit = async () => {
    try {
      loading.value = true;
      // 表单校验
      formRef.value?.submit();
      const params = await formRef.value?.validate();
      if (!clickItemId.value) {
        // 没有id调新增
        const { data } = await queryDashboardCreate({
          dashboardShowStyle: 'CHART', // 一期不做文字展示
          ...params,
          productIds: params.dashboardDataSourceType == 'INTERNAL' ? params.productIds : [params.productIds],
          tagValues: [params.referOne, params.referTwo],
        });
        clickItemId.value = data;
        // 刷新左侧列表id和title
        boardList.value.find((item: any) => {
          if (item.filedId == clickFiledId.value) {
            item.id = data;
            item.title = params.title;
          }
          return item.filedId == clickFiledId.value;
        });
      } else {
        // 有id调更新
        await queryDashboardUpdate({
          id: clickItemId.value,
          dashboardShowStyle: 'CHART', // 一期不做文字展示
          ...params,
          productIds: params.dashboardDataSourceType == 'INTERNAL' ? params.productIds : [params.productIds],
          tagValues: [params.referOne || '', params.referTwo || ''],
        });
      }
      // 刷新图表
      const res = await getDashboardViewDataById({ id: clickItemId.value });
      showChart.value = false;
      initEChart(eChartOptions, params, res.data, peiData);
      nextTick(() => {
        showChart.value = true;
      });
      loading.value = false;
      message.success(t('操作成功'));
    } catch (error: any) {
      loading.value = false;
      if (error.errorFields) {
        if (
          [
            'title',
            'statisticsRangeType',
            'date',
            'dashboardDataSourceType',
            'productIds',
            'processId',
            'datasetId',
            'datapointId',
            'productStatus',
          ].includes(error.errorFields[0].name[0])
        ) {
          activeKey.value = 'data';
        } else {
          activeKey.value = 'style';
        }
        return;
      }
      error.message && message.error(error.message);
    }
  };
  // 表单属性
  const formProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    initialValues: {},
    fieldMapToTime: [['date', ['statisticsRangeStartDate', 'statisticsRangeEndDate'], 'YYYY-MM-DD']],
    schemas: [
      {
        field: 'title',
        label: t('看板标题'),
        component: 'Input',
        vShow: () => activeKey.value == 'data',
        required: true,
      },
      {
        field: 'statisticsRangeType',
        label: t('统计范围'),
        component: 'Select',
        vShow: () => activeKey.value == 'data',
        required: true,
        defaultValue: 'CURRENT_YEAR',
        componentProps: () => {
          return {
            options: [
              {
                label: t('当年'),
                value: 'CURRENT_YEAR',
              },
              {
                label: t('当月'),
                value: 'CURRENT_MONTH',
              },
              {
                label: t('具体日期'),
                value: 'COSTUME',
              },
            ],
          };
        },
      },
      {
        field: 'date',
        component: 'RangePicker',
        vShow: () => {
          return activeKey.value == 'data';
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.statisticsRangeType == 'COSTUME';
        },
        noLabel: true,
        label: t('统计日期范围'),
        required: true,
        componentProps: () => {
          return {
            valueFormat: 'YYYY-MM-DD',
            format: 'YYYY-MM-DD',
          };
        },
      },
      {
        field: 'dashboardDataSourceType',
        label: t('数据源选择'),
        component: 'Select',
        vShow: () => activeKey.value == 'data',
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [
              {
                label: t('内置指标'),
                value: 'INTERNAL',
              },
              {
                label: t('数据集'),
                value: 'DATASET',
              },
            ],
            onChange: (value: string) => {
              // 内置时多选
              formModel.productIds = undefined;
              formModel.processId = undefined;
              formModel.datasetId = undefined;
              formModel.datapointId = undefined;
              formRef.value.updateSchema({
                field: 'productIds',
                componentProps: {
                  multiple: value == 'INTERNAL',
                },
              });
            },
          };
        },
      },
      {
        field: 'productIds',
        component: 'TreeSelect',
        label: t('产品'),
        required: true,
        vShow: () => activeKey.value == 'data',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            multiple: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              return await fetchProductOptionTree();
            },
            onChange: (value: any) => {
              if (formModel.dashboardDataSourceType != 'DATASET') {
                // 内置不需要选工艺数据集
                return;
              }
              formModel.processId = undefined;
              formModel.datasetId = undefined;
              formModel.datapointId = undefined;
              // 选择产品信息后回显可选择的生产工艺
              if (value) {
                // 获取工艺
                getProcessList(value);
              } else {
                //清空下拉
                formUpdateSchema('productIds');
              }
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('工艺'),
        required: true,
        vShow: () => {
          return activeKey.value == 'data';
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.dashboardDataSourceType == 'DATASET';
        },
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [],
            fieldNames: {
              value: 'id',
            },
            onChange: async (processId: string) => {
              formModel.datasetId = undefined;
              formModel.datapointId = undefined;
              if (!processId) {
                formUpdateSchema('processId');
                return;
              }
              await getDatasetIdOptions(processId);
            },
          };
        },
      },
      {
        field: 'datasetId',
        label: t('数据集名称'),
        component: 'Select',
        vShow: () => {
          return activeKey.value == 'data';
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.dashboardDataSourceType == 'DATASET';
        },
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: async (id: string) => {
              formModel.datapointId = undefined;
              if (!id) {
                // 清空重置
                formUpdateSchema('datasetId');
                return;
              }
              await getDatapointIdOptions(id);
            },
          };
        },
      },
      {
        field: 'datapointId',
        label: t('索引-数据点名称'),
        component: 'Select',
        vShow: () => {
          return activeKey.value == 'data';
        },
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.dashboardDataSourceType == 'DATASET';
        },
        required: true,
        componentProps: () => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
          };
        },
      },
      {
        field: 'productStatus',
        label: t('生产状态'),
        component: 'Select',
        vShow: () => activeKey.value == 'data',
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('生产中'),
                value: 'PROCESSING',
              },
              {
                label: t('生产完成'),
                value: 'FINISHED',
              },
            ],
          };
        },
      },
      // TODO: 一期不做
      // {
      //   field: 'showStyle',
      //   label: t('显示样式'),
      //   component: 'Select',
      //   vShow: () => activeKey.value == 'style',
      //   required: true,
      //   componentProps: () => {
      //     return {
      //       options: [
      //         {
      //           label: t('统计图展示'),
      //           value: 0,
      //         },
      //         {
      //           label: t('文字展示'),
      //           value: 1,
      //         },
      //       ],
      //     };
      //   },
      // },
      {
        field: 'chartType',
        label: t('图表样式'),
        component: 'Select',
        vShow: () => activeKey.value == 'style',
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('折线图'),
                value: 'LINE',
              },
              {
                label: t('柱状图'),
                value: 'BAR',
              },
              {
                label: t('环形图'),
                value: 'CIRCLE',
              },
              {
                label: t('饼状图'),
                value: 'PIE',
              },
            ],
          };
        },
      },
      {
        field: 'yAxisStrategy',
        label: t('度量(Y轴)'),
        component: 'Select',
        vShow: () => activeKey.value == 'style',
        required: true,
        defaultValue: 'COUNT',
        componentProps: () => {
          return {
            options: [
              {
                label: t('数值'),
                value: 'COUNT',
              },
            ],
          };
        },
      },
      {
        field: 'xAxisStrategy',
        label: t('维度(X轴)'),
        component: 'Select',
        vShow: () => activeKey.value == 'style',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.chartType == 'LINE' || formModel.chartType == 'BAR';
        },
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('产线'),
                value: 'BY_LINE',
              },
              {
                label: t('日期-月'),
                value: 'BY_MONTH',
              },
              {
                label: t('产品'),
                value: 'BY_PRODUCT',
              },
              {
                label: t('生产批次'),
                value: 'BY_BATCH',
              },
            ],
          };
        },
      },
      {
        field: 'granularityStrategy',
        label: t('显示颗粒度'),
        component: 'Select',
        vShow: () => activeKey.value == 'style',
        required: true,
        componentProps: () => {
          return {
            options: [
              {
                label: t('产品'),
                value: 'SPLIT_WITH_PRODUCT',
              },
              {
                label: t('根据维度(X轴)聚合'),
                value: 'GROUP_WITH_X_AXIS',
              },
              {
                label: t('生产批次'),
                value: 'SPLIT_WITH_BATCH',
              },
            ],
          };
        },
      },
      {
        field: 'referOne',
        label: t('参考线1'),
        component: 'Input',
        vShow: () => activeKey.value == 'style',
      },
      {
        field: 'referTwo',
        label: t('参考线2'),
        component: 'Input',
        vShow: () => activeKey.value == 'style',
      },
    ],
  });
  onMounted(() => {
    getAllData();
  });
  return {
    boardList,
    clickFiledId,
    activeKey,
    formRef,
    formProps,
    showChart,
    loading,
    eChartOptions,
    peiData,
    addBoard,
    boardDelete,
    clickBoardChange,
    typeChange,
    formSubmit,
  };
};
