<template>
  <div id="scroll-calendar-month" class="calendar-month">
    <div class="calendar-month-header">
      <div class="title shadow border-r-b">{{ t('生产计划') }}</div>
      <div class="date">
        <div v-for="monthItem in titleData" :key="monthItem.month" class="month-item">
          <div class="date-first border-r-b">{{ monthItem.month }}</div>
          <div class="date-second">
            <div v-for="item in monthItem.day" :key="item" class="date-second-item border-r-b">
              <span :class="{ 'current-day': isCurrentDay(monthItem.month, item) }">
                {{ item }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-if="list.length" class="calendar-month-body">
      <div v-for="(processItem, index) in list" :key="index" class="plan-row-item">
        <div class="plan-row-item-title border-r-b shadow" :style="{ backgroundColor: processItem.bgColor }">
          <div class="batch">{{ processItem.batchNo }}</div>
          <div class="process-name" style="margin: 4px 0">
            {{ processItem.processName }}
          </div>
          <div class="line-name">{{ processItem?.productionLineName }}</div>
        </div>
        <div
          class="plan-row-item-content"
          :style="{
            backgroundColor: processItem.bgColor,
            'min-height':
              processItem.procedureDateList.length > 2 ? processItem.procedureDateList.length * 30 + 8 + 'px' : '87px',
          }">
          <template v-if="showProcesses">
            <div
              v-for="(item, index2) in processItem.procedureDateList"
              v-show="item.diffDay > 0"
              :key="index2"
              :style="{
                width: 60 * item.diffDay + 'px',
                left: 60 * (item.startDay - 1) + 'px',
                top: processItem.procedureDateList.length === 1 ? '32px' : 22 * index2 + 8 * (index2 + 1) + 'px',
              }"
              class="line-box"
              @click="editProcess(processItem, item, index2)">
              <Dropdown placement="bottom" arrow>
                <div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis">
                  {{ item.procedureName }}
                </div>
                <template #overlay>
                  <div class="dropdown-content">
                    <div class="dropdown-content-title">{{ processItem.batchNo }}</div>
                    <div class="dropdown-content-title">
                      {{ item.procedureName }}
                    </div>
                    <div class="dropdown-content-time">{{ t('开始时间') }}:{{ item.startTime }}</div>
                    <div class="dropdown-content-time">{{ t('结束时间') }}:{{ item.endTime }}</div>
                  </div>
                </template>
              </Dropdown>
            </div>
          </template>
          <div
            v-else
            v-show="processItem.diffDay > 0"
            class="line-box"
            :style="{
              width: 60 * processItem.diffDay + 'px',
              left: 60 * (processItem.startDay - 1) + 'px',
            }">
            <Dropdown placement="bottom" arrow>
              <div @click="editProcess(processItem)">
                <span>{{ processItem.processName }}</span>
              </div>
              <template #overlay>
                <div class="dropdown-content">
                  <div class="dropdown-content-title">{{ processItem.batchNo }}</div>
                  <div class="dropdown-content-title">
                    {{ processItem.processName }}
                  </div>
                  <div class="dropdown-content-time">{{ t('开始时间') }}:{{ processItem.startTime }}</div>
                  <div class="dropdown-content-time">{{ t('结束时间') }}:{{ processItem.endTime }}</div>
                </div>
              </template>
            </Dropdown>
          </div>
          <div v-for="(_item, index) in countDay" :key="index" class="border-item border-r-b"></div>
        </div>
      </div>
    </div>
    <Empty v-else class="empty-box" />
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import dayjs from 'dayjs';
  import { Dropdown, Empty } from 'ant-design-vue';

  const props = withDefaults(
    defineProps<{
      showProcesses: boolean;
      titleMonths: Array<string>;
      calendarDate: Array<any>;
      colorList: Array<string>;
    }>(),
    {
      showProcesses: () => false,
      titleMonths: () => [],
      cadetblue: () => [],
      colorList: () => [],
    },
  );
  const emit = defineEmits(['editProcess']);
  const list = ref<any[]>([]);
  const titleData = ref<any[]>([]);
  const countDay = computed(() => {
    return titleData.value.reduce((prev, cur) => {
      return prev + cur.day;
    }, 0);
  });
  // 判断是否是当前年月当日
  const isCurrentDay = (month: string, day: number) => {
    return dayjs().isSame(`${month}-${day}`, 'day');
  };
  // 获取开始日期
  const getStartDay = (time: string) => {
    const startDay = `${props.titleMonths[0]}-1`;
    if (dayjs(time).isBefore(dayjs(startDay), 'day')) {
      return 1;
    } else {
      const date1 = dayjs(startDay);
      const date2 = dayjs(time);
      return date2.diff(date1, 'day') + 1;
    }
  };
  // 获取结束日期
  const getEndDay = (time: string) => {
    const startDate = `${props.titleMonths[0]}-1`;
    const endDate = `${props.titleMonths[props.titleMonths.length - 1]}-${dayjs(
      props.titleMonths[props.titleMonths.length - 1],
    ).daysInMonth()}`;
    // 如果结束日期大于当月最后一天，则计算当月最后一天到`${props.titleMonths[0]}-1`的天数
    // 否则计算开始日期到结束日期的天数
    if (dayjs(time).isAfter(dayjs(endDate), 'day')) {
      const date1 = dayjs(startDate);
      const date2 = dayjs(endDate);
      return date2.diff(date1, 'day') + 1;
    } else {
      const date1 = dayjs(startDate);
      const date2 = dayjs(time);
      return date2.diff(date1, 'day') + 1;
    }
  };
  // 编辑工序
  const editProcess = (...args: any[]) => {
    emit('editProcess', ...args);
  };
  watch(
    () => [props.calendarDate, props.titleMonths],
    () => {
      console.log('watch:calendar-month');
      // 月份表头数据
      titleData.value = props.titleMonths.map((item: string) => {
        return {
          month: item,
          // 当月天数
          day: dayjs(item).daysInMonth(),
        };
      });
      const colorObj: {
        [key: string]: string;
      } = {};
      list.value = props.calendarDate.map((item: any) => {
        // 开始日期
        const startDay = getStartDay(item.startTime);
        // 结束日期
        const endDay = getEndDay(item.endTime);
        // 相差天数
        let diffDay = endDay - startDay + 1;
        // 如果开始结束时间都不当前年，那么diffMonth为0
        if (
          (dayjs(item.startTime).isBefore(dayjs(props.titleMonths[0]), 'month') &&
            dayjs(item.endTime).isBefore(dayjs(props.titleMonths[0]), 'month')) ||
          (dayjs(item.startTime).isAfter(dayjs(props.titleMonths[props.titleMonths.length - 1]), 'month') &&
            dayjs(item.endTime).isAfter(dayjs(props.titleMonths[props.titleMonths.length - 1]), 'month'))
        ) {
          diffDay = 0;
        }
        // 背景色（颜色从colorList中取，productionLineId一样的的背景色一样，不够时重复取）
        if (!colorObj[item.productionLineId]) {
          let index = 0;
          if (Object.keys(colorObj).length <= props.colorList.length) {
            index = Object.keys(colorObj).length;
          } else {
            index = Object.keys(colorObj).length - props.colorList.length;
          }
          colorObj[item.productionLineId] = props.colorList[index];
        }
        // 工序处理
        item.procedureDateList.forEach((item1: any) => {
          // 开始日期
          const startDay1 = getStartDay(item1.startTime);
          // 结束日期
          const endDay1 = getEndDay(item1.endTime);
          // 相差天数
          let diffDay1 = endDay1 - startDay1 + 1;
          if (
            (dayjs(item1.startTime).isBefore(dayjs(`${props.titleMonths[0]}-1`), 'day') &&
              dayjs(item1.endTime).isBefore(dayjs(`${props.titleMonths[0]}-1`), 'day')) ||
            (dayjs(item1.startTime).isAfter(
              dayjs(
                `${props.titleMonths[props.titleMonths.length - 1]}-${dayjs(
                  props.titleMonths[props.titleMonths.length - 1],
                ).daysInMonth()}`,
              ),
              'day',
            ) &&
              dayjs(item1.endTime).isAfter(
                dayjs(
                  `${props.titleMonths[props.titleMonths.length - 1]}-${dayjs(
                    props.titleMonths[props.titleMonths.length - 1],
                  ).daysInMonth()}`,
                ),
                'day',
              ))
          ) {
            diffDay1 = 0;
          }
          item1.startDay = startDay1;
          item1.endDay = endDay1;
          item1.diffDay = diffDay1;
        });

        const bgColor = colorObj[item.productionLineId];
        item.startDay = startDay;
        item.endDay = endDay;
        item.diffDay = diffDay;
        item.bgColor = bgColor;
        return item;
      });
    },
    {
      immediate: true,
      deep: true,
    },
  );
</script>

<style lang="less" scoped>
  // Dropdown的下拉样式
  .dropdown-content {
    width: 275px;
    padding: 16px 16px 7px 16px;
    box-shadow: 0px 0px 6px 0px #00000033;
    border-radius: 4px;
    box-sizing: border-box;
    background-color: white;
    position: relative;
    left: calc(50% - 137.5px);
    .dropdown-content-title {
      margin-bottom: 9px;
      color: #242526;
    }
    .dropdown-content-time {
      margin-bottom: 9px;
      color: #606266;
    }
  }
  :deep(.mes-dropdown-trigger) {
    z-index: 999 !important;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .calendar-month {
    width: 100%;
    height: 100%;
    overflow: auto;
    font-size: 14px;
    border-top: 1px solid #e1e3e5;
    border-left: 1px solid #e1e3e5;
    // 隐藏滚动条
    // scrollbar-width: none;

    // 阴影
    .shadow {
      box-shadow: 2px 0px 4px rgba(0, 0, 0, 0.15);
    }
    // 右下border
    .border-r-b {
      border-right: 1px solid #e1e3e5;
      border-bottom: 1px solid #e1e3e5;
      box-sizing: border-box;
    }
    .calendar-month-header {
      position: sticky;
      top: 0;
      z-index: 99;
      width: max-content;
      height: 50px;
      display: flex;
      .title {
        width: 200px;
        height: 50px;
        line-height: 50px;
        padding-left: 12px;
        box-sizing: border-box;
        background: #f9fafb;
        position: sticky;
        left: 0;
        z-index: 1;
      }
      .date {
        width: max-content;
        background-color: #606266;
        display: flex;
        .month-item {
          .date-first {
            width: 100%;
            box-sizing: border-box;
            height: 22px;
            line-height: 22px;
            padding-left: 12px;
            background: rgb(249, 250, 251);
          }
          .date-second {
            display: flex;
            height: 28px;
            background: #fff;
            .date-second-item {
              width: 60px;
              height: 28px;
              line-height: 28px;
              text-align: center;
              .current-day {
                color: #fff;
                background-color: #2871ff;
                border-radius: 10px;
                padding: 1px 12px;
              }
            }
          }
        }
      }
    }
    .calendar-month-body {
      .plan-row-item {
        width: max-content;
        display: flex;
        content-visibility: auto;
        contain-intrinsic-size: 86px;
        .plan-row-item-title {
          width: 200px;
          padding: 8px;
          box-sizing: border-box;
          position: sticky;
          left: 0;
          z-index: 1;
          flex-shrink: 0;
          display: flex;
          flex-direction: column;
          justify-content: center;
          .batch {
            width: 100%;
            color: #242526;
            font-weight: 500;
            line-height: 18px;
          }
          .process-name,
          .line-name {
            width: 100%;
            color: #606266;
            line-height: 18px;
          }
        }
        .plan-row-item-content {
          position: relative;
          display: flex;
          flex: 1;
          align-items: center;
          .line-box {
            position: absolute;
            padding: 2px 8px;
            box-sizing: border-box;
            background: #2871ff;
            color: white;
            border-radius: 4px;
            cursor: pointer;
          }
          .border-item {
            width: 60px;
            height: 100%;
          }
        }
      }
    }
    .empty-box {
      position: absolute;
      top: calc(50% - 65px);
      left: calc(50% - 25px);
    }
  }
</style>
