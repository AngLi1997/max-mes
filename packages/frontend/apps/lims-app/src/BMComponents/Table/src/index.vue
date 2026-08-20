<template>
  <view class="bm-table" :class="[(props.showBorder || tableData.length === 0) ? 'bm-table-show-border' : '', (props.showNoData && tableData.length === 0) ? 'bm-table-show-no-data' : '']">
    <uni-table
      ref="tableRef"
      class="bm-table-container"
      :class="[
        props.showAllCheck ? 'show-all-checkbox' : '',
        paginationRef.bottomOutRefresh ? 'bottom-out-refresh' : '',
        (!props.noDataShowTable && (props.showNoData && tableData.length === 0)) ? 'bm-table-hidden' : '',
      ]"
      :loading="intervalRequest ? false : loadingRef"
      :empty-text="props.showNoData ? '' : t('暂无数据')"
      v-bind="getBindValues"

      @selection-change="selectionChange"
    >
      <template v-if="paginationRef.bottomOutRefresh">
        <uni-tr class="header">
          <uni-th
            v-for="item in getThBindValues"
            :key="item.prop"
            v-bind="omit(item, ['width'])"
            :class="[item.fixed === 'right' ? 'fixed-right' : '', item.fixed === 'left' ? 'fixed-left' : '']"
            :style="{
              ...(item.width && {
                width: item.width,
              }),
            }"
          >
            {{ item.label }}
          </uni-th>
        </uni-tr>
        <scroll-view
          class="table-scroll-class"
          scroll-y="true"
          refresher-enabled="true"
          :refresher-triggered="triggered"
          :refresher-threshold="100"
          :lower-threshold="70"
          refresher-default-style="white"
          @refresherrefresh="onRefresh"
          @scrolltolower="onScrollToLower"
        >
          <uni-tr class="header" style="opacity: 0; height: 0; line-height: 0;">
            <uni-th
              v-for="item in getThBindValues"
              :key="item.prop"
              v-bind="omit(item, ['width'])"
              :class="[item.fixed === 'right' ? 'fixed-right' : '', item.fixed === 'left' ? 'fixed-left' : '']"
              :style="{
                ...(item.width && {
                  width: item.width,
                }),
              }"
            >
              {{ item.label }}
            </uni-th>
          </uni-tr>
          <uni-tr
            v-for="(item, index) in tableData"
            :key="item[rowKey]"
            class="body-tr"
            :class="[computedTrBindValues(item)?.disabled ? 'disable-check' : '']"
            v-bind="computedTrBindValues(item)"
          >
            <template v-for="tdProps in getTdBindValues" :key="tdProps.prop">
              <BmTableCol
                v-bind="omit(tdProps, ['width'])"
                :row="item"
                :show-pagination="props.pagination"
                :pagination-ref="paginationRef"
                :index="index"
                @update-row="updateRowChange"
              />
            </template>
          </uni-tr>
          <uv-load-more
            color="#B6B9BF"
            font-size="9.38rpx"
            :status="loadMoreStatus"
            :loading-text="t('正在加载')"
            :loadmore-text="t('加载更多')"
            :nomore-text="t('没有更多了')"
          />
        </scroll-view>
      </template>
      <template v-else>
        <uni-tr class="header">
          <uni-th
            v-for="item in getThBindValues"
            :key="item.prop"
            v-bind="omit(item, ['width'])"
            :class="[item.fixed === 'right' ? 'fixed-right' : '', item.fixed === 'left' ? 'fixed-left' : '']"
            :style="{
              ...(item.width && {
                width: item.width,
              }),
            }"
          >
            {{ item.label }}
          </uni-th>
        </uni-tr>
        <uni-tr
          v-for="(item, index) in tableData"
          :key="item[rowKey]"
          class="body-tr"
          :class="[computedTrBindValues(item)?.disabled ? 'disable-check' : '']"
          v-bind="computedTrBindValues(item)"
        >
          <template v-for="tdProps in getTdBindValues" :key="tdProps.prop">
            <BmTableCol
              v-bind="omit(tdProps, ['width'])"
              :row="item"
              :show-pagination="props.pagination"
              :pagination-ref="paginationRef"
              :index="index"
              @update-row="updateRowChange"
            />
          </template>
        </uni-tr>
      </template>
    </uni-table>
    <view v-if="props.showNoData && tableData.length === 0" class="no_data_box">
      <BMNoData :type="props.noDataType || 'emptyData'" :text="props.noDataText || t('暂无数据')" />
    </view>
    <view
      v-if="paginationRef && !paginationRef.bottomOutRefresh"
      class="pagination-box"
      :class="[(!props.noDataShowTable && (props.showNoData && tableData.length === 0)) ? 'bm-table-hidden' : '']"
    >
      <PaginationCom
        v-model="paginationRef.current"
        v-bind="paginationRef"
        @change="paginationChange"
      />
      <wd-popover
        v-if="paginationRef.showJumper"
        v-model="showPopover"
        mode="menu"
        :content="props.paginationMenu"
        placement="top"

        @menuclick="pageMenuClick"
      >
        <wd-button plain size="small" type="info" @click.self="showPopover = !showPopover">
          {{ paginationRef.pageSize }}{{ t('条/页') }}<wd-icon name="arrow-down" />
        </wd-button>
      </wd-popover>
    </view>
  </view>
</template>

<script setup lang="jsx">
import { BMNoData } from '@/BMComponents';
import PaginationCom from '@/uni_modules/wot-design-uni/components/wd-pagination/wd-pagination.vue';
import { isDeepEqual } from '@/utils/is.js';
import { t } from '@/utils/useBmosI18n';
import { isFunction, omit } from 'lodash-es';
import { computed, onUnmounted, watch } from 'vue';
import BmTableCol from './BmTableCol.vue';
import { createTableContext, useTableMethods, useTableState } from './hooks';
import { tableProps } from './tableProps.js';

const props = defineProps(tableProps);
const emit = defineEmits([
  'register',
  'change',
  'selection-change',
  'updateRow',
]);
const tableState = useTableState({ props });
const {
  tableRef,
  tableData,
  getBindValues,
  getThBindValues,
  getTdBindValues,
  paginationRef,
  loadingRef,
  showPopover,
  loadMoreStatus,
  triggered,
  requestTimer,
} = tableState;
const tableMethods = useTableMethods({
  props,
  tableState,
  emit,
});

const computedTrBindValues = computed(() => (row) => {
  const trCustomProps = props.trProps && isFunction(props.trProps) ? props.trProps(row) : {};
  if (props.selectionProps && isFunction(props.selectionProps)) {
    return {
      ...props.selectionProps(row),
      ...trCustomProps,
    };
  }
  return trCustomProps;
});

const {
  fetchData,
  paginationChange,
  selectionChange,
  updateRowChange,
  pageMenuClick,
  onRefresh,
  onScrollToLower,
  clearTimer,
} = tableMethods;

// 当前组件所有的状态和方法
const instance = {
  ...tableState,
  ...tableMethods,
};

fetchData({ ...props.extraParams });
// 如果 extraParams 发生变化，重新请求数据
watch(
  () => props.extraParams,
  (val, oldVal) => {
    if (val && !isDeepEqual(val, oldVal)) {
      fetchData({
        ...val,
        pageNum: 1,
      });
    }
  },
  {
    deep: true,
  },
);
if (props.intervalRequest) {
  requestTimer.value = setInterval(() => {
    fetchData();
  }, 60000);
}

onUnmounted(() => {
  clearTimer();
});

emit('register', instance);
createTableContext(instance);
defineExpose(instance);
</script>

<style lang="scss" scoped>
.bm-table-show-no-data {
  height: 177.19rpx !important;
}
.bm-table-show-border {
  border: 1px solid #ebeef5;
}
.bm-table {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  .bm-table-container {
    flex: 1;
  }
  .no_data_box {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    margin: auto;
  }
}
:deep(.bm-table-container .uni-table) {
  font-size: 11.72rpx;
  .header {
    height: 37.5rpx;
    position: sticky;
    top: 0;
    z-index: 2;
    .uni-table-th {
      font-size: 11.72rpx;
      font-weight: 513;
      word-break: break-all;
      color: #606266;
      background-color: #f5f6f7 !important;
      padding: 0 11.72rpx;
      vertical-align: middle;
    }
    .checkbox {
      background-color: #f5f6f7;
      .uni-table-checkbox {
        display: none;
      }
    }
    .fixed-right {
      position: sticky;
      right: 0;
      background-color: #f5f6f7;
    }
    .fixed-right::after {
      position: absolute;
      top: 0;
      bottom: -1px;
      left: 0;
      width: 2.14rem;
      transform: translateX(-100%);
      transition: box-shadow 0.3s;
      content: '';
      pointer-events: none;
      box-shadow: inset -10px 0 8px -8px rgba(5, 22, 38, 0.12);
    }
    .fixed-left {
      position: sticky;
      left: 0;
      background-color: #f5f6f7;
    }
    .fixed-left::after {
      position: absolute;
      top: 0;
      bottom: -1px;
      right: 0;
      width: 2.14rem;
      transform: translateX(100%);
      transition: box-shadow 0.3s;
      content: '';
      pointer-events: none;
      box-shadow: inset 10px 0 8px -8px rgba(5, 22, 38, 0.12);
    }
  }
  .body-tr {
    height: 37.5rpx;
    .uni-table-td {
      padding: 9.38rpx 11.72rpx;
      font-size: 11.72rpx;
      font-weight: 513;
      word-break: break-all;
      color: var(--bmos-color-text-main);
      vertical-align: middle;
      line-height: 1.1;
    }
    .checkbox {
      padding: 0 11.72rpx;
      .uni-table-checkbox {
        .checkbox__inner {
          width: 18.75rpx;
          height: 18.75rpx;
          border-radius: 4.69rpx;
          border-color: var(--bmos-color-icon-light);
          .checkbox__inner-icon {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%) rotate(45deg);
            height: 7.03rpx;
            width: 2.93rpx;
            border-width: 2.34rpx;
          }
        }
      }
    }
  }
  .wd-table__cell {
    padding: 0 11.72rpx;
  }
  .wd-table__header {
    height: 37.5rpx;
    .wd-table__cell {
      height: 37.5rpx;
    }
  }
  .uni-table-text {
    font-size: 11.72rpx;
    padding-top: 5.86rpx;
  }
}
:deep(.show-all-checkbox .uni-table) {
  .header {
    .checkbox {
      .uni-table-checkbox {
        display: flex;
        .checkbox__inner {
          width: 18.75rpx;
          height: 18.75rpx;
          border-radius: 4.69rpx;
          border-color: var(--bmos-color-icon-light);
          .checkbox__inner-icon {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%) rotate(45deg);
            height: 7.03rpx;
            width: 2.93rpx;
            border-width: 2.34rpx;
          }
        }
        .checkbox--indeterminate .checkbox__inner-icon {
          transform: translate(-125%, -50%) rotate(90deg);
        }
      }
    }
  }
}
:deep(.bm-table-hidden) {
  visibility: hidden;
}
:deep(.disable-check) {
  .uni-table-checkbox .checkbox__inner {
    background-color: #f2f6fc;
  }
}
:deep(.disable-check) {
  .uni-table-checkbox {
    cursor: not-allowed;
    user-select: none;
    pointer-events: none;
  }
}
:deep(.pagination-box) {
  display: flex;
  justify-content: center;
  align-items: center;
  .wd-pager {
    flex-grow: 1;
  }
  .wd-popover {
    width: 30%;
    .wd-popover__target {
      display: flex;
      justify-content: center;
    }
    .wd-transition {
      width: 82.03rpx;
      background: #6c6e73;
      .wd-popover__arrow {
        display: none;
      }
      .wd-popover__menu {
        background: #6c6e73;
        color: #fff;
        font-size: 11.72rpx;
      }
    }
  }
}

:deep(.bottom-out-refresh) {
  overflow: hidden;
  .uni-table {
    display: block;
    height: 100%;
    .table-scroll-class {
      height: calc(100% - 37.5rpx);
    }
  }
}
:deep(.uni-table-loading) {
  height: 29.3rpx;
  line-height: 29.3rpx;
}
</style>
