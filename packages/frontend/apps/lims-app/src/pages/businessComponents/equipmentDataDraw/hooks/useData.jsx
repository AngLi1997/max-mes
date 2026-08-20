import { dictListDictCode, getEquipmentListApi, getScanEquipmentCodeApi, reqPlatformEquipmentAcquisitionPointHistoryDataApi, savePictureEquipmentList } from '@/api';

import { BMScan } from '@/BMComponents';
import { getCurrentCopyRecordItem, initFillData2, pageBasicDataRef, urlQueryRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { getCurrentTime } from '@/utils/time.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { format } from 'date-fns';
import * as echarts from 'echarts';
import html2canvas from 'html2canvas';
import { cloneDeep } from 'lodash-es';
import { nextTick, onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdButton from 'wot-design-uni/components/wd-button/wd-button.vue';
import MqttClient from './mqtt.js';
import { collectionTimeType, getAllNumSeries, getFormatterOverLimit, getXData, getYData } from './utils';

export const useData = () => {
  const formRef = ref();
  const echartsDom = ref();
  const queryInfo = ref({});
  const lineData = ref({});
  const selectDevice = ref('');
  const openSelectDevice = ref(false);
  const { procedureStepModelId } = pageBasicDataRef.value;
  const { productPlanId } = urlQueryRef.value;
  const deviceListOptions = ref([]);
  const selectDeviceData = ref();
  const { showNotify } = useNotify();
  const mqReportHistory = ref(null); // 中控
  const systemInfoStore = useSystemInfoStore();
  const { getParameterByCode } = systemInfoStore;
  // 选择设备
  const selectDeviceConfirm = (data) => {
    selectDeviceData.value = data;
    openSelectDevice.value = false;
    formRef.value.setFieldsValue({ device: data.showLabel });
  };
  const selectEquipmentData = ref();
  const configInfo = ref();
  const equipmentDataOptions = ref();
  const isShowChart = ref(false);
  const userId = ref();
  const loading = ref(false);
  const { version } = getCurrentCopyRecordItem();
  const equipmentData = ref('');
  const getDataNumber = ref(0);

  const initEcharts = async (params) => {
    isShowChart.value = true;
    const {
      correctionLineConfig,
      warningLineConfig,
      standardLineConfig,
      clearanceTime,
      collectionTime,
    } = selectEquipmentData.value;

    const { series, allNum } = getAllNumSeries({
      correctionLineConfig,
      warningLineConfig,
      standardLineConfig,
    });
    const xData = getXData(clearanceTime, collectionTime);
    const { yData, min, max } = await getYData(params, clearanceTime, collectionTime, selectEquipmentData.value.acquisitionDataCode, allNum, procedureStepModelId, queryInfo.value.componentId);
    // 获取数据
    nextTick(() => {
      echartsDom.value.init(echarts, (chart) => {
        chart.setOption({
          grid: {
          // 调整距离
            right: '5%',
            left: '5%',
            top: '5%',
            bottom: '10%',
          },
          xAxis: {
            type: 'category',
            data: xData,
            boundaryGap: false,
            axisLabel: {
              formatter: (value, index) => {
                // 获取数据的长度
                const dataLength = xData.length;
                // 是否是第一个标签
                const isFirstLabel = index === 0;
                // 是否是最后一个标签
                const isLastLabel = index === dataLength - 1;
                // 只显示头尾标签
                if (isFirstLabel || isLastLabel) {
                  return value;
                }
                return '';
              },
            },
          },
          yAxis: {
            type: 'value',
            max() {
              return max;
            },
            min() {
              return min;
            },
          },
          tooltip: {
            trigger: 'axis',
            backgroundColor: '#FFD5CC',
            textStyle: {
              color: '#6C6E73',
            },
            formatter: (params) => {
              const { value, name } = params[0];
              const isOverLimit = getFormatterOverLimit(selectEquipmentData.value, value);
              return `采集结果:   ${value}${isOverLimit}\n采集时间:   ${name}`;
            },
          },
          series: [
            {
              type: 'line',
              // label: {
              //   show: true,
              //   position: 'top'
              // },
              data: yData,
              markLine: {
                symbol: ['none', 'none'],
                label: {
                  show: false,
                },
                data: [...series],
              },
            },
          ],
        });
      });
    });
  };

  // 选择设备数采数据
  const selectEquipmentDataChange = (data) => {
    if (!data) {
      return;
    }
    const findData = configInfo.value.find(item => item.acquisitionDataCode === data);
    if (!findData) {
      formRef.value.setFieldsValue({
        intervalTime: undefined,
        unit: undefined,
        clearanceTime: [],
      });
      return;
    }
    selectEquipmentData.value = cloneDeep(findData);
    formRef.value.setFieldsValue({
      intervalTime: selectEquipmentData.value.collectionTime.value,
      unit: selectEquipmentData.value.collectionTime.type,
    });
    const nowTime = new Date(getCurrentTime()).getTime();
    if (!selectEquipmentData.value.requirementTime.value) {
      formRef.value.setFieldsValue({
        clearanceTime: [nowTime, nowTime],
      });
      selectEquipmentData.value.clearanceTime = [nowTime, nowTime];
    }
    else {
      const startTime = nowTime - selectEquipmentData.value.requirementTime.value * (collectionTimeType[selectEquipmentData.value.requirementTime.type]);
      formRef.value.setFieldsValue({
        clearanceTime: [startTime, nowTime],
      });
      selectEquipmentData.value.clearanceTime = [startTime, nowTime];
    }
  };

  // 采集历史数据
  const getHistoryData = async (params) => {
    if (!selectDeviceData.value.dataPropertyList || selectDeviceData.value.dataPropertyList?.length === 0) {
      showNotify({ type: 'danger', message: t('该设备未配置数据点位') });
      return;
    }
    const acquisitionPoint = selectDeviceData.value.dataPropertyList.find(item => item.code === params.equipmentData);
    if (!acquisitionPoint) {
      showNotify({ type: 'danger', message: t('该设备没有匹配的数据点位') });
      return;
    }
    if (selectDeviceData.value.acquisitionPlatform.value === 'supCon') {
      if (!acquisitionPoint.dataPointName) {
        showNotify({ type: 'danger', message: t('获取设备数据点位失败') });
        return;
      }
      const { collectionTime } = selectEquipmentData.value;
      const interval = collectionTime.value * collectionTimeType[collectionTime.type];
      console.log('========查询数据', {
        method: 'HistoryData',
        topic: 'report/history',
        names: [acquisitionPoint.dataPointName],
        // names: ['test-data'],
        seq: userId.value,
        mode: 0,
        begintime: params.clearanceTime[0] - 300000,
        endtime: params.clearanceTime[1],
        interval,
      });

      mqReportHistory.value?.handlePublish('SupconScadaHisData', JSON.stringify({
        method: 'HistoryData',
        topic: 'report/history',
        names: [acquisitionPoint.dataPointName],
        // names: ['test-data'],
        seq: userId.value,
        mode: 0,
        begintime: params.clearanceTime[0] - 300000,
        endtime: params.clearanceTime[1],
        interval,
      }));
    }
    if (selectDeviceData.value.acquisitionPlatform.value === 'hub') {
      // hub数采平台历史数据通过接口获取
      const data = {
        equipmentId: selectDeviceData.value.id,
        acquisitionPointId: acquisitionPoint.value,
        startTime: format(params.clearanceTime[0], 'yyyy-MM-dd HH:mm:ss'),
        endTime: format(params.clearanceTime[1], 'yyyy-MM-dd HH:mm:ss'),
        pageNum: 1,
        pageSize: 5000,
      };
      const res = await reqPlatformEquipmentAcquisitionPointHistoryDataApi(data);
      const historyData = (res.data.list || []).map((item) => {
        return {
          ...item,
          val: item.value,
          time: item.timeStamp,
        };
      });
      if (historyData.length === 0 || historyData[0].time > selectEquipmentData.value.clearanceTime[0] - getDataNumber.value * 3600000) {
        // 采集到的开始时间大于设置开始时间,往前推一小时查询
        if (getDataNumber.value === 3) {
          getDataNumber.value = 0;
          if (historyData.length === 0) {
            showNotify({ type: 'danger', message: t('未采集到开始时间数据') });
            return;
          }
          else {
            initEcharts(historyData);
          }
          return;
        }
        getDataNumber.value++;
        // 增加一小时重新查询历史数据
        getHistoryData({
          ...params,
          clearanceTime: [selectEquipmentData.value.clearanceTime[0] - getDataNumber.value * 3600000, selectEquipmentData.value.clearanceTime[1]],
        });
      }
      else {
        initEcharts(historyData);
      }
    }
  };
  const checkForm = async () => {
    // 选择了设备,并且填写了数据
    try {
      isShowChart.value = false;
      formRef.value?.submit();
      const params = await formRef.value?.validate();
      getHistoryData(params);
    }
    catch (error) {
      error.message && showNotify({ type: 'danger', message: t('图表生成失败') });
    }
  };
  // 清空绘图表单
  const clearEcharts = () => {
    echartsDom.value?.clear();
  };
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'device',
        label: t('设备信息'),
        colProps: {
          span: 12,
        },
        required: true,
        component: ({ formModel }) => {
          return (
            <BMScan
              v-model={formModel.device}
              type="select"
              placeholder={t('请选择')}
              allow-types={['03']}
              error-type-placeholder={t('请扫描')}
              onSuccess={async (code) => {
                try {
                  const { data } = await getScanEquipmentCodeApi({ code });
                  selectDeviceData.value = data;
                  formModel.device = `${data.code}-${data.name}`;
                  clearEcharts();
                }
                catch (error) {
                  error.message && showNotify({ type: 'danger', message: error.message });
                }
              }}
              onFail={($event) => {
                console.log('========扫描失败', $event);
              }}
              onClear={clearEcharts}
              onSelect={async () => {
                const { data } = await getEquipmentListApi({
                  componentId: queryInfo.value.id,
                  procedureStepModelId,
                  productPlanId,
                });
                deviceListOptions.value = data.map((item) => {
                  item.showLabel = `${item.code}-${item.name}`;
                  return item;
                });
                openSelectDevice.value = true;
                clearEcharts();
              }}
            />
          );
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择设备信息'),
            },
          ];
        },
      },
      {
        field: 'equipmentData',
        component: 'BMFormSelect',
        label: t('数采数据'),
        defaultValue: [],
        colProps: {
          span: 12,
        },
        required: true,
        componentProps: ({ formModel }) => {
          return {
            title: t('数采数据'),
            fieldNames: {
              name: 'name',
              label: 'name',
              key: 'value',
            },
            request: async () => {
              try {
                const componentsConfig = JSON.parse(queryInfo.value.configInfo);
                const configInfo = componentsConfig?.equipmentPictureConfigList;
                const selectDataId = configInfo.map((item) => {
                  return item.acquisitionDataCode;
                }) || [];
                const { data } = await dictListDictCode({
                  code: 'DeviceDataFields',
                });
                equipmentDataOptions.value = [];
                const options = [];
                data.forEach((item) => {
                  if (selectDataId.includes(item.value)) {
                    equipmentDataOptions.value.push({
                      ...item,
                      name: `${item.label}-${item.value}`,
                    });
                    options.push({
                      ...item,
                      name: `${item.label}-${item.value}`,
                    });
                  }
                });
                formModel.equipmentData = equipmentDataOptions.value[0]?.value;
                equipmentData.value = equipmentDataOptions.value[0]?.name;
                selectEquipmentDataChange(formModel.equipmentData);
                return equipmentDataOptions.value || [];
              }
              catch (error) {
                console.log('error: ', error);
              }
            },
            onChange: (data) => {
              selectEquipmentDataChange(data);
              clearEcharts();
            },
            onConfirm: (data) => {
              equipmentData.value = data.name;
            },
            onClear: () => {
              clearEcharts();
            },
          };
        },
      },
      {
        field: 'clearanceTime',
        component: 'BMFormRangePicker',
        defaultValue: [],
        label: t('采集时间'),
        colProps: {
          span: 12,
        },
        componentProps: {
          placeholder: t('开始时间-结束时间'),
          formatDate: 'yyyy-MM-dd HH:mm:ss',
          onChange: (data) => {
            if (selectEquipmentData.value) {
              selectEquipmentData.value.clearanceTime = data;
            }
            clearEcharts();
          },
          onClear: () => {
            clearEcharts();
          },
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: async (value) => {
                if (!value[0]) {
                  return Promise.reject(t('请选择开始使用时间'));
                }
                if (value[0] > value[1]) {
                  return Promise.reject(t('开始时间需小于结束时间'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'intervalTime',
        component: 'Input',
        defaultValue: [],
        label: t('间隔时间'),
        colProps: {
          span: 5,
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入间隔时间'),
            },
            {
              validator: async (value) => {
                if (!value) {
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                }
                else if (!Number.isInteger(number)) {
                  return Promise.reject(t('请输入整数'));
                }
                if (number <= 0) {
                  return Promise.reject(t('请输入正整数'));
                }
                if (`${value}`.length > 10) {
                  return Promise.reject(t('请输入小于11位正整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          onInput: (data) => {
            if (selectEquipmentData.value) {
              selectEquipmentData.value.collectionTime.value = data.value;
            }
            clearEcharts();
          },
          onClear: () => {
            clearEcharts();
          },
        },
      },
      {
        field: 'unit',
        component: 'BMFormSelect',
        label: t('单位'),
        defaultValue: 'minute',
        colProps: {
          span: 3,
        },
        required: true,
        componentProps: () => {
          return {
            title: t('单位'),
            fieldNames: {
              name: 'name',
              key: 'value',
            },
            options: [
              { label: t('日'), value: 'day' },
              { label: t('时'), value: 'hour' },
              { label: t('分'), value: 'minute' },
              { label: t('秒'), value: 'second' },
            ],
            onChange: (data) => {
              if (selectEquipmentData.value) {
                selectEquipmentData.value.collectionTime.type = data;
              }
              clearEcharts();
            },
            onClear: () => {
              clearEcharts();
            },
          };
        },
      },
      {
        field: 'btn',
        label: '111',
        noLabel: true,
        component: () => {
          return (
            <WdButton onClick={checkForm}>
              {t('生成图片')}
            </WdButton>
          );
        },
        colProps: {
          span: 3,
        },
      },
    ],
  });

  // 开起中控mqtt
  const createMqtt = async () => {
    try {
      const data = getParameterByCode('platform.sys.acquisition-address');
      const mqttConfig = JSON.parse(data?.value || '{}').supCon;
      const mqttUrl = mqttConfig?.mqttAddress || '172.16.0.24:8083';
      let url = '';
      // #ifdef APP-PLUS
      url = `wx://${mqttUrl}/mqtt`;
      // #endif
      // #ifdef H5
      url = `mqtt://${mqttUrl}/mqtt`;
      // #endif
      mqReportHistory.value = new MqttClient(url, 'report/history', (topic, data) => {
        if (!data.result) {
          if (data.msg.includes('The number of points to query a single tag cannot exceed')) {
            showNotify({ type: 'danger', message: t('超过最大查询数据量') });
          }
          else {
            showNotify({ type: 'danger', message: t('获取数据失败') });
          }
          return;
        }
        if (data.seq === userId.value && data.result) {
          if (data.result.data.length === 0 || data.result.data[0].time > selectEquipmentData.value.clearanceTime[0] - getDataNumber.value * 3600000) {
            // 采集到的开始时间大于设置开始时间,往前推一小时查询
            if (getDataNumber.value === 3) {
              getDataNumber.value = 0;
              if (data.result.data.length === 0) {
                showNotify({ type: 'danger', message: t('未采集到开始时间数据') });
                return;
              }
              else {
                initEcharts(data.result.data[0]?.datalist);
              }
              return;
            }
            getDataNumber.value++;
            // 增加一小时重新查询历史数据
            getHistoryData({
              equipmentData: equipmentData.value,
              clearanceTime: [selectEquipmentData.value.clearanceTime[0] - getDataNumber.value * 3600000, selectEquipmentData.value.clearanceTime[1]],
            });
          }
          else {
            initEcharts(data.result.data[0]?.datalist);
          }
        }
      });
    }
    catch (error) {
      console.log('=====error', error);
    }
  };

  // 保存图片
  const savePic = () => {
    if (!isShowChart.value) {
      showNotify({ type: 'danger', message: t('未生成数采图片无法保存') });
      return;
    }
    html2canvas(document.getElementById('charts_box_id')).then(
      async (canvas) => {
        // 第一种方法,生成base64
        const str = canvas.toDataURL('image/png');
        const params = {
          copyVersion: version,
          equipmentData: equipmentData.value,
          equipmentId: selectDeviceData.value.id,
          equipmentInfo: `${selectDeviceData.value.code}-${selectDeviceData.value.name}`,
          fieldId: queryInfo.value.fieldId,
          procedureStepModelId,
          productPlanId,
          picture: str,
          suffix: '.png',
        };
        try {
          loading.value = true;
          await savePictureEquipmentList(params);
          uni.navigateBack();
          initFillData2();
        }
        catch (error) {
          error.message && showNotify({ type: 'danger', message: error.message });
        }
        finally {
          loading.value = false;
        }
      },
    );
  };
  onMounted(() => {
    // 获取配置
    const componentsConfig = JSON.parse(queryInfo.value.configInfo);
    configInfo.value = componentsConfig?.equipmentPictureConfigList;
    const userInfo = getStorageSync(USER_INFO);
    userId.value = userInfo?.userId * 1;
  });
  onHide(() => {
    mqReportHistory.value?.endMqtt();
  });

  onShow(() => {
    createMqtt();
  });
  return {
    formRef,
    formProps,
    echartsDom,
    queryInfo,
    lineData,
    savePic,
    selectDevice,
    openSelectDevice,
    deviceListOptions,
    selectDeviceConfirm,
    selectEquipmentData,
    isShowChart,
    loading,
  };
};
