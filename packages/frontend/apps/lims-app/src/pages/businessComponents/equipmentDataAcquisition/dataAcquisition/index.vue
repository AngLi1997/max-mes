<template>
  <BMLayout>
    <BMBasicPage
      :title="title"
      background-color="#F2F3F5"
      @left-click="toBack"
      @cancel="toBack"
      @confirm="confirm"
    >
      <template #titleRight>
        <wd-button type="text" size="small" @click="isOpenMessage = true">
          {{ buttonText }}
        </wd-button>
      </template>
      <view class="content">
        <view class="equipment-info" :class="[mode === 'realTime' ? '' : 'no-data']">
          <view class="name">
            <uv-icon
              name="fuwuqi"
              custom-prefix="bmos-icon"
              size="18.75rpx"
              color="#2871FF"
            />
            <text>{{ equipmentInfo.name }}-{{ equipmentInfo.code }}</text>
          </view>
          <view class="info">
            {{ t("设备工位") }}:
            <view class="info_data">
              {{ equipmentInfo.stationNameList?.map(item => `${item.code}-${item.name}`)?.join('，') }}
            </view>
          </view>
          <view class="info">
            {{ t("设备信息") }}:
            <view class="info_data">
              {{ equipmentInfo.infoPropertyList?.map(item => `${item.name}：${item.showValue ? item.showValue : item.value}`)?.join('，') }}
            </view>
          </view>
          <view v-if="mode === 'realTime'" class="data">
            <view
              v-for="(point, index) in dataPointList"
              :key="index"
              class="dataPoint"
            >
              <text class="point-name">
                {{ point.dataPointName }}
              </text>
              <text class="point-value">
                {{ point.value != null && point.value !== undefined ? point.value : '-' }}
              </text>
            </view>
          </view>
        </view>
        <view class="collection-info" :style="mode === 'realTime' ? 'max-height: calc(100% - 111.33rpx - 9.38rpx);' : 'max-height: calc(100% - 60rpx - 9.38rpx);'">
          <template v-if="mode === 'realTime'">
            <view class="title">
              <view>{{ t('采集时间') }}:{{ clickRecordDataTime }}</view>
              <wd-button size="medium" style="margin: 0;" @click="recordData">
                {{ t('记录数据') }}
              </wd-button>
            </view>
            <view class="table-container">
              <BMTable
                ref="tableRef"
                v-bind="tableProps"
              />
            </view>
          </template>
          <template v-else>
            <HistoryTable
              v-model:history-time="historyTime"
              :equipment-id="queryInfo.equipmentId"
              :component-id="queryInfo.id"
              :history-data="historyData"
              :click-row="clickHistoryRow"
              :data="dataList"
              @get-data="getHistoryData"
            />
          </template>
        </view>
      </view>
      <!-- 签名 -->
      <BMSignModal
        v-model:show="showSign"
        v-model="signValue"
        :label-list="labelList"
        :title="t('签名')"
        :confirm-text="t('确定')"
        :signature-data="curParams"
        show-remark
        :remark-required="true"
        @confirm="confirmSignPopup"
      />
      <BMModal
        v-model="showTipPopup"
        :show-title="false"
        size="small"
        custom-class="tip-popup"
        :close-on-click-modal="false"
        :confirm-text="t('保存')"
        @confirm="confirmTipPopup"
        @cancel="cancelTipPopup"
      >
        <view class="tip">
          {{ t('数据超限，是否继续保存？') }}
        </view>
      </BMModal>
      <BMMessageBox
        v-model="isOpenMessage"
        :title="t('数据将会清空')"
        :content="t('切换采集类型，当前的数据配置会被清空')"
        @cancel="isOpenMessage = false"
        @confirm="collectionModeSwitch"
      />
    </BMBasicPage>
  </BMLayout>
</template>

<script setup lang="jsx">
import {
  getMqttAccreditApi,
  reqMesEquipmentAcquisitionDataApi,
  reqMesEquipmentGetConfigByEquipmentIdApi,
  reqMesUpdateEquipmentAcquisitionDataApi,
  reqPlatformEquipmentAcquisitionPointDataApi,
  reqPlatformEquipmentAcquisitionPointHistoryDataApi,
} from '@/api';
import {
  BMBasicPage,
  BMFormSelect,
  BMIcon,
  BMLayout,
  BMMessageBox,
  BMModal,
  BMSignModal,
  BMTable,
} from '@/BMComponents';
import {
  componentsMap,
  getCurrentCopyRecordItem,
  initFillData2,
  pageBasicDataRef,
  signOptionsRef,
  urlQueryRef,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { formatTime, getCurrentTime } from '@/utils/time.js';
import { USER_INFO } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { onHide, onLoad, onShow, onUnload } from '@dcloudio/uni-app';
import { format } from 'date-fns';
import { isUndefined } from 'lodash-es';

import { onUnmounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdInput from 'wot-design-uni/components/wd-input/wd-input.vue';
import HistoryTable from './components/HistoryTable.vue';
import MqttClient from './hooks/mqtt.js';

const { showNotify } = useNotify();

const systemInfoStore = useSystemInfoStore();
const { getParameterByCode } = systemInfoStore;
const mode = ref('realTime');
const title = ref(t('采集实时数据'));
const buttonText = ref(t('采集历史数据'));
const showSign = ref(false);
const tableKey = ref(0);
const clickRecordDataTime = ref('');
const clickHistoryRow = ref('');
const tableRef = ref();
const isOpenMessage = ref(false);

const labelList = ref([
  {
    label: t('修订人'),
    signatureAction: 45,
  },
  {
    label: t('复核人'),
    signatureAction: 46,
    options: signOptionsRef.value.map((item) => {
      return {
        ...item,
        label: item.userName,
      };
    }),
  },
]);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const dataPointList = ref([]);
const userId = ref();
const historyTime = ref('');
const queryInfo = ref({});

// 表格数据
const dataList = ref([]);

const checkNumberComponentValue = (component, result) => {
  let value = '';
  try {
    value = Number.parseFloat(result);
    if (Number.isNaN(value)) {
      return false;
    }
  }
  catch (_error) {
    return false;
  }
  if (!component.configInfo) {
    return true;
  }
  let state = true;
  // 0 范围限制
  // 1 数值相等
  if (component.configInfo.limit === 1) {
    state = value === Number.parseFloat(component.configInfo.numericalValue);
  }
  else if (component.configInfo.limit === 0) {
    const { scopeMin, scopeMax, lowerLimit, upperLimit } = component.configInfo.scope;
    const min = Number.parseFloat(scopeMin);
    const max = Number.parseFloat(scopeMax);
    const newLowerLimit = isUndefined(lowerLimit) ? 1 : lowerLimit;
    const newUpperLimit = isUndefined(upperLimit) ? 1 : upperLimit;
    /*
      下限 0 上限 0 大于下限 小于上限
      下限 0 上限 1 大于下限 小于等于上限
      下限 1 上限 0 大于等于下限 小于上限
      下限 1 上限 1 大于等于下限 小于等于上限
    */
    const conditions = {
      '00': (min && value <= min) || (max && value >= max),
      '01': (min && value <= min) || (max && value > max),
      '10': (min && value < min) || (max && value >= max),
      '11': (min && value < min) || (max && value > max),
    };
    const key = `${newLowerLimit}${newUpperLimit}`;
    state = !conditions[key];
  }
  return state;
};
const showTipPopup = ref(false);
// 确认
const curParams = ref({});

const cancelTipPopup = () => {
  showTipPopup.value = false;
};

const equipmentInfo = ref({});
const equipmentPropertyList = ref({});
const getEquipmentInfo = async () => {
  try {
    const { data } = await reqMesEquipmentGetConfigByEquipmentIdApi(queryInfo.value?.equipmentId);
    equipmentInfo.value = data;
    equipmentInfo.value.dataPropertyList.forEach((item) => {
      item.showName = `${item.name}-${item.code}`;
      // 将采集点作为key,创建匹配对象
      equipmentPropertyList.value[item.dataPointName] = item;
    });
    dataList.value = [...equipmentInfo.value.dataPropertyList];
    tableRef.value.tableData = dataList.value;
    if (equipmentInfo.value.acquisitionPlatform?.value === 'hub') {
      // 获取采集项的初始数据
      const res = await reqPlatformEquipmentAcquisitionPointDataApi(queryInfo.value?.equipmentId);
      dataPointList.value = res.data || [];
    }
    myMqttCreate();
  }
  catch (_error) {
    //
  }
};

// 采集模式切换
const collectionModeSwitch = () => {
  isOpenMessage.value = false;
  if (mode.value === 'realTime') {
    title.value = t('采集历史数据');
    buttonText.value = t('采集实时数据');
    mode.value = 'history';
  }
  else {
    title.value = t('采集实时数据');
    buttonText.value = t('采集历史数据');
    mode.value = 'realTime';
    tableKey.value += 1;
    getEquipmentInfo();
  }
};
const historyData = ref({});

// 记录数据按钮点击
const recordData = () => {
  clickRecordDataTime.value = getCurrentTime();
  dataList.value.forEach((dataItem) => {
    dataItem.timeStamp = clickRecordDataTime.value;
    dataItem.type = 1;
    const item = dataPointList.value.find(item => item.dataPointName === dataItem.dataPointName);
    dataItem.dataPointValue = item?.value;
  });
};
const mqReport = ref(null);
const mqPush = ref(null);
const mqReportHistory = ref(null);
const getHistoryData = async (begintime, endtime, names, acquisitionPointId) => {
  clickHistoryRow.value = names;
  console.log('=========这里发送消息了吗');

  if (equipmentInfo.value.acquisitionPlatform?.value === 'supCon') {
    // 中控（supCon）数采平台历史数据通过mqtt获取
    mqReport.value?.handlePublish('SupconScadaHisData', JSON.stringify({
      method: 'HistoryData',
      topic: 'report/history',
      names: [names],
      seq: userId.value || 1,
      mode: 0,
      begintime,
      endtime,
      count: 20,
      interval: 1000,
    }));
  }
  if (equipmentInfo.value.acquisitionPlatform?.value === 'hub') {
    // hub数采平台历史数据通过接口获取
    const params = {
      equipmentId: queryInfo.value.equipmentId,
      acquisitionPointId,
      startTime: format(begintime, 'yyyy-MM-dd HH:mm:ss'),
      endTime: format(endtime, 'yyyy-MM-dd HH:mm:ss'),
      pageNum: 1,
      pageSize: 50,
    };
    const res = await reqPlatformEquipmentAcquisitionPointHistoryDataApi(params);
    historyData.value[names] = (res.data.list || []).map((item) => {
      return {
        ...item,
        val: item.value,
        time: item.timeStamp,
      };
    });
  }
};
const mqttUrl = ref('');
const hubMqttQueue = ref([]);
// 开起中控mqtt
const createMqtt = async () => {
  try {
    console.log('=-=====初始化了吗');
    const data = getParameterByCode('platform.sys.acquisition-address');
    const mqttConfig = JSON.parse(data?.value || '{}').supCon;
    mqttUrl.value = mqttConfig?.mqttAddress || '172.30.1.103:8083';
    let url = '';
    // #ifdef APP-PLUS
    url = `wx://${mqttUrl.value}/mqtt`;
    // #endif
    // #ifdef H5
    url = `mqtt://${mqttUrl.value}/mqtt`;
    // #endif
    mqReport.value = new MqttClient(url, queryInfo.value.code || 'rtdvalue/report', (topic, data) => {
      console.log('mqReport消息', topic, data);
      // 根据设备id获取设备绑定采集项的所有数据
      dataPointList.value = [];
      data.RTValue.forEach((item) => {
        const equipment = equipmentPropertyList.value[item.name];
        if (equipment) {
          dataPointList.value.push({
            dataPointName: item.name,
            value: item.value,
          });
        }
      });
    });
    mqPush.value = new MqttClient(`mqtt://${mqttUrl.value}/mqtt`, 'rtdvalue/push', (topic, data) => {
      console.log('mqPush消息', topic, data);
    });

    mqReportHistory.value = new MqttClient(`mqtt://${mqttUrl.value}/mqtt`, 'report/history', (topic, data) => {
      if (data.seq === userId.value && data.result) {
        historyData.value[data.result.data[0].name] = data.result.data[0].datalist.filter(item => item.val);
      }
    });
  }
  catch (error) {
    console.log('=-=====error', error);

    //
  }
};

// 关闭中控mqtt
const myClearInterval = () => {
  mqReport.value?.endMqtt();
  mqPush.value?.endMqtt();
  mqReportHistory.value?.endMqtt();
  mqReport.value = null;
  mqPush.value = null;
  mqReportHistory.value = null;
};

// 保存数据
const saveData = async (data, isUpdate = 1) => {
  try {
    if (isUpdate === 1) {
      await reqMesEquipmentAcquisitionDataApi(data);
    }
    else {
      await reqMesUpdateEquipmentAcquisitionDataApi(data);
    }
    initFillData2();
    if (queryInfo.value.returnData === 1) {
      uni.navigateBack();
    }
    else {
      uni.navigateBack({
        delta: 2,
      });
    }
    myClearInterval();
  }
  catch (error) {
    error.message && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
};
const confirmSignPopup = () => {
  showSign.value = false;
  saveData({ ...curParams.value, remark: signValue.value.remark }, 2);
};

const confirm = async (checkResult = true) => {
  const equipmentAcquisitionPoint = dataList.value?.map((item) => {
    return {
      acquisitionCode: item.acquisitionPointCode,
      acquisitionId: item.acquisitionPointId,
      componentId: item.id,
      dataPointName: item.dataPointName,
      dataPointValue: item.dataPointValue,
      dataPointValueTime: item.type === 2 ? '' : item.timeStamp,
      inputType: item.type === 2 ? 'MANUAL' : 'ACQUISITION',
      dataPropertyCode: item.code,
    };
  });
    // 如果 equipmentAcquisitionPoint 中有 dataPointValue 为空的数据，则提示用户
  if (equipmentAcquisitionPoint.some(item => item.dataPointValue !== 0 && !item.dataPointValue)) {
    uni.showToast({
      title: t('存在未填报设备数据，请确认后再试'),
      icon: 'error',
      duration: 2000,
      mask: true,
    });
    return;
  }

  let checkResultFlag = true;
  try {
    const comment = componentsMap.get(queryInfo.value.curFieldId);
    const parentId = comment.parent.id;
    // 遍历 componentsMap 中的数据，找到 parentId 为当前 parentId 的数据
    const componentList = Array.from(componentsMap.values()).filter(item => item.parentId === parentId);
    equipmentAcquisitionPoint.forEach((item) => {
      const component = componentList.find(it => it.id === item.componentId);
      if (!checkNumberComponentValue(component, item.dataPointValue)) {
        checkResultFlag = false;
      }
    });
  }
  catch (_error) {
    //
  }

  if (!checkResultFlag && checkResult) {
    showTipPopup.value = true;
    return;
  }
  const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
  const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
  const { version } = getCurrentCopyRecordItem();
  // 获取采集时间
  let acquisitionTime = '';
  if (mode.value === 'realTime') {
    // 实时时间
    acquisitionTime = clickRecordDataTime.value ? clickRecordDataTime.value : getCurrentTime();
  }
  else {
    acquisitionTime = formatTime(historyTime.value, 'datetime');
  }
  const data = {
    batchNo,
    componentId: queryInfo.value.id,
    copyVersion: version,
    equipmentId: queryInfo.value.equipmentId,
    equipmentAcquisitionPoint,
    procedureStepId,
    procedureStepModelId,
    processId,
    processVersion,
    productPlanId,
    recordItemId,
    recordVersionId,
    acquisitionTime,
    equipmentAcquisitionGroupComponentId: queryInfo.value.equipmentAcquisitionGroupComponentId,
    reuse: reusable,
  };
  const { isUpdate } = queryInfo.value;
  if (Number(isUpdate) === 2) {
    showSign.value = true;
    curParams.value = {
      ...data,
    };
  }
  else {
    saveData(data);
  }
};

const confirmTipPopup = () => {
  showTipPopup.value = false;
  confirm(false);
};

// 开起hub的mqtt
const createHUBMqtt = async () => {
  hubMqttQueue.value = [];
  try {
    // 获取授权信息
    const res = await getMqttAccreditApi();
    const data = getParameterByCode('platform.sys.acquisition-address');
    const mqttConfig = JSON.parse(data?.value || '{}').hub;
    const url = mqttConfig?.mqttAddress || '172.30.1.167:38080';
    let mqttUrl = '';
    // #ifdef APP-PLUS
    mqttUrl = `wx://${url}/ws/dmcMQTT/`;
    // #endif
    // #ifdef H5
    mqttUrl = `mqtt://${url}/ws/dmcMQTT/`;
    // #endif
    const mqttPoint = new MqttClient(mqttUrl, (topic, data) => {
      dataPointList.value.forEach((point) => {
        if (point.dataPointName === data.name) {
          point.value = data.v;
        }
      });
    }, res.data);
    hubMqttQueue.value.push(mqttPoint);
    dataList.value.forEach((item) => {
      mqttPoint.handleConnect(`nup/system/tagValue/${item.dataPointName}`);
    });
  }
  catch (error) {
    error.message && showNotify({
      message: error.message,
      type: 'warning',
    });
  }
};

// 关闭HUB mqtt
const myCloseHubMqtt = () => {
  hubMqttQueue.value.forEach((item) => {
    item.endMqtt();
  });
  hubMqttQueue.value = [];
};

function myMqttCreate() {
  if (equipmentInfo?.value.acquisitionPlatform?.value === 'hub') {
    hubMqttQueue.value.length === 0 && createHUBMqtt();
  }
  if (equipmentInfo?.value.acquisitionPlatform?.value === 'supCon') {
    !mqReport.value && createMqtt();
  }
};
const closeMqtt = () => {
  myClearInterval();
  myCloseHubMqtt();
};

// 返回
const toBack = () => {
  myClearInterval();
  uni.navigateBack();
};

onLoad(async (e) => {
  // #ifdef APP-PLUS
  const query = Object.fromEntries(Object.keys(e)
    .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
  queryInfo.value = query;
  // #endif
  // #ifdef H5
  queryInfo.value = e;
  // #endif
  await getEquipmentInfo();
  const userInfo = getStorageSync(USER_INFO);
  userId.value = userInfo?.userId * 1;
});

onHide(() => {
  closeMqtt();
});

onShow(() => {
  myMqttCreate();
});

onUnload(() => {
  closeMqtt();
});

onUnmounted(() => {
  closeMqtt();
});

const tableProps = reactive({
  pagination: false,
  border: false,
  tableColProps: [
    {
      prop: 'showName',
      label: t('设备数据'),
      width: 200,
      thProps: {
        align: 'left',
      },
    },
    {
      prop: 'selectPoint',
      label: t('记录方式'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        return (
          <BMFormSelect
            v-model={row.type}
            title={t('采集方式')}
            options={[
              {
                label: t('设备采集'),
                value: 1,
              },
              {
                label: t('手动录入'),
                value: 2,
              },
            ]}
          />
        );
      },
    },
    {
      prop: 'selectValue',
      label: t('数据值'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        if (row.type === 2) {
          return (
            <WdInput v-model={row.dataPointValue} no-border type="number" custom-class="select-point-input" use-suffix-slot>
              {{
                suffix: () => (
                  <BMIcon
                    name="kebianji"
                    size="11.72rpx"
                    color="#2871FF"
                  />
                ),
              }}
            </WdInput>
          );
        }
        else {
          return <text>{ row.dataPointValue != null && row.dataPointValue !== undefined ? row.dataPointValue : '-' }</text>;
        }
      },
    },
    {
      prop: 'time',
      label: t('记录时间'),
      width: 200,
      thProps: {
        align: 'left',
      },
      customRender: ({ row }) => {
        if (row.type === 2) {
          return <text>-</text>;
        }
        else {
          return <text>{ row.timeStamp || '' }</text>;
        }
      },
    },
  ],
});
</script>

<style lang="scss" scoped>
:deep(.tip-popup .modal-container .modal-content) {
  min-height: 44.53rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14.07rpx;
}
:deep(.select-point-input) {
  .wd-input__body {
    border-bottom: none;
  }
}

.right-content {
  font-size: 15.24rpx;
  color: #2871ff;
}
// 内容区域样式
.content {
  width: 100%;
  padding: 9.38rpx 0;
  box-sizing: border-box;
  .equipment-info {
    width: 100%;
    border-radius: 4.69rpx;
    background: #ffffff;
    padding: 9.38rpx;
    box-sizing: border-box;
    overflow: hidden;
    margin-bottom: 9.38rpx;

    .name {
      display: flex;
      font-weight: 500;
      font-size: 14.06rpx;
      color: #2871ff;
      gap: 4.69rpx;
    }
    .info {
      width: 100%;
      line-height: 14.06rpx;
      font-size: 11.72rpx;
      font-weight: 500;
      color: #6c6e73;
      margin: 5.86rpx 0 9.38rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .info_data {
        width: calc(100% - 60.88rpx);
        color: #242526;
      }
    }
    .data {
      width: 100%;
      overflow-x: auto;
      display: flex;
      flex-wrap: nowrap;
      gap: 9.38rpx;
      .dataPoint {
        background-color: #ebf3ff;
        width: 93.75rpx;
        height: 45.7rpx;
        padding: 7.03rpx 0;
        box-sizing: border-box;
        border-radius: 4.69rpx;
        display: flex;
        flex-direction: column;
        align-items: center;
        flex: none;
        .point-name {
          font-size: 10.55rpx;
          font-weight: 500;
          color: #6c6e73;
        }
        .point-value {
          font-size: 11.72rpx;
          font-weight: 500;
          color: #242526;
          margin-top: 5.86rpx;
        }
      }
    }
  }
  .collection-info {
    width: 100%;
    height: 100%;
    background: #ffffff;
    border-radius: 4.69rpx;
    padding: 9.38rpx;
    box-sizing: border-box;
    .title {
      height: 30.41rpx;
      font-size: 12.89rpx;
      font-weight: 500;
      color: #18191a;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .history-title {
      display: flex;
      align-items: center;
      .history-input {
        width: 234.38rpx;
        height: 37.5rpx;
        border: 1px solid #e1e3e5;
        display: flex;
        align-items: center;
        padding: 11.13rpx;
        box-sizing: border-box;
        border-radius: 4.69rpx;
        margin-right: 15.82rpx;
      }
      .history-segmented {
        width: 187.5rpx;
        height: 28.13rpx;
        border-radius: 58.59rpx;
        background-color: #edeff2;
        :deep(.wd-segmented__item) {
          display: flex;
          justify-content: center;
          align-items: center;
          border-radius: 58.59rpx;
          color: #6c6e73;
        }
        :deep(.is-active) {
          color: #2871ff;
        }
        :deep(.wd-segmented__item--active) {
          background-color: none;
          border-radius: 58.59rpx;
        }
      }
    }
    .table-container {
      padding: 9.38rpx 0 0;
      :deep(.wd-input) {
        background-color: transparent;
      }
      :deep(.right-box .bmos-app-icon::before) {
        color: #2871ff !important;
      }
      :deep(.bm-table-container .uni-table .header) {
        z-index: 10;
      }
    }
  }
}
:deep(.wd-table__cell) {
  overflow: hidden;
}
:deep(.wd-input) {
  border: none !important;
}
:deep(.wd-popup) {
  .toast-confirm {
    width: 246.09rpx;
    .wd-message-box__title {
      color: #242526;
      font-weight: 500;
      size: 14.06rpx;
    }
    .wd-message-box__content {
      color: #9da0a6;
      font-weight: 500;
      size: 11.72rpx;
    }
    .wd-message-box__actions {
      padding: 9.38rpx;
      .is-info {
        color: #6c6e73;
        background-color: #fff;
        border: #b6b9bf 1px solid;
        border-radius: 4.69rpx;
        padding: 16px;
        height: 42.19rpx;
      }
      .is-primary {
        background-color: #2871ff;
        color: #fff;
        border-radius: 4.69rpx;
        padding: 16px;
        height: 42.19rpx;
      }
    }
  }
}
.form_box {
  margin-top: 9.38rpx;
}
</style>
