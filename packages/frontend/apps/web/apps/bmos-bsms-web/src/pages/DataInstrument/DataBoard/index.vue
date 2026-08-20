<!-- 数据看板 -->
<template>
  <div style="width: 100%; height: 100%; overflow: auto">
    <Card style="width: 100%">
      <Row justify="space-between">
        <Col v-for="item in statictiscList1" :key="item.title" :span="4">
          <StatictiscCom v-bind="item" :value="cntData[item.key]"></StatictiscCom>
        </Col>
      </Row>
      <Row justify="space-between">
        <Col v-for="item in statictiscList2" :key="item.title" :span="4">
          <StatictiscCom v-bind="item" :value="cntData[item.key]"></StatictiscCom>
        </Col>
      </Row>
    </Card>
    <Card style="width: 100%; height: calc(50% - 86px); margin: 16px 0">
      <template #title>
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px">
          <BMTableTitle :title="t('不合格数据统计')" />
          <DatePicker
            v-model:value="year"
            format="YYYY"
            value-format="YYYY"
            picker="year"
            :allow-clear="false"
            :disabled-date="disabledDate"
            @change="getBarData" />
        </div>
      </template>
      <Echart :options="unqualifiedOptions" width="100%" height="97%"></Echart>
    </Card>
    <Flex :gap="16" :flex="0" style="height: calc(50% - 86px)">
      <Card style="width: 100%; height: 100%">
        <template #title>
          <BMTableTitle :title="t('血浆类型统计')" />
        </template>
        <Echart :options="typeOptions" width="100%" height="90%"></Echart>
      </Card>
      <Card style="width: 100%; height: 100%">
        <template #title>
          <BMTableTitle :title="t('库存预警数量统计')" />
        </template>
        <Echart :options="inventoryOptions" width="100%" height="90%"></Echart>
      </Card>
    </Flex>
  </div>
</template>

<script setup lang="tsx">
  import type { EChartsOption } from 'echarts';
  import { BMTableTitle } from '@bmos/components';
  import StatictiscCom from './statictiscCom.vue';
  import Card from '@/components/Card/index.vue';
  import Echart from '@/components/Echart/index.vue';
  import { Flex, Row, Col, DatePicker, message } from 'ant-design-vue';
  import {
    getUnqualifiedPlasmaStatistic,
    getQuarantineResultStatistic,
    getStockWarningStatistic,
    getMaterialBatchStatistic,
  } from '@/services';
  import { t } from '@bmos/i18n';
  import { Dayjs } from 'dayjs';

  defineOptions({
    name: 'DataBoard',
  });

  const year = ref(new Date().getFullYear() + '');

  const barData = ref<any>({});

  const getBarData = async (year: string | Dayjs) => {
    try {
      const { data } = await getUnqualifiedPlasmaStatistic({ year });
      barData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const disabledDate = (current: Dayjs) => {
    return current && current.toDate() > new Date();
  };

  const cntData = ref<any>({});

  const getCntData = async () => {
    try {
      const { data } = await getMaterialBatchStatistic();
      cntData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  type statictiscListType = {
    title: string;
    key: string;
    icon: '' | 'currentPlasma' | 'currentSample' | 'outbound' | 'sortingPlasma' | 'waitingPlasma' | 'waitingSample';
    slot?: () => any;
  };

  // 数值统计
  const statictiscList1 = ref<statictiscListType[]>([
    {
      title: t('待入库标本（批次）'),
      key: 'warehousingSampleBatch',
      icon: 'waitingSample',
    },
    {
      title: t('待入库血浆（批）'),
      key: 'warehousingPlasmaBatch',
      icon: 'waitingPlasma',
    },
    {
      title: t('当前血浆库存（批）'),
      key: 'plasmaBatchBalance',
      icon: 'currentPlasma',
    },
    {
      title: t('当前标本库存（批）'),
      key: 'sampleBatchBalance',
      icon: 'currentSample',
    },
    {
      title: t('待分拣血浆（份）'),
      key: 'sortingPlasma',
      icon: 'sortingPlasma',
    },
    {
      title: t('待出库（批）'),
      key: 'outboundPlasmaBatch',
      icon: 'outbound',
    },
  ]);

  const statictiscList2 = ref<statictiscListType[]>([
    {
      title: t('待入库标本（份）'),
      key: 'warehousingSample',
      icon: 'waitingSample',
    },
    {
      title: t('待入库血浆（份）'),
      key: 'warehousingPlasma',
      icon: 'waitingPlasma',
    },
    {
      title: t('当前血浆库存（份）'),
      key: 'plasmaBalance',
      icon: 'currentPlasma',
      slot: () => (
        <span style={{ color: '#2871FF', fontSize: '12px', fontWeight: 500, fontFamily: 'Source Han Sans CN' }}>{`${t(
          '总重量',
        )}：${cntData.value?.totalPlasmaWeight ?? 0} KG`}</span>
      ),
    },
    {
      title: t('当前标本库存（份）'),
      key: 'sampleBalance',
      icon: 'currentSample',
    },
    {
      title: t('已分拣血浆（份）'),
      key: 'sortedPlasma',
      icon: 'sortingPlasma',
    },
    {
      title: t('待出库（份）'),
      key: 'outboundPlasma',
      icon: 'outbound',
    },
  ]);

  // 不合格数据统计
  const unqualifiedOptions = computed<EChartsOption>(() => ({
    aria: {
      enabled: true,
    },
    grid: {
      left: '4%',
      right: '2%',
    },
    xAxis: {
      type: 'category',
      data: [
        t('1月'),
        t('2月'),
        t('3月'),
        t('4月'),
        t('5月'),
        t('6月'),
        t('7月'),
        t('8月'),
        t('9月'),
        t('10月'),
        t('11月'),
        t('12月'),
      ],
    },
    yAxis: {
      type: 'value',
      name: `(${t('份')})`,
    },
    tooltip: {
      trigger: 'axis',
    },
    legend: {
      data: [t('浆站不合格检测'), t('企业不合格检测')],
      right: 0,
    },
    series: [
      {
        name: t('浆站不合格检测'),
        type: 'bar',
        data: barData.value?.stationTotal?.map((item: any) => item.totalNum) ?? [],
        barWidth: 21,
        color: '#2681FF',
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
        },
      },
      {
        name: t('企业不合格检测'),
        type: 'bar',
        data: barData.value?.enterpriseTotal?.map((item: any) => item.totalNum) ?? [],
        barWidth: 21,
        color: '#FFBB33',
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
        },
      },
    ],
  }));

  const quarantineResult = ref<any>([]);

  const getQuarantineResultData = async () => {
    try {
      const { data } = await getQuarantineResultStatistic();
      quarantineResult.value = data?.map((item: any) => {
        return {
          value: item.totalNum,
          name: item.plasmaStatus?.name,
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 血浆类型统计
  const typeOptions = computed<EChartsOption>(() => ({
    aria: {
      enabled: true,
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      top: '2%',
      right: 'right',
    },
    color: ['#59BF78', '#FF5633', '#FFBB33'],
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: true,
          lineHeight: 16,
          formatter: '{b}\n{d}%',
        },
        labelLine: {
          show: true,
        },
        data: quarantineResult.value,
      },
    ],
  }));

  const inventoryData = ref<any>({});

  const getInventoryData = async () => {
    try {
      const { data } = await getStockWarningStatistic();
      inventoryData.value = data;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 库存预警数量统计
  const inventoryOptions = computed<EChartsOption>(() => ({
    aria: {
      enabled: true,
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      top: '2%',
      right: 'right',
    },
    color: ['#2681FF', '#FFBB33'],
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: true,
          lineHeight: 16,
          formatter: '{b}\n{d}%',
        },
        labelLine: {
          show: true,
        },
        data: [
          { value: inventoryData.value?.plasmaWarning ?? 0, name: t('血浆') },
          { value: inventoryData.value?.sampleWarning ?? 0, name: t('标本') },
        ],
      },
    ],
  }));

  onActivated(() => {
    getBarData(year.value);
    getQuarantineResultData();
    getInventoryData();
    getCntData();
  });
  onMounted(() => {
    getBarData(year.value);
    getQuarantineResultData();
    getInventoryData();
    getCntData();
  });
</script>

<style scoped></style>
