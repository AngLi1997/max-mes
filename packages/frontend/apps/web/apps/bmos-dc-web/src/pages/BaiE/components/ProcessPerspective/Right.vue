<template>
  <img :src="titleImg" />
  <div class="container bmos-table-content">
    <div class="title">
      {{ t('完成批次') }}
    </div>
    <div style="height: 176px; display: flex; margin-bottom: 25px">
      <BMTable
        ref="tableRef"
        :columns="columns"
        :dataSource="tableData"
        :extraParams="extraParams"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 200, y: 100 }" />
    </div>
    <div class="title">
      {{ t('在制批次') }}
      <div class="right" @click="toProcessProgress">
        {{ t('查看更多') }}
        <img :src="imageCommon[`/src/assets/baiePng/processMore.png`]?.default" />
      </div>
    </div>
    <div class="process-title">
      <div class="item">
        <BMIcons
          icon="DCCircle"
          :style="{
            fontSize: '8px',
            width: '8px',
            height: '8px',
            color: colors[0],
          }" />
        {{ t('未激活') }}
      </div>
      <div class="item">
        <BMIcons
          icon="DCCircle"
          :style="{
            fontSize: '8px',
            width: '8px',
            height: '8px',
            color: colors[1],
          }" />
        {{ t('进行中') }}
      </div>
      <div class="item">
        <BMIcons
          icon="DCCircle"
          :style="{
            fontSize: '8px',
            width: '8px',
            height: '8px',
            color: colors[4],
          }" />
        {{ t('已完成') }}
      </div>
      <div class="item">
        <BMIcons
          icon="DCCircle"
          :style="{
            fontSize: '8px',
            width: '8px',
            height: '8px',
            color: colors[3],
          }" />
        {{ t('已结束') }}
      </div>
    </div>
    <!-- 新增滚动容器 -->
    <div class="progress-list">
      <div v-for="progress in progressData" :key="progress.batchNo" class="progress">
        <div class="progress_header">
          <div class="progress_header_title_item">
            <span class="progress_header_title_item_title">{{ t('工艺名称') }}</span>
            <span class="progress_header_title_item_value">{{ progress.processName }}</span>
          </div>
          <div class="progress_header_line"></div>
          <div class="progress_header_title_item">
            <span class="progress_header_title_item_title">{{ t('生产批号') }}</span>
            <span class="progress_header_title_item_value">{{ progress.batchNo }}</span>
          </div>
        </div>
        <div class="process-container">
          <div
            v-for="(row, rowIndex) in getRows(progress)"
            :key="'row-' + rowIndex"
            :class="[
              'process-row',
              rowIndex % 2 === 0 ? 'even' : 'odd',
              rowIndex % 2 === 0 && rowIndex !== 0 ? 'even-row-not-first' : '',
            ]">
            <div v-for="(node, nodeIndex) in row" :key="node.procedureId" class="process-node">
              <template
                v-if="rowIndex === getRows(progress).length - 1 && nodeIndex === row.length - 1 && rowIndex % 2 !== 0">
                <div class="process-arrow" style="transform: rotate(180deg)"></div>
              </template>
              <!-- 连接线 -->
              <div
                v-if="
                  rowIndex === 0 ||
                  (rowIndex % 2 === 0
                    ? nodeIndex !== 0 ||
                      (rowIndex === getRows(progress).length - 1 && nodeIndex === row.length - 1 && row.length > 1)
                    : nodeIndex !== row.length - 1 || rowIndex === getRows(progress).length - 1)
                "
                class="process-line"></div>
              <Tooltip overlayClassName="process-item-tooltip">
                <template #title>
                  {{ node.showName }}
                </template>
                <div class="process_item">
                  <BMIcons
                    icon="DCCircle"
                    :style="{
                      fontSize: '20px',
                      width: '20px',
                      height: '20px',
                      color: colors[node.stateEnum?.value],
                    }" />
                  <span class="node-label">{{ nodeIndex + 1 + rowIndex * 11 }}</span>
                </div>
              </Tooltip>

              <!-- 末尾箭头 如果是最后一行最后一个 -->
              <template
                v-if="rowIndex === getRows(progress).length - 1 && nodeIndex === row.length - 1 && rowIndex % 2 === 0">
                <div class="process-line"></div>
                <div class="process-arrow"></div>
              </template>
            </div>
            <!-- 换行转角 -->
            <div v-if="rowIndex < getRows(progress).length - 1 && rowIndex % 2 === 0" class="process-turn-even"></div>
            <div v-if="rowIndex < getRows(progress).length - 1 && rowIndex % 2 !== 0" class="process-turn-odd"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { currentLng, t } from '@bmos/i18n';
  import { TypeEnum } from './type';
  import { BMTable, TableColumn } from '@bmos/components';
  import { reqQueryBelarusDashboardDataCompleteBatch, reqQueryBelarusDashboardDataProductProgress } from '@/services';
  import { BMIcons } from '@bmos/icons';

  const emits = defineEmits(['toProcessProgress']);
  const props = withDefaults(
    defineProps<{
      typeActive: TypeEnum;
    }>(),
    {
      typeActive: TypeEnum.HUMAN_ALBUMIN,
    },
  );

  const toProcessProgress = () => {
    emits('toProcessProgress');
  };
  const images = import.meta.glob('@/assets/baiePng/*/*/*.png', { eager: true });
  const imageCommon = import.meta.glob('@/assets/baiePng/*.png', { eager: true });

  const titleImg = computed(() => {
    const lang = currentLng.value;
    return props.typeActive === TypeEnum.HUMAN_ALBUMIN
      ? images[`/src/assets/baiePng/${lang}/process/rightHumanAlbuminTitle.png`]?.default
      : images[`/src/assets/baiePng/${lang}/process/rightImmunoglobulinsTitle.png`]?.default;
  });

  const extraParams = computed(() => {
    return {
      type: props.typeActive === TypeEnum.HUMAN_ALBUMIN ? 2 : 3,
    };
  });
  const columns = computed<TableColumn[]>(() => {
    return [
      {
        title: t('生产批号'),
        dataIndex: 'batchNo',
        width: 180,
      },
      {
        title: t('完成时间'),
        dataIndex: 'endTime',
        width: 120,
      },
      {
        title: t('产量（瓶）'),
        dataIndex: 'production',
        width: 100,
        className: 'production-text',
        customRender: ({ record }: any) => {
          return formatNumber(record.production);
        },
      },
    ];
  });

  // 添加格式化数字的函数
  const formatNumber = (num: string | number) => {
    if (!num) return '0';
    const parts = num.toString().split('.');
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return parts.join('.');
  };

  const getProcess = async (params: any) => {
    try {
      const { data } = await reqQueryBelarusDashboardDataProductProgress(params);
      if (data) {
        progressData.value = data;
      } else {
        progressData.value = [];
      }
    } catch (error) {
      progressData.value = [];
    }
  };
  const tableData = ref<any>([]);
  const request = async (params: any): Promise<any> => {
    try {
      const { data } = await reqQueryBelarusDashboardDataCompleteBatch(params);
      getProcess({ type: props.typeActive === TypeEnum.HUMAN_ALBUMIN ? 4 : 5 });
      if (data) {
        tableData.value = data;
      } else {
        tableData.value = [];
      }
    } catch (error) {
      tableData.value = [];
    }
  };

  const interval = ref<any>(null); // 定时器

  const intervalFn = async () => {
    if (interval.value) {
      clearInterval(interval.value);
    }
    const params = {
      type: props.typeActive === TypeEnum.HUMAN_ALBUMIN ? 2 : 3,
    };
    request(params);
    interval.value = setInterval(() => {
      request(params);
    }, 30000);
  };

  onMounted(async () => {
    intervalFn();
  });

  watch(
    () => props.typeActive,
    () => {
      intervalFn();
    },
  );

  onUnmounted(() => {
    clearInterval(interval.value);
  });

  const progressData = ref<any>([]);
  const colors: Record<number, string> = { 0: '#455C73', 1: '#3BBF42', 4: '#99D4FF', 3: '#FFC34C' }; // 状态颜色
  const rowLimit = 11; // 每行最多 10 个
  const getRows = (progress: any) => {
    const result = [];
    for (let i = 0; i < progress?.procedureList.length; i += rowLimit) {
      result.push(progress.procedureList.slice(i, i + rowLimit));
    }
    return result;
  };
</script>

<style lang="less">
  .bmos-table-content {
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
    .production-text {
      color: #40ffff;
    }
  }
  .process-item-tooltip {
    .dc-tooltip-inner {
      min-height: 24px;
      border-radius: 4px;
      background: #174775;
      box-shadow: 0px 0px 4px 0px #102248;
      color: #c3d7e5;
      text-align: center;
      font-family: 'Source Han Sans CN';
      font-size: 12px;
      font-style: normal;
      font-weight: 400;
      line-height: 14px; /* 111.111% */
    }
  }
</style>
<style lang="less" scoped>
  .container {
    display: flex;
    width: 480px;
    padding: 20px;
    justify-content: center;
    flex-direction: column;
    align-items: flex-start;
    flex-shrink: 0;
    gap: 15px;
    border-bottom: 1px solid rgba(85, 158, 255, 0);
    border-bottom: 1px solid rgba(85, 158, 255, 0);
    background: linear-gradient(360deg, rgba(26, 34, 51, 0.65) 0%, rgba(26, 34, 51, 0.45) 100%);

    .title {
      background-image: url('@/assets/baiePng/processTitleBg.png');
      background-repeat: no-repeat;
      height: 24px;
      width: 100%;
      color: #fff;
      font-family: 'Source Han Sans CN';
      font-size: 16px;
      font-style: normal;
      font-weight: 400;
      line-height: 20px; /* 125% */
      padding-left: 24px;
      display: flex;
      justify-content: space-between;
      .right {
        display: flex;
        align-items: center;
        font-size: 14px;
        gap: 5px;
        cursor: pointer;
      }
    }
    .process-title {
      display: flex;
      align-items: center;
      padding: 5px 10px;
      gap: 20px;
      border-radius: 4px 0px 0px 4px;
      background: linear-gradient(90deg, rgba(102, 178, 255, 0.2) 0%, rgba(102, 178, 255, 0) 100%);
      .item {
        display: flex;
        align-items: center;
        gap: 4px;
        color: #8fa4b2;
        font-size: 12px;
      }
    }
  }
  .progress-list {
    max-height: calc(2 * 125px + 10px); // 120px为每个.progress的预估高度，可根据实际调整
    overflow-y: auto;
    width: 100%;
    overflow-x: hidden;
    margin-bottom: 10px;
  }
  .progress {
    min-height: 120px; // 保证每个progress高度一致，如需调整可改
    margin-bottom: 10px;
    border-radius: 16px;
    border: 1.2px solid var(--555555, rgba(178, 217, 255, 0.2));
  }
  .progress_header {
    display: flex;
    width: 440px;
    padding: 8px 15px;
    align-items: center;
    gap: 15px;
  }
  .progress_header_title_item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    flex: 1 0 0;
  }
  .progress_header_line {
    width: 1px;
    height: 18px;
    background: linear-gradient(
      90deg,
      rgba(117, 190, 254, 0.1) 0%,
      rgba(117, 190, 254, 0.5) 50.5%,
      rgba(117, 190, 254, 0.1) 100%
    );
  }
  .progress_header_title_item_title {
    color: var(---, #98bed9);
    text-align: center;

    /* 中文/12px */
    font-family: 'Source Han Sans CN';
    font-size: 12px;
    font-style: normal;
    font-weight: 400;
    line-height: 16px; /* 133.333% */
  }
  .progress_header_title_item_value {
    color: #fff;
    text-align: center;

    /* 中文/14px */
    font-family: 'Source Han Sans CN';
    font-size: 14px;
    font-style: normal;
    font-weight: 400;
    line-height: 18px; /* 128.571% */
  }
  .process-container {
    display: flex;
    flex-direction: column;
    position: relative;
    padding: 0 10px 10px 10px;
    width: 440px;
  }

  .process-row {
    display: flex;
    align-items: center;
    position: relative;
  }
  .process-row.odd {
    align-self: flex-end;
    // 顺序反向
    flex-direction: row-reverse;
    margin-right: 3px;
  }
  .even-row-not-first {
    margin-left: 18px;
  }

  .process-node {
    display: flex;
    align-items: center;
    position: relative;
  }

  .node-label {
    color: #fff;
    text-align: center;
    font-family: 'Source Han Sans CN';
    font-size: 12px;
    font-style: normal;
    font-weight: 500;
    line-height: normal;
    line-height: 18px;
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
  }

  .process_item {
    width: 20px;
    height: 20px;
    position: relative;
    cursor: pointer;
  }

  .process-line {
    width: 18px;
    height: 2px;
    background-color: #66b2ff;
  }

  .process-turn-even {
    position: absolute;
    right: -5px;
    top: 9px;
    width: 20px;
    height: 20px;
    border: 2px solid #66b2ff;
    border-radius: 50%;
    clip-path: inset(0 0 0 50%);
  }

  .process-turn-odd {
    position: absolute;
    left: -8px;
    top: 6px;
    width: 20px;
    height: 20px;
    border: 2px solid #66b2ff;
    border-radius: 50%;
    clip-path: inset(0 50% 0 0);
  }

  .process-arrow {
    width: 0;
    height: 0;
    border-left: 10px solid #66b2ff;
    border-top: 5px solid transparent;
    border-bottom: 5px solid transparent;
  }
</style>
