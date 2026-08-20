<template>
  <!-- 报表查看弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :footer="null"
    :title="t('报表查看')"
    wrapClassName="modalSizeMedium">
    <div class="reportInfo">
      <div>{{ t('生产工艺') }} :{{ reportInfo.processName }}</div>
      <div v-if="props.reportInfo.procedureName" class="procedureName">
        {{ t('工序名称') }} :{{ reportInfo.procedureName }}
      </div>
    </div>
    <!-- echarts -->
    <div id="charts"></div>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { ref, nextTick, watch } from 'vue';
  import echarts from '@/plugins/echarts';
  import { message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  const props = defineProps({
    reportInfo: {
      type: Object,
      default: () => {},
    },
    pieChartData: {
      type: Array,
      default: () => [],
    },
  });
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const chartsData = ref<any>([]);
  const openModal = () => {
    open.value = true;
  };
  // 获取echarts图数据
  const getData = async () => {
    try {
      // const res = await jiekou();
      chartsData.value = props.pieChartData;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const initEcharts = async () => {
    const chart = echarts.init(document.getElementById('charts'));
    chart.setOption({
      tooltip: {
        trigger: 'item',
        extraCssText: 'border-style: none;', // 额外附加到浮层的 css 样式
      },
      legend: {
        bottom: '5%',
        left: 'center',
        itemGap: 27.5,
        itemWidth: 8.5,
        itemHeight: 8.5,
        textStyle: {
          fontSize: 12,
        },
      },
      series: [
        {
          name: t('统计批次'),
          type: 'pie',
          radius: ['38%', '52%'],
          center: ['50%', '40%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center',
          },
          emphasis: {
            label: {
              show: false,
            },
            scaleSize: 0,
          },
          labelLine: {
            show: false,
          },
          // data: [
          //   { value: 1048, name: '合格' },
          //   { value: 225, name: '其他' },
          //   { value: 200, name: '不合格' },
          // ],
          data: chartsData.value || [],
          itemStyle: {
            normal: {
              color(colors: any) {
                const colorList = ['rgb(15,178,124)', 'rgb(163,173,191)', 'rgb(255,86,51)'];
                return colorList[colors.dataIndex];
              },
            },
          },
        },
      ],
    });
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getData();
        initEcharts();
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal });
</script>
<style lang="less" scoped>
  .reportInfo {
    width: 100%;
    padding: 11px 14px;
    border-radius: 6px;
    font-size: 12px;
    background-color: rgb(245, 247, 250);
    .procedureName {
      margin-top: 8px;
    }
  }
  #charts {
    width: 100%;
    height: 250px;
  }
</style>
