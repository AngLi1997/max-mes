<template>
  <div class="bai-e-title-box" :class="{ 'title-box-bg': isProcessProgress }">
    <div style="display: flex; flex: 1">
      <div class="title-left">
        <img
          v-if="isProcessProgress"
          :src="images[`/src/assets/baiePng/${currentLng}/processProgressTitle.png`]?.default"
          alt=""
          srcset=""
          width="100%"
          height="100%" />
        <img
          v-else
          :src="images[`/src/assets/baiePng/${currentLng}/title.png`]?.default"
          alt=""
          srcset=""
          width="100%"
          height="100%" />
      </div>
      <div
        class="title-center"
        :class="{ 'title-center-ru': currentLng === 'ru_RU', 'process-progress': isProcessProgress }">
        <div
          v-if="isProcessProgress"
          :class="['sci-fi-button', currentLng === 'ru_RU' ? 'sci-fi-button-ru' : '']"
          @click="returnProcess">
          {{ t('返回') }}
        </div>
        <template v-else>
          <div
            :class="{ 'button-active': activeKey === '0', 'width-176': currentLng === 'ru_RU' }"
            class="tab-item"
            @click="handleChangeActiveKey('0')">
            <div class="tab-activated"></div>
            <div
              :class="{
                'text-activated': activeKey === '0',
                'text-item': activeKey !== '0',
              }">
              {{ t('态势总览') }}
            </div>
            <div class="tab-activated tab-activated-right"></div>
          </div>
          <div
            :class="{ 'button-active': activeKey === '1', 'width-206': currentLng === 'ru_RU' }"
            class="tab-item"
            @click="handleChangeActiveKey('1')">
            <div class="tab-activated"></div>
            <div
              :class="{
                'text-activated': activeKey === '1',
                'text-item': activeKey !== '1',
              }">
              {{ t('工艺透视') }}
            </div>
            <div class="tab-activated tab-activated-right"></div>
          </div>
          <div
            :class="{ 'button-active': activeKey === '2', 'width-206': currentLng === 'ru_RU' }"
            class="tab-item"
            @click="handleChangeActiveKey('2')">
            <div class="tab-activated"></div>
            <div
              :class="{
                'text-activated': activeKey === '2',
                'text-item': activeKey !== '2',
              }">
              {{ t('分层智控') }}
            </div>
            <div class="tab-activated tab-activated-right"></div>
          </div>
        </template>
      </div>
    </div>

    <div class="title-right">
      <Select v-model:value="languageValue" size="small" @change="handleChangeLanguage">
        <SelectOption value="zh_CN">中文</SelectOption>
        <SelectOption value="ru_RU">Беларуская</SelectOption>
      </Select>
      <div class="vertical-line"></div>
      <div class="date-box">
        <span class="time">
          {{ time }}
        </span>
        <span class="date">
          {{ date }}
        </span>
        &nbsp;
        <span class="date">
          {{ t(today) }}
        </span>
      </div>
    </div>
  </div>
  <div class="common-style">
    <!-- 态势总览 -->
    <OverviewSituation v-if="activeKey === '0'"></OverviewSituation>
    <!-- 工艺透视 -->
    <ProcessPerspective
      v-if="activeKey === '1'"
      ref="processPerspectiveRef"
      @sendMessage="sendMessage"
      @toProcessProgress="toProcessProgress"
      @closeProcess="processClose" />
    <!-- 分层智控 -->
    <LayeredIntelligentControl
      v-if="activeKey === '2'"
      ref="layeredIntelligentControlRef"
      :defaultFloor="defaultFloor"
      @sendMessage="sendMessage"
      @closeProcess="processClose"></LayeredIntelligentControl>
  </div>
  <div v-if="isProcessProgress" class="container">
    <ProcessDetail :typeActive="activeKeyProcess" />
  </div>
  <!-- 工序信息 -->
  <ProcessInfoDetail v-if="showProcessDetail" :processList="processList" @close="processClose"></ProcessInfoDetail>
</template>

<script setup lang="ts">
  import LayeredIntelligentControl from './components/LayeredIntelligentControl/index.vue';
  import { t, changeLanguage, currentLng, I18nLanguageType } from '@bmos/i18n';
  import { useTime } from './hooks/useTime';
  import ProcessPerspective from './components/ProcessPerspective/index.vue';
  import OverviewSituation from './components/OverviewSituation/index.vue';
  import { SelectValue } from 'ant-design-vue/es/select';
  import ProcessDetail from './components/ProcessPerspective/Detail.vue';
  import { TypeEnum } from './components/ProcessPerspective/type';
  import { updateLangResource } from '@/utils/i18n';
  import ProcessInfoDetail from './components/LayeredIntelligentControl/components/ProcessInfoDetail.vue';
  import { reqQueryBelarusDashboardDataProcedure } from '@/services';

  const images = import.meta.glob('@/assets/baiePng/*/*.png', { eager: true });
  const emit = defineEmits(['sendMessage']);

  const { time, date, today } = useTime();
  const languageValue = ref(currentLng.value);
  const layeredIntelligentControlRef = ref<InstanceType<typeof LayeredIntelligentControl>>();
  const processPerspectiveRef = ref<InstanceType<typeof ProcessPerspective>>();
  const activeKey = ref(sessionStorage.getItem('BAIE_TAB') || '0'); // 0: 态势总览 1: 工艺透视 2: 分层智控
  const defaultFloor = ref(1);

  const timer = ref(null);

  // 模型id
  const modelCode = ref('');

  // 模型ids
  const modelCodes = ref<string[]>(null);

  // 工序信息汇总
  const allProcessInfo = ref<any>({});

  // 显示工序详情
  const showProcessDetail = ref(false);

  // 模型是否加载完成
  const modelLoadCompleted = ref<boolean>(false);

  const processList = computed(() => {
    // 判断modelCodes是否为数组
    if (Array.isArray(modelCodes.value)) {
      const arr = [];
      modelCodes.value.forEach(id => {
        const result = allProcessInfo.value[id] || {};
        arr.push(result);
      });
      return arr;
    }
    const result = allProcessInfo.value[modelCode.value] || {};
    return [result];
  });

  // 工序详情关闭
  const processClose = () => {
    showProcessDetail.value = false;
    modelCodes.value = null;
    modelCode.value = '';
    emit('sendMessage', {
      type: 'closeDetail',
    });
  };

  // 查询工序信息
  const queryProcessInfo = async () => {
    const res = await reqQueryBelarusDashboardDataProcedure({
      modelCodes: [
        // 人血白蛋白
        'SM_4楼设备_融浆罐（R0101_0',
        'SM_4楼设备_反应罐1___R0201_0',
        'SM_4楼设备_1_压滤机（X0208）_0',
        'SM_4楼设备_反应罐2___R0202_1',
        'SM_4楼设备_1_压滤机（X0208）_1',
        'SM_4楼设备_反应罐2___R0202_0',
        'SM_4楼设备_1_压滤机（X0208）_2',
        'SM_4楼设备_FV滤液暂存罐_配液罐1___R0204_0',
        'SM_4楼设备_1_压滤机（X0208）_3',
        'SM_4楼设备_FV溶解罐__R0203_0',
        'SM_4楼设备_FV溶解罐__R0203_1',
        'SM_4楼设备_3_压滤机（X0224）_0',
        'SM_4楼设备11',
        'SM_3楼设备_0_9%Nacl罐（R0403）_0',
        'SM_3楼设备_巴氏灭活罐（R0402）_0',
        'SM_3楼设备01_脉动真空灭菌柜_0',
        'SM_3楼设备01_脉动真空灭菌柜_1',
        'SM_3楼设备04_灭菌隧道___M3C03_0',
        'SM_3楼设备05_灌装加塞机_M3D05_0',
        'SM_2楼设备10_多功能全自动装盒机_0',
        // 静注人免疫球蛋白
        'SM_4楼设备_反应罐3___R0301_0',
        'SM_4楼设备_反应罐4___R0302_0',
        'SM_4楼设备_2_压滤机（X0308）_0',
        'SM_4楼设备_反应罐4___R0302_1',
        'SM_4楼设备_2_压滤机（X0308）_2',
        'SM_4楼设备_FII滤液暂存罐_配液罐2___R0304_0',
        'SM_4楼设备_2_压滤机（X0308）_1',
        'SM_4楼设备_FII溶解罐__R0303_0',
        'SM_4楼设备_3_压滤机（X0224）_1',
        'SM_4楼设备_3_压滤机（X0224）_2',
        'SM_3楼设备10_超滤模块（X0506）_0',
        'SM_3楼设备14_脉动真空灭菌柜_1',
        'SM_3楼设备14_脉动真空灭菌柜_0',
        'SM_3楼设备_静免超滤罐（R0501）_0',
        'SM_3楼设备_低ph灭活罐2_（R0504）_0',
        'SM_3楼设备01_脉动真空灭菌柜_2',
        'SM_3楼设备01_脉动真空灭菌柜_3',
        'SM_3楼设备04_灭菌隧道___M3C03_1',
        'SM_3楼设备05_灌装加塞机_M3D05_1',
        'SM_2楼设备10_多功能全自动装盒机_1',
      ],
    });
    res.data.forEach((item: any) => {
      allProcessInfo.value[item.modelCode] = item;
    });
    emit('sendMessage', {
      type: 'queryProcessInfo',
      value: res.data || [],
    });
  };

  const handleChangeActiveKey = (val: string) => {
    processClose();
    activeKey.value = val;
    sessionStorage.setItem('BAIE_TAB', val);
    if (val === '1' || val === '2') {
      defaultFloor.value = 1;
      queryProcessInfo();
      if (!timer.value) {
        timer.value = setInterval(() => {
          queryProcessInfo();
        }, 30000);
      }
    } else {
      if (timer.value) {
        clearInterval(timer);
        timer.value = null;
      }
    }

    emit('sendMessage', {
      type: 'changeTab',
      value: val,
    });
  };

  const changeLanguageEmit = (val: I18nLanguageType) => {
    emit('sendMessage', {
      type: 'changeLanguage',
      value: val,
    });
  };

  const handleChangeLanguage = async (val: SelectValue) => {
    localStorage.setItem('LANG', val as string);
    await updateLangResource(val as I18nLanguageType);
    changeLanguage(val as I18nLanguageType);
    changeLanguageEmit(val as I18nLanguageType);
    localStorage.setItem('BAIE_LANG', val as I18nLanguageType);
  };
  function onMessage(data: any) {
    console.log('dc-onMessage', data);
    // 模型加载完成，切换语言
    if (data?.type === 'modalCompleted') {
      modelLoadCompleted.value = true;
      changeLanguageEmit(currentLng.value);
      queryProcessInfo();
      handleChangeActiveKey(activeKey.value);
    }
    // 切换Tab
    if (data?.type === 'changeTab' && modelLoadCompleted.value) {
      if (activeKey.value === data?.value) {
        return;
      }
      activeKey.value = data?.value;
      sessionStorage.setItem('BAIE_TAB', data?.value);
    }
    // 打开工序详情
    if (data?.type === 'openProcessDetail') {
      showProcessDetail.value = data.show;
      if (data.show) {
        modelCode.value = data.id;
        modelCodes.value = data.ids || null;
      } else {
        modelCode.value = '';
        modelCodes.value = null;
      }
    }

    // 切换楼层
    if (data?.type === 'changeFloor') {
      activeKey.value = '2';
      defaultFloor.value = data?.floor;
    }
    // 打开房间详情
    if (data?.type === 'showRoomDetail') {
      layeredIntelligentControlRef.value?.openRoomDetail(data);
    }
    // 打开设备详情
    if (data?.type === 'showDeviceDetail') {
      if (activeKey.value === '2') {
        layeredIntelligentControlRef.value?.openDeviceDetail(data);
      }
      if (activeKey.value === '1') {
        processPerspectiveRef.value?.openDeviceDetail(data);
      }
    }
    // 打开监控
    if (data?.type === 'openMonitor') {
      layeredIntelligentControlRef.value?.openMonitor(data);
    }
  }
  function sendMessage(params: any) {
    emit('sendMessage', params);
  }

  // 处理工序进度
  const isProcessProgress = ref<boolean>(false);
  const toProcessProgress = (type: TypeEnum) => {
    isProcessProgress.value = true;
    activeKeyProcess.value = type;
  };
  const activeKeyProcess = ref<TypeEnum>(TypeEnum.HUMAN_ALBUMIN);
  const returnProcess = () => {
    isProcessProgress.value = false;
  };

  defineExpose({ onMessage });

  onMounted(() => {
    handleChangeLanguage(currentLng.value);
  });
  onUnmounted(() => {
    if (timer.value) {
      clearInterval(timer);
      timer.value = null;
    }
  });
</script>

<style lang="less">
  .dc-select-dropdown {
    background: rgba(10, 31, 51, 1);
    .dc-select-item-option-content {
      color: #fff !important;
    }
    .dc-select-item-option-selected {
      background: rgba(40, 61, 81, 1) !important;
    }
  }
</style>

<style lang="less" scoped>
  .common-style {
    width: 0;
    :deep(.dc-select-selector:hover) {
      border-color: rgba(128, 202, 255, 1) !important;
    }
    :deep(.dc-select-selector) {
      height: 35px !important;
      padding: 5px 7px !important;
      background-color: #0a1f3333;
      color: #c3d7e5;
      border: none;
      border: 1px solid rgba(84, 112, 140, 1);
    }
    :deep(.dc-select-focused) {
      .dc-select-selector {
        border-color: rgba(128, 202, 255, 1) !important;
      }
    }
    :deep(.dc-select-arrow) {
      color: #80caff;
    }
    :deep(.dc-switch) {
      background-image: url('@/assets/baiePng/switch-off.png');
      background-size: 100% 100%;
      width: 70px;
      height: 30px;
      .dc-switch-handle {
        width: 26px;
        height: 26px;
        border-radius: 13px;
        background-image: url('@/assets/baiePng/switch-off-circle.png');
        background-size: 100% 100%;
      }
      .dc-switch-handle::before {
        display: none;
      }
    }
    :deep(.dc-switch-checked) {
      background-image: url('@/assets/baiePng/switch-on.png') !important;
      background-size: 100% 100% !important;
      .dc-switch-handle {
        inset-inline-start: calc(100% - 30px);
        background-image: url('@/assets/baiePng/switch-on-circle.png');
      }
    }
  }
  .bai-e-title-box {
    width: 100%;
    height: 100px;
    background-image: url('@/assets/baiePng/bg-title.png');
    background-size: 100% 100%;
    display: flex;
    justify-content: space-between;
    position: relative;
    z-index: 10;
    .width-176 {
      width: 176px;
    }
    .width-206 {
      width: 206px;
    }
    .title-left {
      width: 782px;
      height: 70px;
      flex-shrink: 0;
    }
    .title-center {
      flex: 1;
      justify-content: center;
      display: flex;
      height: 40px;
      font-family: Source Han Sans CN;
      font-weight: 500;
      font-size: 18px;
      vertical-align: middle;
      color: #98bed9;
      margin-top: 15px;
      .tab-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 4px;
        cursor: pointer;
        margin-right: 26px;
        .text-item {
          padding: 0 24px;
          text-align: center;
        }
      }
      .button-active {
        .tab-activated {
          width: 18px;
          height: 100%;
          background-image: url('@/assets/baiePng/tab-activated.png');
          background-size: cover;
        }
        .tab-activated-right {
          transform: rotate(180deg);
        }
        .text-activated {
          color: transparent;
          background-image: -webkit-linear-gradient(-90deg, #ffffff 0%, #80caff 100%);
          -webkit-background-clip: text;
          padding: 0 6px;
          text-align: center;
        }
        background: linear-gradient(
          90.16deg,
          rgba(102, 191, 255, 0) 0.15%,
          rgba(102, 191, 255, 0.15) 20.52%,
          rgba(102, 166, 255, 0.35) 50.02%,
          rgba(102, 191, 255, 0.15) 79.94%,
          rgba(102, 191, 255, 0) 99.88%
        );
      }
    }
    .process-progress {
      justify-content: end;
      margin-top: 20px;
    }
    .title-center-ru {
      font-size: 14px;
    }
    .sci-fi-button {
      // width: 110px;
      height: 32px;
      margin-right: 70px;
      font-size: 16px;
      padding: 0 20px;
      line-height: 32px;
      font-weight: bold;
      color: white;
      text-align: center;
      cursor: pointer;
      background-image: url('@/assets/baiePng/returnHome.png');
      background-size: 100% 100%;
    }
    .sci-fi-button-ru {
      font-size: 14px;
    }
    .title-right {
      height: 26px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 22px;
      margin-right: 20px;
      :deep(.dc-select-selector) {
        height: 26px !important;
        padding: 1px 7px !important;
        width: 125px !important;
        background-color: #0a1f3333;
        color: #c3d7e5;
        border: none;
      }
      :deep(.dc-select-arrow) {
        color: #80caff;
      }
      .vertical-line {
        width: 1px;
        height: 14px;
        margin: 0 15px;
        background-color: #778ea6;
      }
      .date-box {
        display: flex;
        align-items: center;
        .time {
          font-family: Bai Jamjuree;
          font-weight: 500;
          font-size: 20px;
          line-height: 100%;
          color: #ffffff;
          margin-right: 15px;
          display: inline-block;
          width: 78px;
        }
        .date {
          font-family: Source Han Sans CN;
          font-weight: 400;
          font-size: 12px;
          line-height: 16px;
          color: #b2dfff;
        }
      }
    }
  }
  .title-box-bg {
    background-color: rgba(10, 31, 51, 1);
  }
  .container {
    width: 100%;
    height: calc(100% - 100px);
    background: #191d26;
    z-index: 2;
    position: relative;
    color: #fff;
    overflow-y: auto;
  }
</style>
