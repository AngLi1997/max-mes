<!-- 生产计划日历 -->
<template>
  <div class="calendarContents">
    <!-- 筛选条件 -->
    <div class="chooseAll">
      <div class="chooseProcess">
        <template v-if="source !== 'sourcePlan'">
          <span>{{ t('工艺名称') }}</span>
          <TreeSelect
            v-model:value="formState.processId"
            :tree-data="processTreeData"
            style="width: 260px; margin-left: 10px"
            show-search
            allow-clear
            treeNodeFilterProp="showName"
            :placeholder="t('请选择')"
            :field-names="{ label: 'showName', value: 'id' }"
            @change="treeChange"></TreeSelect>
        </template>
      </div>
      <div class="choose">
        <Space :size="20">
          <span v-if="formState.dateType != '年'">
            <span style="margin-right: 10px">{{ t('显示工序') }}</span>
            <Switch v-model:checked="formState.showProcesses" />
          </span>
          <DatePicker
            v-if="formState.dateType === '年'"
            v-model:value="formState.year"
            picker="year"
            :allowClear="false"
            @change="changeYear" />
          <RangePicker
            v-if="formState.dateType == '月'"
            v-model:value="formState.month"
            :allowClear="false"
            picker="month"
            @panelChange="changeMonth"
            @openChange="openChange" />

          <Segmented
            v-model:value="formState.dateType"
            :options="dateType"
            block
            style="width: 100px"
            @change="changeYearAndMonth" />
        </Space>
      </div>
    </div>
    <!-- 日历表格（年） -->
    <div v-if="formState.dateType == '年'" class="content-box">
      <CalendarYear
        :calendarDate="calendarDate"
        :year="formState.year.year() + ''"
        :colorList="colorList"
        @editProcess="editProcess" />
    </div>
    <!-- 日历表格（月） -->
    <div v-if="formState.dateType == '月'" class="content-box">
      <CalendarMonth
        :showProcesses="formState.showProcesses"
        :calendarDate="calendarDate"
        :titleMonths="titleMonths"
        :colorList="colorList"
        @editProcess="editProcess" />
    </div>
  </div>
  <EditCalendarModal
    ref="EditCalendarModalRef"
    :currentProcessItem="currentProcessItem"
    :rowData="rowData"
    :source="source"
    :productionPlanItemId="productionPlanItemId"
    :modalTitle="formState.showProcesses ? t('工序计划日期调整') : t('工艺计划日期调整')"
    :procedureIndex="procedureIndex"
    @reqUpdateCalendar="reqUpdateCalendar"
    @planUpdateCalendar="planUpdateCalendar"></EditCalendarModal>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { DatePicker, RangePicker, Switch, Segmented, message, Space, TreeSelect } from 'ant-design-vue';
  import { onMounted } from 'vue';
  import { reqProductionCalendar, reqProductionCalendarMonths, getProcessListTreeReq } from '@/services';
  import { loopSelectableNotValueTree } from '@bmos/utils';
  import dayjs from 'dayjs';
  import CalendarYear from './components/CalendarYear.vue';
  import EditCalendarModal from './components/EditCalendarModal.vue';
  import CalendarMonth from './components/CalendarMonth.vue';

  const props = defineProps({
    productionPlanId: {
      //生产计划id
      type: String,
      default: () => '',
    },
    productionPlanItemId: {
      //指令单的productionPlanItemId
      type: String,
      default: () => '',
    },
    source: {
      //计划而来
      type: String,
      default: () => '',
    },
    tableDataList: {
      //计划而来的数组
      type: Array,
      default: () => [],
    },
  });

  const calendarDate = ref<any>([]);
  const titleMonths = ref<any>([]); // 月日历表格展示的月份数据
  const EditCalendarModalRef = ref<any>(null);
  const rowData = ref<any>();
  const currentProcessItem = ref<any>(); //点击时单存工艺
  const procedureIndex = ref<any>(); //计划日历修改时若点修改工序时存此工序的下标
  const rangePickerStatus = ref<boolean>(false); // 月份切换器的状态
  const processTreeData = ref<any>(); //工艺树
  const formState = ref<any>({
    showProcesses: false, //是否显示工序
    year: dayjs(),
    month: [dayjs(), dayjs()],
    dateType: '年',
    processId: undefined, //工艺id
  });
  const dateType = ref<any>([
    {
      value: '年',
      label: t('年'),
    },
    {
      value: '月',
      label: t('月'),
    },
  ]); //分段选择器
  const colorList = [
    '#FDE6E6',
    '#E9F5E9',
    '#EBF8FF',
    '#EBF1FF',
    '#FAE6FA',
    '#FFFDEB',
    '#F4FAE8',
    '#EAFCF9',
    '#F4EFFD',
    '#FFF4EB',
    '#F3E5E5',
    '#EFFFD4',
  ];
  const emit = defineEmits(['updateTableDataList']);
  // 切换年月
  const changeYearAndMonth = (val: any) => {
    if (props.source === 'sourcePlan') {
      switch (val) {
        case '年':
          formState.value.showProcesses = false;
          break;
        case '月':
          formState.value.showProcesses = true;
          titleMonths.value = getMonths();
          break;
        default:
          break;
      }
    } else {
      switch (val) {
        case '年':
          formState.value.showProcesses = false;
          break;
        case '月':
          formState.value.showProcesses = true;
          // daysInMonth.value = getDaysInMonth(formState.value.year, formState.value.month);
          break;

        default:
          break;
      }
      getCalendar();
    }
  };
  // 切换年下拉
  const changeYear = () => {
    if (props.source === 'sourcePlan') {
    } else {
      getCalendar();
    }
  };
  const openChange = (val: boolean) => {
    rangePickerStatus.value = val;
  };
  // 切换月下拉
  const changeMonth = () => {
    if (props.source === 'sourcePlan') {
      if (!rangePickerStatus.value) {
        titleMonths.value = getMonths();
      }
    } else {
      !rangePickerStatus.value && getCalendar();
    }
  };
  // 获取两个日期之间的所有月份
  const getMonths = () => {
    const startDate = dayjs(formState.value.month[0]); // 开始日期
    const endDate = dayjs(formState.value.month[1]); // 结束日期
    let currentDate = startDate.clone(); // 克隆开始日期以避免修改原始日期
    const months = []; // 用于存储月份的数组
    while (!currentDate.isAfter(endDate)) {
      // 当当前日期不晚于结束日期时继续循环
      months.push(currentDate.format('YYYY-MM')); // 添加当前月份到数组中，格式为YYYY-MM
      currentDate = currentDate.add(1, 'month'); // 将当前日期增加一个月
    }
    return months;
  };
  // 点击后弹窗修改工艺/工序的日历
  const editProcess = (processItem: any, procedureItem?: any, procedureIndex1?: any) => {
    currentProcessItem.value = processItem;
    rowData.value = procedureItem ? procedureItem : processItem;
    procedureIndex.value = procedureIndex1;
    EditCalendarModalRef.value.openModal();
  };

  // 获取日历数据(接口而来)
  const getCalendar = async () => {
    if (formState.value.dateType == '年') {
      const data = {
        year: dayjs(formState.value.year).year(),
        processId: formState.value.processId || undefined,
        productionPlanId: props.productionPlanId || undefined,
        productionPlanItemId: props.productionPlanItemId || undefined,
      };
      const res = await reqProductionCalendar(data);
      calendarDate.value = res.data;
    } else {
      const months = getMonths();
      const data = {
        startMonth: months[0],
        endMonth: months[months.length - 1],
        processId: formState.value.processId || undefined,
        productionPlanId: props.productionPlanId || undefined,
        productionPlanItemId: props.productionPlanItemId || undefined,
      };
      const res = await reqProductionCalendarMonths(data);
      titleMonths.value = months;
      calendarDate.value = res.data;
    }
  };

  // 计划而来的数据
  const getPlanCalendar = () => {
    const temp: any = props.tableDataList?.reduce((acc: any, current: any) => {
      return acc?.concat(current);
    }, []);
    temp?.forEach((item: any, index: any) => {
      item.processIndex = index; //工艺加索引(多批计划时可区分)
      item.procedureDateList = item.procedureListDetail; //注意这里俩后端返的字段名不一样 光
    });
    calendarDate.value = updateSort(temp);
  };
  // 日历前端排序(先开始的在前)
  const updateSort = (arr: any) => {
    return arr.sort((a: any, b: any) => {
      return new Date(a.startTime) - new Date(b.startTime);
    });
  };

  // 求该工艺的所属工序日期数组中最晚的日期
  const getLatestDate = (data: any) => {
    const dateArray = data?.map((item: any) => dayjs(item.endTime));
    let latestDate = dateArray[0];
    dateArray.forEach((item: any) => {
      if (item.isAfter(latestDate)) {
        latestDate = item;
      }
    });
    return latestDate.format('YYYY-MM-DD');
  };

  // 弹框确定之后更新日历(此处为接口更新)
  const reqUpdateCalendar = () => {
    getCalendar();
  };
  // 弹框确定之后更新日历(此处为前端更新)
  const planUpdateCalendar = (res: any, currentProcessItem: any, procedureIndex: any) => {
    calendarDate.value.forEach((item: any) => {
      if (!formState.value.showProcesses) {
        //改工艺
        if (item.key === currentProcessItem.key) {
          if (res.whetherAdjust) {
            //该工艺后续计划调整
            const latestDate = getLatestDate(item.procedureDateList);
            if (dayjs(res.endTime).isBefore(dayjs(latestDate))) {
              message.error(t('工艺的结束日期不能早于下属工序最晚结束日期'));
              return;
            }
            const echoStartTime = rowData.value.startTime; //存弹框打开时未改的开始时间
            item.startTime = res.startTime;
            item.endTime = res.endTime;
            item.procedureDateList.forEach((i: any) => {
              //相应调整该工艺的所属工序
              i.startTime = dayjs(item.startTime).add(i.procedureItemInterval, 'day').format('YYYY-MM-DD');
              i.endTime = dayjs(i.startTime)
                .add(i.procedureItemDuration - 1, 'day')
                .format('YYYY-MM-DD');
            });
            const temp = dayjs(res.startTime).diff(dayjs(echoStartTime), 'day'); //计算延迟或者提前了多久
            calendarDate.value.forEach((item2: any) => {
              if (item2.processIndex > item.processIndex) {
                //改后续的工艺及后续工艺的所属工序
                item2.startTime = dayjs(item2.startTime).add(temp, 'day').format('YYYY-MM-DD');
                item2.endTime = dayjs(item2.startTime)
                  .add(item2.processIdItemDuration - 1, 'day')
                  .format('YYYY-MM-DD'); //结束日期为开始日期加执行时长
                item2.procedureDateList.forEach((item3: any) => {
                  item3.startTime = dayjs(item2.startTime).add(item3.procedureItemInterval, 'day').format('YYYY-MM-DD');
                  item3.endTime = dayjs(item3.startTime)
                    .add(item3.procedureItemDuration - 1, 'day')
                    .format('YYYY-MM-DD');
                });
              }
            });
            message.success(t('操作成功'));
            EditCalendarModalRef.value.closeModal();
          } else {
            //该工艺后续计划不调整(但是该工艺下的工序会相应调整)
            const latestDate = getLatestDate(item.procedureDateList);
            if (dayjs(res.endTime).isBefore(dayjs(latestDate))) {
              message.error(t('工艺的结束日期不能早于下属工序最晚结束日期'));
              return;
            }
            item.startTime = res.startTime;
            item.endTime = res.endTime;
            item.procedureDateList.forEach((item2: any) => {
              item2.startTime = dayjs(item.startTime).add(item2.procedureItemInterval, 'day').format('YYYY-MM-DD');
              item2.endTime = dayjs(item2.startTime)
                .add(item2.procedureItemDuration - 1, 'day')
                .format('YYYY-MM-DD');
            });
            message.success(t('操作成功'));
            EditCalendarModalRef.value.closeModal();
          }
        }
      } else {
        // 改工序
        if (item.key === currentProcessItem.key) {
          if (!res.whetherAdjust) {
            //该工艺的该条工序的后续计划不调整
            if (dayjs(res.startTime).isBefore(dayjs(currentProcessItem.startTime))) {
              message.error(t('工序的开始日期不能早于所属工艺开始日期'));
              return;
            }
            if (dayjs(currentProcessItem.endTime).isBefore(dayjs(res.endTime))) {
              currentProcessItem.endTime = res.endTime; //工序计划调整结束时间晚于工艺结束日期，工艺结束时间会跟着调整
              item.endTime = res.endTime;
            }
            item.procedureDateList[procedureIndex].startTime = res.startTime;
            item.procedureDateList[procedureIndex].endTime = res.endTime;
            message.success(t('操作成功'));
            EditCalendarModalRef.value.closeModal();
          } else {
            //该工艺的该条工序的后续计划调整
            if (dayjs(res.startTime).isBefore(dayjs(currentProcessItem.startTime))) {
              message.error(t('工序的开始日期不能早于所属工艺开始日期'));
              return;
            }
            const echoStartTime = rowData.value.startTime; //存弹框打开时未改的开始时间
            item.procedureDateList[procedureIndex].startTime = res.startTime;
            item.procedureDateList[procedureIndex].endTime = res.endTime;
            const temp = dayjs(res.startTime).diff(dayjs(echoStartTime), 'day'); //计算延迟或者提前了多久
            item.procedureDateList?.forEach((item2: any, index: any) => {
              if (index > procedureIndex) {
                item2.startTime = dayjs(item2.startTime).add(temp, 'day').format('YYYY-MM-DD');
                item2.endTime = dayjs(item2.startTime)
                  .add(item2.procedureItemDuration - 1, 'day')
                  .format('YYYY-MM-DD'); //结束日期为开始日期加执行时长
              }
            });
            if (dayjs(currentProcessItem.endTime).isBefore(dayjs(getLatestDate(item.procedureDateList)))) {
              currentProcessItem.endTime = getLatestDate(item.procedureDateList); //工序计划调整结束时间晚于工艺结束日期，工艺结束时间会跟着调整
            }
            message.success(t('操作成功'));
            EditCalendarModalRef.value.closeModal();
          }
        }
      }
    });
    calendarDate.value = updateSort(calendarDate.value);
    emit('updateTableDataList', calendarDate.value);
  };
  //获取工艺树
  const getProcessTreeData = async () => {
    try {
      const { data } = await getProcessListTreeReq();
      processTreeData.value = loopSelectableNotValueTree(data, 'isFlag', true);
    } catch (error) {}
  };
  // 工艺树改变
  const treeChange = () => {
    getCalendar();
  };
  onMounted(async () => {
    getProcessTreeData();
    if (props.source === 'sourcePlan') {
      //计划而来的日历
      getPlanCalendar();
    } else {
      getCalendar();
    }
  });
</script>
<style lang="less" scoped>
  .calendarContents {
    height: 100%;
    background: white;
    overflow-y: auto;
    .chooseAll {
      padding: 16px;
      box-sizing: border-box;
      display: flex;
      justify-content: space-between;
      .choose {
        display: flex;
        align-items: center;
        justify-content: end;
      }
    }
    .content-box {
      width: 100%;
      height: calc(100% - 68px);
      overflow: hidden;
      box-sizing: border-box;
      padding: 0 16px 12px;
    }
  }
</style>
