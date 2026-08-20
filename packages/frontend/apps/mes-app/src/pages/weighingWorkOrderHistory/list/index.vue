<template>
  <BMBasicPage
    :title="t('称量工单历史')"
    :show-buttons="false"
    background-color="var(--bmos-color-bg)"
    @left-click="leftClick"
  >
    <template #titleRight>
      <view class="action">
        <BMFilter
          v-model="filterData"
          :form-props="filterFormProps"
          @confirm="filterConfirmOrReset"
          @reset="filterConfirmOrReset"
        />
        <BMFilter
          v-model="sortData"
          :title="t('排序')"
          icon="paixu"
          :form-props="sortFormProps"
          @confirm="filterConfirmOrReset"
          @reset="filterConfirmOrReset"
        />
      </view>
    </template>
    <view class="list-content">
      <scroll-view
        v-if="listData.length"
        class="scroll-class"
        scroll-y="true"
        refresher-enabled="true"
        :refresher-triggered="triggered"
        :refresher-threshold="100"
        :lower-threshold="70"
        refresher-default-style="white"
        @refresherrefresh="onRefresh"
        @scrolltolower="onScrollToLower"
      >
        <view class="list-box">
          <view class="list">
            <Item v-for="(item) in list1" :key="item.id" :item="item" @click="itemClick(item)" />
          </view>
          <view class="list">
            <Item v-for="(item) in list2" :key="item.id" :item="item" @click="itemClick(item)" />
          </view>
        </view>
        <wd-loadmore :state="loadMoreStatus" />
      </scroll-view>
      <BMNoData v-else type="emptyData" :text="t('暂无称量任务')" />
    </view>
  </BMBasicPage>
</template>

<script setup>
import { BMBasicPage, BMFilter, BMNoData } from '@/BMComponents';
import { t } from '@/utils/useBmosI18n.js';
import Item from './components/item.vue';
import { usePage } from './hooks/usePage.js';

// 筛选表单配置
const filterFormProps = {
  schemas: [
    {
      field: 'material',
      component: 'Input',
      label: t('物料信息'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'centre',
      component: 'Input',
      label: t('称量中心'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'ticketNo',
      component: 'Input',
      label: t('工单编号'),
      colProps: {
        span: 24,
      },
    },
  ],
};
  // 排序表单配置
const sortFormProps = {
  schemas: [
    {
      field: 'sortCompleteTime',
      component: 'BMFormRadio',
      label: t('任务完成时间'),
      colProps: {
        span: 24,
      },
      componentProps: ({ formModel }) => {
        return {
          options: [
            {
              label: t('顺序排列'),
              value: 'completeTime asc',
            },
            {
              label: t('逆序排列'),
              value: 'completeTime desc',
            },
          ],
          onChange: () => {
            formModel.sortSendTime = '';
            formModel.sortTicketNo = '';
          },
        };
      },
    },
    {
      field: 'sortSendTime',
      component: 'BMFormRadio',
      label: t('任务下发时间'),
      colProps: {
        span: 24,
      },
      componentProps: ({ formModel }) => {
        return {
          options: [
            {
              label: t('顺序排列'),
              value: 'sendTime asc',
            },
            {
              label: t('逆序排列'),
              value: 'sendTime desc',
            },
          ],

          onChange: () => {
            formModel.sortCompleteTime = '';
            formModel.sortTicketNo = '';
          },
        };
      },
    },
    {
      field: 'sortTicketNo',
      component: 'BMFormRadio',
      label: t('工单编号'),
      colProps: {
        span: 24,
      },
      componentProps: ({ formModel }) => {
        return {
          options: [
            {
              label: t('顺序排列'),
              value: 'taskNo asc',
            },
            {
              label: t('逆序排列'),
              value: 'taskNo desc',
            },
          ],
          onChange: () => {
            formModel.sortCompleteTime = '';
            formModel.sortSendTime = '';
          },
        };
      },
    },
  ],
};

const {
  filterData,
  sortData,
  triggered,
  listData,
  list1,
  list2,
  loadMoreStatus,
  leftClick,
  onRefresh,
  onScrollToLower,
  itemClick,
  filterConfirmOrReset,
} = usePage();
</script>

<style lang="scss" scoped>
:deep(.action) {
  display: flex;
  align-items: center;
  justify-content: end;
  gap: 11.72rpx;
}
:deep(.bm-table) {
  overflow: hidden;
}
:deep(.bm-table-show-border) {
  border: none;
}
.list-content {
  position: relative;
  height: 100%;
  overflow: hidden;
  .scroll-class {
    height: 100%;
    box-sizing: border-box;
    .list-box {
      display: flex;
      row-gap: 9.38rpx;
      column-gap: 9.38rpx;
      padding: 9.38rpx 0;
      .list {
        width: 100%;
        display: flex;
        flex-direction: column;
        row-gap: 9.38rpx;
      }
    }
  }
}
</style>
