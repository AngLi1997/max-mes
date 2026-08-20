<template>
  <div class="calendar-year">
    <div class="calendar-year-header">
      <div class="title shadow border-r-b">{{ t('生产计划') }}</div>
      <div ref="dateRef" class="date">
        <div class="date-first border-r-b">{{ year }}{{ t('年') }}</div>
        <div class="date-second">
          <div v-for="(item, index) in 12" :key="index" class="date-second-item border-r-b">
            <span :class="{ 'current-month': isCurrentMonth(item) }">
              {{ item + t('月') }}
            </span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="list.length" class="calendar-year-body">
      <div v-for="(processItem, index) in list" :key="index" class="plan-row-item">
        <div class="plan-row-item-title border-r-b shadow" :style="{ backgroundColor: processItem.bgColor }">
          <div class="batch">{{ processItem.batchNo }}</div>
          <div class="process-name" style="margin: 4px 0">
            {{ processItem.processName }}
          </div>
          <div class="line-name">{{ processItem?.productionLineName }}</div>
        </div>
        <div class="plan-row-item-content" :style="{ width: dateWidth, backgroundColor: processItem.bgColor }">
          <div
            v-show="processItem.diffMonth > 0"
            class="line-box"
            :style="{
              width: (100 / 12) * processItem.diffMonth + '%',
              left: (100 / 12) * (processItem.startMonth - 1) + '%',
            }">
            <Dropdown placement="bottom" arrow>
              <div @click="editProcess(processItem)">
                <span style="display: inline-block; width: 100%; height: 100%">
                  {{ processItem.processName }}
                </span>
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
          <div v-for="item in 12" :key="item" class="border-item border-r-b"></div>
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
  import { useListenResize } from '../hooks';

  const props = withDefaults(
    defineProps<{
      year: string;
      calendarDate: Array<any>;
      colorList: Array<string>;
    }>(),
    {
      year: dayjs().year() + '',
      cadetblue: () => [],
      colorList: () => [],
    },
  );
  const emit = defineEmits(['editProcess']);
  const dateRef = ref();
  const dateWidth = ref<string>('0px');
  const list = ref<any[]>([]);
  // 判断是否是当前年月
  const isCurrentMonth = (month: number) => {
    return dayjs().isSame(`${props.year}-${month}`, 'month');
  };
  // 获取开始月份
  const getStartMonth = (time: string) => {
    return dayjs(time).isBefore(dayjs(props.year), 'year') ? 1 : dayjs(time).month() + 1;
  };
  // 获取结束月份
  const getEndMonth = (time: string) => {
    return dayjs(time).isAfter(dayjs(props.year), 'year') ? 12 : dayjs(time).month() + 1;
  };

  // 编辑工序
  const editProcess = (item: any) => {
    emit('editProcess', item);
  };
  watch(
    () => [props.calendarDate, props.year],
    () => {
      console.log('watch:calendar-year');
      const colorObj: {
        [key: string]: string;
      } = {};
      list.value = props.calendarDate.map((item: any) => {
        // 开始月份
        item.startMonth = getStartMonth(item.startTime);
        // 结束月份
        item.endMonth = getEndMonth(item.endTime);
        // 如果开始结束时间都不当前年，那么diffMonth为0
        if (
          (dayjs(item.startTime).isAfter(dayjs(props.year), 'year') &&
            dayjs(item.endTime).isAfter(dayjs(props.year), 'year')) ||
          (dayjs(item.startTime).isBefore(dayjs(props.year), 'year') &&
            dayjs(item.endTime).isBefore(dayjs(props.year), 'year'))
        ) {
          item.diffMonth = 0;
        } else {
          item.diffMonth = item.endMonth - item.startMonth + 1;
        }
        // 开始结束相差月份
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
        item.bgColor = colorObj[item.productionLineId];
        return item;
      });
    },
    {
      immediate: true,
      deep: true,
    },
  );
  // 监听屏幕大小变化
  useListenResize(() => {
    nextTick(() => {
      dateWidth.value = (dateRef.value?.offsetWidth || 0) + 'px';
    });
  });
  onMounted(() => {
    dateWidth.value = (dateRef.value?.offsetWidth || 0) + 'px';
  });
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
  .calendar-year {
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
    .calendar-year-header {
      position: sticky;
      top: 0;
      z-index: 99;
      display: flex;
      height: 50px;
      width: 100%;
      .title {
        width: 200px;
        padding-left: 12px;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        background: #f9fafb;
        flex-shrink: 0;
        position: sticky;
        left: 0;
        z-index: 1;
      }
      .date {
        flex: 1;
        .date-first {
          width: 100%;
          box-sizing: border-box;
          height: 22px;
          line-height: 22px;
          padding-left: 12px;
          background: rgb(249, 250, 251);
        }
        .date-second {
          width: 100%;
          display: flex;
          height: 28px;
          background: #fff;
          .date-second-item {
            width: calc(100% / 12);
            min-width: 122px;
            height: 28px;
            line-height: 28px;
            text-align: center;
            .current-month {
              color: #fff;
              background-color: #2871ff;
              border-radius: 10px;
              padding: 1px 12px;
            }
          }
        }
      }
    }
    .calendar-year-body {
      .plan-row-item {
        display: flex;
        content-visibility: auto;
        contain-intrinsic-size: 80px;
        .plan-row-item-title {
          width: 200px;
          padding: 8px;
          box-sizing: border-box;
          position: sticky;
          left: 0;
          z-index: 1;
          flex-shrink: 0;
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
            height: 22px;
          }
          .border-item {
            min-width: 122px;
            width: calc(100% / 12);
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
