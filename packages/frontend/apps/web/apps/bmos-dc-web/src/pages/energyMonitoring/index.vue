<template>
  <div class="energy-monitoring">
    <div v-if="activePage === 'chart'" class="energy-monitoring-container">
      <div class="item electricity-chart">
        <div class="title">{{ t('电力耗能趋势') }}</div>
        <div class="content">
          <div class="operation">
            <div class="left">{{ t('单位') }}: {{ 'KW.h' }}</div>
            <div class="right">
              <Button
                :class="[electricityChartPicker === 'date' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeElectricityChartPicker('date')">
                {{ t('日') }}
              </Button>
              <Button
                :class="[electricityChartPicker === 'month' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeElectricityChartPicker('month')">
                {{ t('月') }}
              </Button>
              <Button
                :class="[electricityChartPicker === 'year' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeElectricityChartPicker('year')">
                {{ t('年') }}
              </Button>
              <DatePicker
                v-model:value="electricityChartDate"
                :picker="electricityChartPicker"
                valueFormat="YYYY-MM-DD"
                :allowClear="false"
                @change="() => setElectricityChartData()">
                <template #suffixIcon>
                  <BMIcons
                    icon="PickerSuffixIcon"
                    :style="{
                      fontSize: '16px',
                      width: '16px',
                      height: '16px',
                    }" />
                </template>
              </DatePicker>
            </div>
          </div>
          <div ref="electricityChartRef" class="chart" style="height: 100%">
            <Echarts
              v-if="electricityChartIsInit"
              :options="electricityChartOptions"
              :height="electricityChartHeight" />
          </div>
        </div>
      </div>
      <div class="item heating-chart">
        <div class="title">{{ t('集中供热量趋势') }}</div>
        <div class="content">
          <div class="operation">
            <div class="left">{{ t('单位') }}: {{ 'MJ' }}</div>
            <div class="right">
              <Button
                :class="[heatingChartPicker === 'date' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeHeatingChartPicker('date')">
                {{ t('日') }}
              </Button>
              <Button
                :class="[heatingChartPicker === 'month' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeHeatingChartPicker('month')">
                {{ t('月') }}
              </Button>
              <Button
                :class="[heatingChartPicker === 'year' ? 'active-btn' : '', 'operation-btn']"
                @click="() => changeHeatingChartPicker('year')">
                {{ t('年') }}
              </Button>
              <DatePicker
                v-model:value="heatingChartDate"
                :picker="heatingChartPicker"
                valueFormat="YYYY-MM-DD"
                :allowClear="false"
                @change="() => setHeatingChartData()">
                <template #suffixIcon>
                  <BMIcons
                    icon="PickerSuffixIcon"
                    :style="{
                      fontSize: '16px',
                      width: '16px',
                      height: '16px',
                    }" />
                </template>
              </DatePicker>
            </div>
          </div>
          <div ref="heatingChartRef" class="chart" style="height: 100%">
            <Echarts v-if="heatingChartIsInit" :options="heatingChartOptions" :height="heatingChartHeight" />
          </div>
        </div>
      </div>
      <div class="item electricity-table">
        <div class="title">{{ t('电力耗能统计') }}</div>
        <div class="content">
          <div class="operation">
            <div class="left"></div>
            <div class="right">
              <Button
                :class="[electricityTablePicker === 'date' ? 'active-btn' : '', 'operation-btn']"
                @click="() => electricityTableChangePicker('date')">
                {{ t('日') }}
              </Button>
              <Button
                :class="[electricityTablePicker === 'month' ? 'active-btn' : '', 'operation-btn']"
                @click="() => electricityTableChangePicker('month')">
                {{ t('月') }}
              </Button>
              <Button
                :class="[electricityTablePicker === 'year' ? 'active-btn' : '', 'operation-btn']"
                @click="() => electricityTableChangePicker('year')">
                {{ t('年') }}
              </Button>
              <DatePicker
                v-model:value="electricityTableDate"
                :picker="electricityTablePicker"
                valueFormat="YYYY-MM-DD"
                :allowClear="false">
                <template #suffixIcon>
                  <BMIcons
                    icon="PickerSuffixIcon"
                    :style="{
                      fontSize: '16px',
                      width: '16px',
                      height: '16px',
                    }" />
                </template>
              </DatePicker>
            </div>
          </div>
          <BMTable
            ref="electricityTableRef"
            :columns="electricityColumns"
            :dataRequest="electricityRequest"
            :extraParams="electricityExtraParams"
            :pagination="false"
            :search="false"
            :showToolBar="false"
            :scroll="{ x: 400, y: 300 }" />
        </div>
      </div>
      <div class="item heating-table">
        <div class="title">{{ t('集中供热量统计') }}</div>
        <div class="content">
          <div class="operation">
            <div class="left"></div>
            <div class="right">
              <Button
                :class="[heatingTablePicker === 'date' ? 'active-btn' : '', 'operation-btn']"
                @click="() => heatingTableChangePicker('date')">
                {{ t('日') }}
              </Button>
              <Button
                :class="[heatingTablePicker === 'month' ? 'active-btn' : '', 'operation-btn']"
                @click="() => heatingTableChangePicker('month')">
                {{ t('月') }}
              </Button>
              <Button
                :class="[heatingTablePicker === 'year' ? 'active-btn' : '', 'operation-btn']"
                @click="() => heatingTableChangePicker('year')">
                {{ t('年') }}
              </Button>
              <DatePicker
                v-model:value="heatingTableDate"
                :picker="heatingTablePicker"
                valueFormat="YYYY-MM-DD"
                :allowClear="false">
                <template #suffixIcon>
                  <BMIcons
                    icon="PickerSuffixIcon"
                    :style="{
                      fontSize: '16px',
                      width: '16px',
                      height: '16px',
                    }" />
                </template>
              </DatePicker>
            </div>
          </div>
          <BMTable
            ref="heatingTableRef"
            :columns="heatingColumns"
            :dataRequest="heatingRequest"
            :extraParams="heatingExtraParams"
            :pagination="false"
            :search="false"
            :showToolBar="false"
            :scroll="{ x: 400, y: 300 }" />
        </div>
      </div>
    </div>
    <div v-if="activePage === 'energyFlow'" class="energy-flow-container">
      <div ref="flowChartRef" class="chart" style="height: 100%">
        <Echarts v-if="flowChartIsInit" :options="flowChartOptions" :height="flowChartHeight" />
      </div>
    </div>
    <div class="energy-monitoring-footer">
      <Button
        :class="[activePage === 'energyFlow' ? 'footer-active-btn' : '', 'footer-operation-btn']"
        @click="() => changeActivePage('energyFlow')">
        {{ t('能流图') }}
      </Button>
      <Button
        :class="[activePage === 'chart' ? 'footer-active-btn' : '', 'footer-operation-btn']"
        @click="() => changeActivePage('chart')">
        {{ t('用能趋势') }}
      </Button>
    </div>
    <div class="footer"></div>
  </div>
</template>
<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import { useElectricityChart, useElectricityTable, useFlowChart, useHeatingChart, useHeatingTable } from './hooks';
  import Echarts from '@/components/Echarts/index.vue';
  import { BMTable } from '@bmos/components';

  const {
    electricityChartDate,
    electricityChartPicker,
    electricityChartRef,
    electricityChartOptions,
    changeElectricityChartPicker,
    setElectricityChartInit,
    electricityChartHeight,
    setElectricityChartData,
    electricityChartIsInit,
  } = useElectricityChart();

  const {
    heatingChartDate,
    heatingChartPicker,
    heatingChartRef,
    heatingChartOptions,
    changeHeatingChartPicker,
    setHeatingChartInit,
    heatingChartHeight,
    setHeatingChartData,
    heatingChartIsInit,
  } = useHeatingChart();

  const {
    electricityTableRef,
    electricityColumns,
    electricityTableDate,
    electricityTablePicker,
    electricityTableChangePicker,
    electricityExtraParams,
    electricityRequest,
  } = useElectricityTable();

  const {
    heatingTableRef,
    heatingColumns,
    heatingTableDate,
    heatingTablePicker,
    heatingTableChangePicker,
    heatingExtraParams,
    heatingRequest,
  } = useHeatingTable();

  const activePage = ref('energyFlow');
  const changeActivePage = async (page: string) => {
    activePage.value = page;
    await nextTick();
    if (page === 'energyFlow') {
      flowChartInit();
    } else {
      setElectricityChartInit();
      setHeatingChartInit();
    }
  };

  const { flowChartOptions, flowChartInit, flowChartHeight, flowChartRef, flowChartIsInit } = useFlowChart();

  onMounted(() => {
    flowChartInit();
  });
</script>
<style lang="less">
  .dc-content {
    padding: 0px !important;
    background: url(./assets/background.svg) #333b45 50% / cover no-repeat;
  }
  .energy-monitoring-container {
    .bmos-table {
      flex: 1;
      overflow: auto;
      width: 100%;
    }
    .bmos-table .dc-table-body {
      border-bottom: none !important;
    }
    .dc-table-wrapper .dc-table {
      background: transparent;
      color: #fff;
    }
    .dc-table-wrapper .dc-table-thead > tr > th {
      border-bottom: 1px solid rgba(153, 204, 255, 0.2);
      background: rgb(46, 70, 108);
      color: #b9e8ff;
    }
    .dc-table-wrapper
      .dc-table-thead
      > tr
      > th:not(:last-child):not(.dc-table-selection-column):not(.dc-table-row-expand-icon-cell):not([colspan])::before {
      background-color: #3b78b5;
    }
    .dc-table-wrapper .dc-table-cell-scrollbar:not([rowspan]) {
      box-shadow: none;
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr > td {
      border-top: 1px solid rgb(46, 70, 108);
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr:nth-child(even) {
      background: rgba(45, 166, 255, 0.05);
    }
    .dc-table-wrapper .dc-table:not(.dc-table-bordered) .dc-table-tbody > tr:last-child > td {
      border-bottom: 1px solid rgba(153, 204, 255, 0.2);
    }
    .dc-table-wrapper .dc-table-tbody > tr > td {
      transition: background 2s;
    }
    .dc-table-wrapper .dc-table-tbody > tr.dc-table-row:hover > td {
      background: rgba(51, 170, 255, 0.1);
    }
    .dc-table-wrapper .dc-table-cell-fix-left {
      background: rgb(42, 48, 60);
    }
    // .dc-table-wrapper .dc-table-cell-fix-left 的 偶数行 背景色
    .dc-table-wrapper .dc-table-cell-fix-left:nth-child(even) {
      background: rgba(221, 137, 11, 0.05);
    }
    .dc-table-wrapper .dc-table-tbody > tr.dc-table-placeholder:hover > td {
      background: transparent;
    }
    .bmos-table .dc-table-wrapper .dc-table-pagination.dc-pagination {
      color: #fff;
    }
    .dc-pagination .dc-pagination-item a {
      color: #fff;
    }
    .dc-pagination .dc-pagination-item-active {
      background-color: rgba(51, 170, 255, 0.1);
    }
    .anticon {
      color: #fff;
    }
    ::-webkit-scrollbar {
      width: 0;
    }
  }
</style>
<style lang="less" scoped>
  .energy-monitoring {
    display: flex;
    flex-direction: column;
    // gap: 20px;
    height: 100%;
    width: 100%;
  }
  .energy-monitoring-footer {
    display: flex;
    justify-content: center;
    gap: 20px;
    align-items: center;
    margin-bottom: 24px;
    margin-top: 38px;
  }
  .footer {
    width: 100%;
    height: 20px;
    position: absolute;
    left: 0;
    bottom: 0;
    background-image: url('/src/assets/img/footer.png');
    background-size: 100% 100%;
  }
  .energy-monitoring-container {
    height: 100%;
    width: 100%;
    color: #fff;
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-template-rows: repeat(2, 1fr);
    grid-column-gap: 40px;
    grid-row-gap: 20px;
  }
  .energy-flow-container {
    height: 100%;
    width: 100%;
    color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    .chart {
      height: 90%;
      width: 90%;
    }
  }
  .item {
    .title {
      height: 30px;
      background: url(./assets/title.svg) no-repeat;
      // background-size: contain;
      color: #cce8ff;
      /* 16px-加粗 */
      font-family: 'Source Han Sans CN';
      font-size: 16px;
      font-style: normal;
      font-weight: 500;
      line-height: 20px; /* 125% */
      padding-left: 40px;
    }
    .content {
      display: flex;
      flex-direction: column;
      height: calc(100% - 30px);
      overflow: hidden;
      border: 1px solid rgba(72, 177, 255, 0.3);
      background: rgba(11, 12, 13, 0.3);
    }
    .operation {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 14px 24px;
      .left {
        color: var(---, #86a7bf);
        text-align: right;

        /* 14px-正文 */
        font-family: 'Source Han Sans CN';
        font-size: 14px;
        font-style: normal;
        font-weight: 400;
        line-height: 18px; /* 128.571% */
      }
      .right {
        display: flex;
        gap: 24px;
      }
    }
    .chart {
      flex: 1;
      overflow: hidden;
    }
    .bmos-table {
      flex: 1;
      overflow: auto;
      width: 100%;
      padding: 0 24px 22px 24px;
    }
  }

  .electricity-chart {
    grid-area: 1 / 1 / 2 / 2;
  }
  .heating-chart {
    grid-area: 1 / 2 / 2 / 3;
  }
  .electricity-table {
    grid-area: 2 / 1 / 3 / 2;
    overflow: hidden;
  }
  .heating-table {
    grid-area: 2 / 2 / 3 / 3;
    overflow: hidden;
  }
  :deep(.footer-operation-btn) {
    border: none;
    width: 130px;
    height: 32px;
    cursor: pointer;
    display: inline-flex;
    align-items: center;
    color: #fff;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 20px;
    font-style: normal;
    font-weight: bold;
    line-height: normal;
    letter-spacing: 1.1px;
    padding: 10px 20px;
    justify-content: center;
    background: url(./assets/footerBtn.svg) no-repeat;
    background-position-y: bottom;
    transition: background 0.3s ease;
    color: #8fb2cc;
    transform: skewX(-15deg); /* 倾斜效果 */
    border-radius: 10px;
    span {
      transform: skewX(-15deg); /* 倾斜效果 */
    }
  }
  :deep(.footer-active-btn) {
    background: url(./assets/footerActiveBtn.svg) no-repeat;
    background-size: cover;
    background-position-y: bottom;
    color: #fff;
  }
  :deep(.operation-btn) {
    border: none;
    color: #86a7bf;
    width: 60px;
    height: 30px;
    cursor: pointer;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background: url(./assets/btn.svg) no-repeat;
    background-size: cover;
    transition: background 0.3s ease;
  }
  :deep(.active-btn) {
    background: url(./assets/activeBtn.svg) no-repeat;
    background-size: cover;
  }
  :deep(.dc-picker) {
    background: url(./assets/picker.svg) no-repeat;
    background-size: cover;
    border: none;
    width: 120px;
    height: 30px;
    flex-shrink: 0;
    border-radius: 0;
    .dc-picker-input > input {
      color: #fff;
    }
  }
  :deep(.dc-form-item .dc-form-item-label > label) {
    color: #fff;
  }
  :deep(.dc-select:not(.dc-select-customize-input) .dc-select-selector) {
    border-radius: 4px;
    border: 1px solid rgba(65, 159, 255, 0.3);
    background: rgba(204, 229, 255, 0.15);
  }
  :deep(.dc-select-single .dc-select-selector) {
    color: #fff;
  }
  :deep(.dc-select) {
    color: #fff;
  }
  :deep(.dc-select-selection-item-remove .anticon-close) {
    color: #fff;
  }
  :deep(.dc-select .dc-select-clear) {
    background: transparent;
  }
  :deep(.dc-select .dc-select-clear:hover) {
    color: #fff;
  }
</style>
