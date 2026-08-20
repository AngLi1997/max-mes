<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('消息中心')"
    :footer="null"
    wrap-class-name="modalSizeLarge message-notify-modal">
    <div class="message-notify-row">
      <div class="left">
        <div
          v-for="item in menuList"
          :key="item.key"
          :class="['menu-item', item.className]"
          @click="() => handleClickMenu(item)">
          <span class="item-name">{{ item.label }}</span>
          <div class="item-number">{{ item.number || '' }}</div>
        </div>
      </div>
      <div class="right">
        <Tabs v-model:activeKey="tabKey" tabPosition="top" @change="init">
          <template #leftExtra>
            <div style="width: 20px"></div>
          </template>
          <template #rightExtra>
            <div class="all-read" @click="handleAllRead">
              <BMIcons icon="Clear" class="all-read-icon"></BMIcons>
              {{ t('全部已读') }}
            </div>
          </template>
          <TabPane :key="MessageTabType.NOT_READ" :tab="t('未读消息')">
            <div v-for="item in unReadList" :key="item.id">
              <NotifyMessageItem :item="item" @read="readItem" />
            </div>
            <infinite-loading
              :identifier="identifier"
              :distance="5"
              spinner="wave"
              :immediate-check="true"
              @infinite="loadData"
              @error="loadFailed">
              <template #no-more>
                <div class="no-more">{{ t('没有更多内容') }}</div>
              </template>

              <template #loading>
                <div class="loading">{{ t('加载中') }}...</div>
              </template>

              <template #error>
                <div class="error" @click="loadFailed">{{ t('加载失败，点击重试') }}</div>
              </template>
            </infinite-loading>
          </TabPane>
          <TabPane :key="MessageTabType.READ" :tab="t('已读消息')">
            <div v-for="item in readList" :key="item.id">
              <NotifyMessageItem :item="item" :showReadIcon="false" />
            </div>
            <infinite-loading
              :identifier="identifier"
              :distance="10"
              spinner="wave"
              :immediate-check="true"
              @infinite="loadData"
              @error="loadFailed">
              <template #no-more>
                <div class="no-more">{{ t('没有更多内容') }}</div>
              </template>

              <template #loading>
                <div class="loading">{{ t('加载中') }}...</div>
              </template>

              <template #error>
                <div class="error" @click="loadFailed">{{ t('加载失败，点击重试') }}</div>
              </template>
            </infinite-loading>
          </TabPane>
        </Tabs>
      </div>
    </div>
  </NormalModalForm>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { NormalModalForm, Recordable } from '@bmos/components';
  import { Tabs, TabPane } from 'ant-design-vue';
  import { BMIcons } from '@bmos/icons';
  import { MessageTabType, NotifyMessageItemType, NotifyMessageType, NotifyMessageTypeMap } from '../types';
  import NotifyMessageItem from './components/NotifyMessageItem.vue';
  // @ts-ignore
  import InfiniteLoading from '@codog/vue3-infinite-loading';
  import { getNotifyMessageList, reqPlasmaNoticeAllRead } from '../../api/info';

  interface MenuList {
    key: NotifyMessageType;
    label: string;
    number: number;
    className?: string;
  }
  const emits = defineEmits(['readItem', 'readAll']);
  const open = defineModel<boolean>({ default: false });
  const props = withDefaults(
    defineProps<{
      notifyCount: Recordable;
    }>(),
    {
      notifyCount: () => ({}),
    },
  );

  const tabKey = ref<MessageTabType>(MessageTabType.NOT_READ);

  const menuKeys = ref<NotifyMessageType>(NotifyMessageType.ALL);

  const menuList = ref<MenuList[]>([
    {
      key: NotifyMessageType.ALL,
      label: t('全部消息'),
      number: 12,
      className: 'active',
    },
    {
      key: NotifyMessageType.AUDIT,
      label: t('审批'),
      number: 7,
    },
    {
      key: NotifyMessageType.WARNING,
      label: t('预警'),
      number: 5,
    },
    {
      key: NotifyMessageType.ALARM,
      label: t('告警'),
      number: 0,
    },
  ]);

  watchEffect(() => {
    menuList.value = menuList.value.map((menu: MenuList) => {
      return {
        ...menu,
        number: props.notifyCount[menu.key] || 0,
      };
    });
  });

  const handleClickMenu = (item: MenuList) => {
    menuList.value = menuList.value.map((menu: MenuList) => {
      if (menu.key === item.key) {
        menuKeys.value = item.key;
        return {
          ...menu,
          className: 'active',
        };
      }
      return {
        ...menu,
        className: '',
      };
    });
    init();
  };
  const unReadList = ref<NotifyMessageItemType[]>([]);
  const readList = ref<NotifyMessageItemType[]>([]);
  const identifier = ref(0); // 用于标识每次加载
  const page = ref(1); // 当前页码
  const hasMore = ref(true); // 是否有更多数据
  const isLoading = ref(false); // 是否正在加载
  const total = ref(0); // 总数
  // 模拟接口请求函数
  const fetchData = async (pageNumber: number) => {
    isLoading.value = true;
    try {
      const { data } = await getNotifyMessageList({
        messageStatus: tabKey.value,
        pageNum: pageNumber,
        pageSize: 10,
        ...(menuKeys.value !== NotifyMessageType.ALL && { messageType: NotifyMessageTypeMap.get(menuKeys.value) }),
      });
      if (data && data.list && data.list.length > 0) {
        if (tabKey.value === MessageTabType.NOT_READ) {
          unReadList.value.push(...data.list);
        } else {
          readList.value.push(...data.list);
        }
        page.value++;
        total.value = data.total;
        if (data.list.length < 10) {
          hasMore.value = false; // 没有更多数据
        }
      } else {
        hasMore.value = false; // 没有更多数据
      }
    } catch (error: any) {
      throw new Error(error); // 模拟加载失败
    } finally {
      isLoading.value = false;
    }
  };
  // 加载更多数据的处理
  const loadData = async (state: any) => {
    if (isLoading.value || !hasMore.value) {
      state.complete(); // 无更多数据或正在加载时，不做操作
      return;
    }

    try {
      await fetchData(page.value);
      state.loaded(); // 数据加载成功，标记完成
      if (!hasMore.value) {
        state.complete(); // 如果没有更多数据，标记完成
      }
    } catch (error) {
      state.error(); // 加载失败，触发错误状态
    }
  };

  // 处理加载失败
  const loadFailed = () => {
    fetchData(page.value); // 点击错误时，重新加载数据
  };

  const init = () => {
    page.value = 1;
    hasMore.value = true;
    unReadList.value = [];
    readList.value = [];
    identifier.value = Date.now();
  };

  const readItem = (item: any) => {
    unReadList.value = unReadList.value.filter((i: NotifyMessageItemType) => i.id !== item.id);
    emits('readItem', item);
    // total page 10 判断是否还有数据 如果有 且当前 unReadList.length < 10 则继续加载
    if (hasMore.value && unReadList.value.length < 10) {
      fetchData(1);
    }
  };
  const handleAllRead = async () => {
    try {
      await reqPlasmaNoticeAllRead({
        all: true,
        ...(menuKeys.value !== NotifyMessageType.ALL && { messageType: NotifyMessageTypeMap.get(menuKeys.value) }),
      });
      init();
      emits('readAll');
    } catch (error) {
      console.log(error);
    }
  };

  watch(
    () => open.value,
    val => {
      if (val) {
        init();
      }
    },
  );
</script>

<style lang="less">
  .message-notify-modal {
    .plat-modal-body {
      margin: 0;
    }
    .message-notify-row {
      display: flex;
      height: 60vh;
      .left {
        width: 150px;
        border-right: 1px solid var(--bmos-second-level-border-color);
        padding: 8px;
      }
      .right {
        flex: 1;
        padding-bottom: 8px;
        height: 100%;
        overflow: hidden;
        .all-read {
          padding-right: 20px;
          cursor: pointer;
          .all-read-icon {
            width: 16px;
            height: 16px;
            margin-right: 4px;
          }
        }
        .plat-tabs-top {
          height: 100%;
          overflow: hidden;
        }
        .plat-tabs-top > .plat-tabs-nav {
          margin: 0;
        }
        .plat-tabs-top > .plat-tabs-content-holder {
          height: calc(100% - 40px);
          overflow-y: auto;
        }
        .error {
          cursor: pointer;
        }
      }
    }
    .menu-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      align-self: stretch;
      height: 36px;
      padding: 6px 12px;
      gap: 10px;
      align-self: stretch;
      border-radius: 4px;
      cursor: pointer;
      background-color: #fff;
      .item-name {
        color: var(--bmos-third-level-text-color);
      }
      .item-number {
        color: var(--bmos-fourth-level-text-color);
      }
    }
    .active {
      background-color: var(--bmos-primary-color-background);
      .item-name {
        color: var(--bmos-primary-color);
      }
    }
  }
</style>
