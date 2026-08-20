<template>
  <div class="situations_box">
    <div class="investment_box">
      <img :src="imagePaths.investmentTitle" width="440px" height="32px" />
      <div class="investment_content">
        <div class="total_box">
          <img :src="investment_icon" width="60px" height="60px" />
          <div class="total">
            {{ t('年度批次投浆总量') }}
            <div class="total_num">
              <span class="num_box">{{ totalNum }}</span>
              <span class="num_unit">{{ t('吨') }}</span>
            </div>
          </div>
        </div>
        <div class="mouth_box">
          <div class="title">
            <img :src="mouth_icon" width="12px" height="12px" />
            {{ t('月批次投浆量') }}
          </div>
          <div class="select_year">
            <Select
              v-model:value="mouthYear"
              style="width: 100%"
              :options="mouthYear_options"
              size="small"
              @change="getInvestmentData"></Select>
          </div>
        </div>
        <div class="echarts_box">
          <Echarts v-if="showChart" :options="eChartOptions" :height="200" />
        </div>
      </div>
    </div>
    <div class="production">
      <img :src="imagePaths.productionTitle" width="440px" height="32px" />
      <div class="select_year">
        <Select
          v-model:value="productionYear"
          style="width: 100px"
          :options="mouthYear_options"
          size="small"
          @change="getProductionData"></Select>
      </div>
      <div v-for="item in productionDataList" :key="item.type" class="product_box">
        <div class="title">
          {{ productionDataTitle[item.type as keyof typeof productionDataTitle] }}
          <span class="title_specification">{{ item.data.specification }}</span>
        </div>
        <div class="production_box">
          <div class="production_item">
            <div class="production_title">
              <span>{{ t('年度产量') }}</span>
              <span>{{ t('年度批次') }}</span>
            </div>
            <div class="production_content">
              <div>
                <span class="annualProduction">{{ formatNumber(item.data.annualProduction) }}</span>
                <span class="unit">{{ t('瓶') }}</span>
              </div>
              <div>
                <span class="annualBatchProduction">{{ item.data.annualBatchProduction }}</span>
                <span class="unit">{{ t('批') }}</span>
              </div>
            </div>
          </div>
          <div class="production_item">
            <div class="production_title">
              <span>{{ t('本月产量') }}</span>
              <span>{{ t('本月批次') }}</span>
            </div>
            <div class="production_content">
              <div>
                <span class="annualProduction">{{ formatNumber(item.data.monthlyProduction) }}</span>
                <span class="unit">{{ t('瓶') }}</span>
              </div>
              <div>
                <span class="annualBatchProduction">{{ item.data.monthlyBatchProduction }}</span>
                <span class="unit">{{ t('批') }}</span>
              </div>
            </div>
          </div>
          <img :src="group" alt="" width="70px" height="70px" />
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { t, currentLng } from '@bmos/i18n';
  import investment_icon from '@/assets/baiePng/situation/investment_icon.png';
  import mouth_icon from '@/assets/baiePng/situation/mouth_icon.png';
  import group from '@/assets/baiePng/situation/group.png';
  import { Select } from 'ant-design-vue';
  import echarts from '@/plugins/echarts';
  import { getDashboardDataPlasma, getDashboardDataProduct } from '@/services';
  import { useTime } from '../../hooks/useTime';
  const { year } = useTime();
  import dayjs from 'dayjs';

  // 添加格式化数字的函数
  const formatNumber = (num: string | number) => {
    if (!num) return '0';
    const parts = num.toString().split('.');
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return parts.join('.');
  };

  const images = import.meta.glob('@/assets/baiePng/*/*/*.png', { eager: true });
  const imagePaths = computed(() => {
    const lang = currentLng.value;
    console.log(currentLng.value);
    return {
      investmentTitle: images[`/src/assets/baiePng/${lang}/situation/investmentTitle.png`]?.default,
      productionTitle: images[`/src/assets/baiePng/${lang}/situation/productionTitle.png`]?.default,
    };
  });

  const timer = ref(null);
  const totalNum = ref('');
  const mouthYear = ref('2025');
  const mouthYear_options = ref<any>([]);
  const productionYear = ref('2025');
  const showChart = ref(false);
  const eChartOptions = ref();
  const productionDataList = ref<any>([
    {
      type: 'humanSerumAlbumin',
      data: {
        specification: '1',
        annualProduction: '3',
        annualBatchProduction: '1',
        monthlyProduction: '3',
        monthlyBatchProduction: '1',
      },
    },
    {
      type: 'ImmunoglobulinInjection',
      data: {
        specification: '1',
        annualProduction: '3',
        annualBatchProduction: '1',
        monthlyProduction: '3',
        monthlyBatchProduction: '1',
      },
    },
  ]);
  const productionDataTitle = computed(() => {
    return {
      humanSerumAlbumin: t('人血白蛋白'),
      ImmunoglobulinInjection: t('静注人免疫球蛋白'),
    };
  });

  const getInvestmentData = async () => {
    const { data } = await getDashboardDataPlasma({ type: 1, queryTime: mouthYear.value });
    data.xAxis.data = data.xAxis.data.map((item: string) => t(item));
    showChart.value = false;
    totalNum.value = data.plasmaVolume;
    eChartOptions.value = {
      tooltip: {},
      grid: {
        left: 40,
        right: 20,
        top: 20,
        bottom: 30,
      },
      series: [],
      xAxis: {
        ...data.xAxis,
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#98BED9',
            fontSize: '12px',
          },
        },
        axisLabel: {
          color: '#98BED9',
          fontSize: currentLng.value == 'zh_CN' ? 12 : 10,
        },
      },
      yAxis: {
        ...(data.yAxis ? data.yAxis : {}),
        splitLine: {
          show: true,
          lineStyle: {
            type: 'dashed',
            color: '#8FA4B2',
            opacity: '0.3',
          },
        },
        axisLine: {
          // 坐标轴线
          lineStyle: {
            color: '#8FA4B2',
            fontSize: '12px',
          },
        },
        axisLabel: {
          color: '#8FA4B2',
          fontSize: currentLng.value == 'zh_CN' ? 12 : 10,
        },
      },
    };
    eChartOptions.value.series = data.series.map((item: any) => {
      item.itemStyle = {
        color: new echarts.graphic.LinearGradient(
          0,
          0,
          0,
          1, // 渐变方向从上到下
          [
            { offset: 0, color: `rgba(128, 202, 255, 1)` }, // 柱图顶部颜色
            { offset: 1, color: `rgba(128, 202, 255, 0.1)` }, // 柱图底部颜色
          ],
        ),
      };
      item.data = item.data.map((dt: any, index: number) => {
        // 判断数据是否是当前当月
        const month = index + 1;
        const dataMonth = dayjs(`${mouthYear.value}-${month}`);
        const currentMonth = dayjs();
        const isCurrent = dataMonth.isSame(currentMonth, 'month');
        if (isCurrent) {
          return {
            value: dt,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1, // 渐变方向从上到下
                [
                  { offset: 0, color: `rgba(102, 178, 255, 1)` }, // 柱图顶部颜色
                  { offset: 1, color: `rgba(64, 255, 255, 1)` }, // 柱图底部颜色
                ],
              ),
              borderColor: new echarts.graphic.LinearGradient(
                0,
                0,
                0,
                1, // 渐变方向从上到下
                [
                  { offset: 0, color: `rgba(255, 255, 255, 0.8)` }, // 柱图顶部颜色
                  { offset: 1, color: `rgba(255, 255, 255, 0)` }, // 柱图底部颜色
                ],
              ), // 边框颜色
              borderWidth: 1, // 边框宽度
              borderType: 'solid', // 边框类型（solid | dashed | dotted）
            },
          };
        } else {
          return dt;
        }
      });
      return item;
    });
    nextTick(() => {
      showChart.value = true;
    });
  };
  const getProductionData = async () => {
    const { data: humanSerumAlbumin } = await getDashboardDataProduct({ type: 2, queryTime: productionYear.value });
    const { data: ImmunoglobulinInjection } = await getDashboardDataProduct({
      type: 3,
      queryTime: productionYear.value,
    });
    productionDataList.value[0].data = humanSerumAlbumin;
    productionDataList.value[1].data = ImmunoglobulinInjection;
  };
  onMounted(() => {
    getInvestmentData();
    getProductionData();
    let nowYear = (year.value as any) * 1;
    while (nowYear >= 2025) {
      mouthYear_options.value.push({
        label: nowYear,
        value: `${nowYear}`,
      });
      nowYear--;
    }
    timer.value = setInterval(
      () => {
        getInvestmentData();
        getProductionData();
      },
      5 * 60 * 1000,
    );
  });
  onUnmounted(() => {
    clearInterval(timer.value);
    timer.value = null;
  });
</script>
<style lang="less" scoped>
  .situations_box {
    img {
      object-fit: cover;
    }
    position: fixed;
    top: 0;
    left: 0;
    padding: 115px 50px 0 20px;
    width: 510px;
    height: 100%;
    box-sizing: border-box;
    background-image: url('@/assets/baiePng/situation/bg.png');
    background-size: 100% 100%;
    .investment_box {
      margin-bottom: 50px;
      .investment_content {
        .total_box {
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 10px;
          height: 60px;
          margin-top: 15px;
          .total {
            width: 100%;
            height: 100%;
            background-image: url('@/assets/baiePng/situation/total_bg.png');
            background-size: 100% 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px;
            color: #98bed9;
            font-size: 14px;
            line-height: 18px;
            .num_box {
              color: #40ffff;
              font-size: 22px;
              font-weight: 500;
            }
            .num_unit {
              margin-left: 8px;
            }
          }
        }
        .mouth_box {
          height: 35px;
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-top: 20px;
          .title {
            color: #c3d7e5;
            margin-left: 4px;
          }
          .select_year {
            width: 100px;
          }
        }
        .echarts_box {
          width: 100%;
          height: 200px;
        }
      }
    }
    .production {
      .select_year {
        margin-top: 20px;
        display: flex;
        justify-content: flex-end;
      }
      .product_box {
        margin-top: 10px;
        margin-bottom: 30px;
        .title {
          height: 24px;
          line-height: 24px;
          color: #fff;
          font-size: 16px;
          padding-left: 20px;
          background-image: url('@/assets/baiePng/situation/humanSerumAlbuminTitle.png');
          background-size: 100% 100%;
          .title_specification {
            color: #8fa4b2;
            font-size: 12px;
            margin-left: 15px;
          }
        }
        .production_box {
          position: relative;
          .production_item {
            height: 60px;
            margin-top: 20px;
            background-image: url('@/assets/baiePng/situation/production_item.png');
            background-size: 100% 100%;
            padding: 6px 16px;
            display: flex;
            justify-content: space-between;
            flex-direction: column;
            .production_title {
              display: flex;
              align-items: center;
              justify-content: space-between;
              color: #98bed9;
              font-size: 12px;
            }
            .production_content {
              display: flex;
              align-items: center;
              justify-content: space-between;
              .unit {
                color: #c3d7e5;
                font-size: 12px;
                margin-left: 8px;
              }
              .annualProduction {
                color: #80caff;
                font-size: 20px;
                font-weight: 500;
              }
              .annualBatchProduction {
                color: #40ffff;
                font-size: 20px;
                font-weight: 500;
              }
            }
          }
          img {
            position: absolute;
            top: 0;
            left: 0;
            bottom: 0;
            right: 0;
            margin: auto;
          }
        }
      }
    }
  }
</style>
