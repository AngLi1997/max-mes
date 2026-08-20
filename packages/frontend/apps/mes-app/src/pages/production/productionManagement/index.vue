<template>
  <BMLayout>
    <BMBasicPage
      :title="title"
      :default-padding="false"
      :show-buttons="false"
      background-color="#F2F3F5"
      @left-click="toBack"
    >
      <template #titleRight>
        <view class="action">
          <BMFilter v-model="filterData" :form-props="filterFormProps" @confirm="filterConfirmOrReset" @reset="filterConfirmOrReset" />
          <BMFilter v-model="sortData" :title="t('排序')" icon="paixu" :form-props="sortFormProps" @confirm="filterConfirmOrReset" @reset="filterConfirmOrReset" />
        </view>
      </template>
      <view class="container">
        <view class="content">
          <scroll-view
            class="scroll-class"
            scroll-y="true"
            refresher-enabled="true"
            :refresher-triggered="triggered"
            :refresher-threshold="100"
            :lower-threshold="70"
            refresher-default-style="white"
            @refresherrefresh="onRefresh"
            @scrolltolower="onScrolltolower"
          >
            <view
              v-if="list.length"
              class="production-list"
            >
              <uv-waterfall
                v-model="list"
                column-gap="9.38rpx"
              >
                <!-- 第一列数据 -->
                <template #list1>
                  <!-- 为了磨平部分平台的BUG，必须套一层view -->
                  <view>
                    <ProductionItem
                      v-for="(item) in list1"
                      :key="item.id"
                      icon="gongxu2"
                      :item="item"
                      process-icon
                      :history="isProductionHistory"
                      class="waterfall-item"
                      @click="clickHandle(item)"
                    />
                  </view>
                </template>
                <!-- 第二列数据 -->
                <template #list2>
                  <!-- 为了磨平部分平台的BUG，必须套一层view -->
                  <view>
                    <ProductionItem
                      v-for="(item) in list2"
                      :key="item.id"
                      icon="gongxu2"
                      :item="item"
                      process-icon
                      :history="isProductionHistory"
                      class="waterfall-item"
                      @click="clickHandle(item)"
                    />
                  </view>
                </template>
              </uv-waterfall>
              <uv-load-more
                color="#B6B9BF"
                font-size="11.72rpx"
                :status="loadMoreStatus"
                :loading-text="t('正在加载')"
                :loadmore-text="t('加载更多')"
                :nomore-text="t('没有更多了')"
              />
            </view>
            <BmosNoData
              v-else
              :text="t('暂无生产工艺')"
              type="emptyProductionBefore"
            />
          </scroll-view>
        </view>
      </view>
    </BMBasicPage>
  </BMLayout>
</template>

<script setup>
import {
  getProcedureLineApi,
  getProductionHistoryApi
  , getProductionManagementListApi,
  getProductTreeApi,
} from '@/api/productionApi.js';

import { BMBasicPage, BMFilter, BMLayout } from '@/BMComponents';
import BmosNoData from '@/components/BmosNoData/index.vue';
import ProductionItem from '@/pages/home/todo/components/todoItem.vue';
import { buildUrlQuery } from '@/utils/url';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { computed, reactive, ref } from 'vue';

const props = defineProps({
  productionHistory: String,
  productionRevision: String,
});
const triggered = ref(false);
const loadMoreStatus = ref('loadmore');
const filterData = ref({});

const isProductionHistory = ref(false);
const isProductionRevision = ref(false);
const params = reactive({
  pageNum: 1,
  pageSize: 20,
});
const list = ref([]);
const list1 = ref([]);
const list2 = ref([]);
const total = ref(0);
const sortData = ref({});
const title = computed(() =>
  isProductionHistory.value
    ? isProductionRevision.value
      ? t('生产修订')
      : t('生产历史')
    : t('生产管理'),
);

// 获取生产管理列表信息
const getProductionManagementList = async () => {
  const api = isProductionHistory.value
    ? getProductionHistoryApi
    : getProductionManagementListApi;
  const res = await api({ ...params, ...filterData.value, ...sortData.value });
  const { data } = res;
  total.value = data.total;
  if (params.pageNum === 1) {
    list.value = data.list;
  }
  else {
    list.value = list.value.concat(data.list);
  }
  list1.value = [];
  list2.value = [];
  list.value.forEach((item, index) => {
    if (index % 2 === 0) {
      list1.value.push(item);
    }
    else {
      list2.value.push(item);
    }
  });
  triggered.value = false;
  loadMoreStatus.value
      = total.value >= list.value.length ? 'loadmore' : 'nomore';
};

onShow(() => {
  isProductionHistory.value = props.productionHistory === 'true';
  isProductionRevision.value = props.productionRevision === 'true';
  params.pageNum = 1;
  getProductionManagementList();
});
// 下拉刷新触发
const onRefresh = async () => {
  console.log('下拉触发时，triggered状态', triggered.value);
  params.pageNum = 1;
  triggered.value = true;
  getProductionManagementList();
};
// 上拉触底
const onScrolltolower = () => {
  console.log('上拉触底');
  if (
    params.pageNum * params.pageSize < total.value
    && triggered.value === false
  ) {
    params.pageNum++;
    loadMoreStatus.value = 'loading';
    getProductionManagementList();
  }
};

const toBack = () => {
  uni.navigateBack();
};

// 筛选重置
const filterConfirmOrReset = () => {
  params.pageNum = 1;
  getProductionManagementList();
};

const clickHandle = (item) => {
  const {
    executeProcessInstanceId,
    productMergeCode,
    productName,
    batchNo,
    processId,
    processName,
    processVersion,
    processVersionId,
    productPlanId,
    executePaused,
    lineName,
  } = item;

  const urlParams = {
    id: executeProcessInstanceId,
    productMergeCode,
    productName,
    batchNo,
    processId,
    processName,
    processVersion,
    processVersionId,
    productPlanId,
    executePaused,
    lineName,
    ...props,
  };
  uni.navigateTo({
    url: `/pages/production/productionManagement/craftsmanshipFlow/index?${buildUrlQuery(urlParams)}`,
  });
};

const getChildrenData = (arr) => {
  const newArr = [];
  arr.map((item) => {
    item.categoryFlag = !item.categoryFlag;
    if (item.children.length > 0) {
      item.children = getChildrenData(item.children);
    }
    newArr.push(item);
    return item;
  });
  return newArr;
};
const getChildrenList = (list, parentId) => {
  if (!list) {
    return [];
  }
  const newChildren = [];
  list.forEach((item) => {
    const children = getChildrenList(item.children, item.id);
    item.name = `${item.code}-${item.name}`;
    item.categoryFlag = !item.parentId;
    item.parentId = item.parentId ?? parentId;
    if (item.infoList) {
      item.infoList.forEach((infoItem) => {
        infoItem.name = `${infoItem.code}-${infoItem.name}`;
        infoItem.categoryFlag = !infoItem.parentId;
        infoItem.parentId = infoItem.parentId ?? item.id;
      });
      item.children = [...children, ...item.infoList];
    }
    else {
      item.children = [...children];
    }
    newChildren.push(item);
  });
  return newChildren;
};
// 筛选表单配置
const filterFormProps = reactive({
  schemas: [
    {
      field: 'productId',
      component: 'BMFormSelect',
      label: t('产品名称'),
      colProps: {
        span: 24,
      },
      componentProps: () => {
        return {
          request: async () => {
            const { data } = await getProductTreeApi({ categoryType: 2 });
            return getChildrenData(data);
          },
          title: t('产品名称'),
          type: 'tree',
          mode: 'multiple',
          fieldNames: {
            name: 'showName',
            key: 'id',
            checkKey: 'categoryFlag',
            checkKeyValue: true,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
        };
      },
    },
    {
      field: 'batchNo',
      component: 'Input',
      label: t('批号'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'lineId',
      component: 'BMFormSelect',
      label: t('产线'),
      colProps: {
        span: 24,
      },
      componentProps: () => {
        return {
          request: async () => {
            const { data } = await getProcedureLineApi();
            const options = getChildrenList(data);
            return options;
          },
          title: t('产线名称'),
          type: 'tree',
          mode: 'multiple',
          fieldNames: {
            key: 'id',
            checkKey: 'categoryFlag',
            checkKeyValue: true,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
        };
      },
    },
  ],
});
// 排序表单配置
const sortFormProps = reactive({
  schemas: [
    {
      field: 'orderBy',
      component: 'BMFormRadio',
      label: t('开始时间'),
      colProps: {
        span: 24,
      },
      componentProps: {
        options: [
          {
            label: t('顺序排列'),
            value: 'start_time asc',
          },
          {
            label: t('逆序排列'),
            value: 'start_time desc',
          },
        ],
      },
    },
    {
      field: 'orderBy',
      component: 'BMFormRadio',
      label: t('生产批号'),
      colProps: {
        span: 24,
      },
      componentProps: {
        options: [
          {
            label: t('顺序排列'),
            value: 'batch_no asc',
          },
          {
            label: t('逆序排列'),
            value: 'batch_no desc',
          },
        ],
      },
    },
  ],
});
</script>

<style lang="scss" scoped>
  // $show-lines: 1;
// @import '@climblee\uv-ui\libs\css\variable.scss';

.left-content {
  display: flex;

  .title {
    font-family: Source Han Sans CN;
    font-size: 15.24rpx;
    font-weight: 500;
    line-height: 22.27rpx;
    letter-spacing: 0em;
    color: #18191a;
    margin-left: 14.65rpx;
  }
}

.action {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  flex-shrink: 0;
  gap: 11.72rpx;
}

.content {
  height: 100%;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  padding: 7.03rpx 9.38rpx;
  display: flex;
  flex-direction: column;
  position: relative;
}
.container {
  height: 100%;
}
.scroll-class {
  height: 100%;
}
</style>
