<template>
  <view class="todo_box">
    <view class="todo_header">
      <view class="todo_title">
        <view v-for="item in tabList" :key="item.value" class="todo_title_item" @click="tabTypeChange(item.value)">
          <text class="todo_title_item_label">
            {{ item.label }}
            <view class="todo_title_item_num">
              {{ totalReactive[item.value] }}
            </view>
          </text>
          <view v-if="tabType === item.value" class="todo_title_item_line" />
        </view>
      </view>
      <BMFilter v-model="filterData" :form-props="filterFormProps" @confirm="filterConfirmOrReset" @reset="filterConfirmOrReset" />
    </view>
    <view class="todoList_box">
      <scroll-view
        v-if="todoList.length || validateList.length"
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
        <produceValidate v-if="validateList.length > 0" :validate-list="validateList" :type="tabType" />
        <Item
          v-for="(item, index) in todoList"
          :key="index"
          :data="item"
          @node-click="toWork"
        />
        <uv-load-more
          color="#B6B9BF"
          font-size="11.72rpx"
          :status="loadMoreStatus"
          :loading-text="t('正在加载')"
          :loadmore-text="t('加载更多')"
          :nomore-text="t('没有更多了')"
        />
      </scroll-view>
      <BMNoData
        v-else
        type="emptyData"
        :text="t('暂无待办')"
      />
    </view>
    <!-- 未开始任务弹窗 -->
    <BMMessageBox
      v-model="isAction"
      :title="t('开始执行')"
      :content="t('是否开始当前步骤/任务')"
      @confirm="confirmStart"
    />
    <!-- 强制开启任务弹窗 -->
    <BMModal
      v-model="signBoxShow"
      :title="t('未满足执行条件')"
      size="medium"
      overflow="visible"
      :default-padding="false"
      closable
    >
      <view class="sign_box">
        <view class="sign_title">
          {{ t("未满足执行条件：") }}{{ signTitle
          }}{{ t("是否开启当前步骤/任务？") }}
        </view>
        <BMSign
          ref="BMSignRef"
          v-model="signValue"
          :label-list="labelList"
        />
      </view>
      <template #buttons>
        <view class="sign_btn_box">
          <view
            class="sign_btn sign_btn_cancle"
            @click="signBoxShow = false"
          >
            {{ t("取消") }}
          </view>
          <view
            class="sign_btn sign_btn_config"
            @click="signConfig"
          >
            {{ t("开启") }}
          </view>
        </view>
      </template>
    </BMModal>
  </view>
</template>

<script setup>
import { getTodoPageApi } from '@/api/todoApi.js';
import { getStepGroupUserApi, reqActiveStepApi } from '@/api/webViewApi.js';
import {
  BMFilter,
  BMMessageBox,
  BMModal,
  BMNoData,
  BMSign,
} from '@/BMComponents';
import { useSystemInfoStore } from '@/stores/systemInfo.js';
import { useTabbarStore } from '@/stores/tabbar.js';
import { BMOS_ACCESS_TOKEN } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { queryParams } from '@climblee/uv-ui/libs/function/index.js';
import { onHide, onShow } from '@dcloudio/uni-app';
import { onMounted, ref } from 'vue';
import Item from './components/item.vue';
import produceValidate from './components/produceValidate.vue';
import { usePage } from './hooks/useDatas';

const tabBarStore = useTabbarStore();
const { setTodoCount } = tabBarStore;
const systemInfoStore = useSystemInfoStore();
const { getParameterByCode } = systemInfoStore;
const {
  params,
  total,
  todoList,
  loadMoreStatus,
  triggered,
  filterFormProps,
  filterData,
  filterConfirmOrReset,
  getTodoList,
  validateList,
  tabList,
  totalReactive,
  tabType,
  tabTypeChange,
} = usePage();
const totalTimer = ref(null);
const long = ref(30 * 1000);
const isAction = ref(false);
const messageItem = ref({});
const signBoxShow = ref(false);
const signTitle = ref('');
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    // 签名动作
    signatureAction: 105,
    options: null,
    disabled: true,
  },
]);
const BMSignRef = ref();

onMounted(() => {
  // 获取参数配置
  const data = getParameterByCode('platform.sys.app-msg-polling-time');
  if (data.value) {
    // 将获取的字符串转换为数字
    long.value = (Number(data.value) || 30) * 1000;
    startTimer();
  }
});

// 点击某一个节点
function toWork(item, jump = false) {
  const { activeState, nodeFunction } = item;
  if (activeState || jump) {
    // const params = `?processInstanceId=${processInstanceId}&batchNo=${batchNo}&nodeId=${nodeId}&processId=${processId}&processVersion=${processVersion}&productPlanId=${productPlanId}&taskId=${taskId}&executePaused=${executePaused}`;
    if (nodeFunction.value === '3' || nodeFunction.value === '4') {
      // 3工序换班4工艺换班
      uni.navigateTo({
        url: `/pages/processChange/index${queryParams({
          ...item,
          nodeFunction: nodeFunction.value,
          isToDo: true,
        })}`,
      });
    }
    else if (nodeFunction.value === '6') {
      // 请验
      uni.navigateTo({
        url: `/pages/pleaseVerify/index${queryParams(item)}`,
      });
    }
    else {
      uni.navigateTo({
        url: `/pages/webview/index${queryParams(item)}`,
      });
    }
  }
  else {
    isAction.value = true;
    messageItem.value = item;
  }
}

async function confirmStart() {
  if (!messageItem.value) {
    return;
  }
  try {
    const { procedureStepModelId, planId, executionId } = messageItem.value;
    await reqActiveStepApi({
      procedureStepModelId,
      planId,
      executionId,
    });
    toWork(messageItem.value, true);
  }
  catch (error) {
    signTitle.value = error.message;
    const {
      nodeId,
      productPlanId,
      nodeFunction,
      processChangeNumber,
      procedureChangeNumber,
    } = messageItem.value;
    // 操作人列表
    const res = await getStepGroupUserApi({
      nodeId,
      productPlanId,
      nodeFunction: nodeFunction.value,
      processChangeNumber,
      procedureChangeNumber,
    });
    labelList.value[0].options = (res.data || []).map((item) => {
      const { userName, loginName, userId } = item;
      return {
        text: `${userName}-${loginName}`,
        value: loginName,
        id: userId,
        userName,
      };
    });
    signBoxShow.value = true;
    messageItem.value = null;
  }
}

async function signConfig() {
  try {
    await BMSignRef.value.checkSign();
    signBoxShow.value = false;
  }
  catch (error) {
    error?.message
    && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
}

onShow(async () => {
  console.log('待办页面展示');
  params.pageNum = 1;
  await getTodoList();
  getTodoTotal();
  startTimer();
});
onHide(() => {
  console.log('待办页面隐藏');
  clearTimer();
});

// 开启定时器
function startTimer() {
  clearTimer();
  totalTimer.value = setInterval(() => {
    console.log('定时刷新待办总数');
    getTodoTotal();
  }, long.value);
}

function clearTimer() {
  if (totalTimer.value) {
    clearInterval(totalTimer.value);
    totalTimer.value = null;
  }
}

async function getTodoTotal() {
  try {
    if (getStorageSync(BMOS_ACCESS_TOKEN)) {
      const { data } = await getTodoPageApi({
        menuCode: '121010002000001',
        todoType: tabType.value,
      });
      totalReactive.value = {
        present_todo: data.presentTodoCount,
        future_todo: data.futureTodoCount,
      };
      setTodoCount(data.count);
    }
    else {
      clearTimer();
    }
  }
  catch (e) {
    console.log(e);
  }
}

// 下拉刷新触发
async function onRefresh() {
  params.pageNum = 1;
  triggered.value = true;
  getTodoList();
}
// 上拉触底
function onScrolltolower() {
  console.log('上拉触底');
  if (
    params.pageNum * params.pageSize < total.value
    && triggered.value === false
  ) {
    params.pageNum++;
    loadMoreStatus.value = 'loading';
    getTodoList();
  }
}
</script>

<style lang="scss" scoped>
  .todo_box {
  height: 100%;
  position: relative;
  box-sizing: border-box;
  .todo_header {
    display: flex;
    height: 42.19rpx;
    align-items: center;
    justify-content: space-between;
    background-color: #fff;
    padding: 0 9.38rpx;
    .todo_title {
      width: 234.38rpx;
      display: flex;
      align-items: center;
      height: 100%;
      .todo_title_item {
        width: 50%;
        height: 100%;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        .todo_title_item_label {
          position: relative;
          font-size: 14.06rpx;
          line-height: 16.41rpx;
          font-weight: 500;
          .todo_title_item_num {
            position: absolute;
            right: -23.44rpx;
            top: 0;
            width: 14.06rpx;
            height: 10.55rpx;
            padding: 1.17rpx 2.34rpx 1.17rpx 2.34rpx;
            border-radius: 20px 20px 20px 20px;
            background: #ff4c26;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 8.2rpx;
          }
        }
        .todo_title_item_line {
          width: 37.5rpx;
          height: 3.52rpx;
          border-radius: 2.34rpx;
          background: #2871ff;
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          margin: auto;
        }
      }
    }
  }
  .todoList_box {
    height: calc(100% - 42.19rpx);
    padding: 0 9.38rpx;
  }
  .scroll-class {
    height: calc(100% - 16.25rpx);
    box-sizing: border-box;
  }
}
.filter-content {
  padding: 0 9.38rpx;
  .filter-item {
    margin-top: 5.86rpx;
    .item-title {
      font-size: 11.72rpx;
      margin-top: 18.75rpx;
      margin-bottom: 5.86rpx;
    }
  }
}
.sign_box {
  padding: 0 9.38rpx;
  .sign_title {
    color: #9da0a6;
    text-align: center;
    margin-bottom: 14.06rpx;
  }
}
.sign_btn_box {
  display: flex;
  justify-content: space-between;
  .sign_btn {
    height: 33.98rpx;
    line-height: 33.98rpx;
    text-align: center;
    font-size: 12.89rpx;
    border-radius: 4.69rpx;
    width: calc(50% - 4.69rpx);
  }
  .sign_btn_cancle {
    border: 1px solid #b6b9bf;
  }
  .sign_btn_config {
    color: #ff4c26;
    border: 1px solid #ff4c26;
  }
}
:deep(.modal-content) {
  min-height: 0 !important;
}
</style>
