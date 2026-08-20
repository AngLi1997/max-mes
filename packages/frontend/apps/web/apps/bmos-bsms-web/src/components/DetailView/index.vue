<!-- 详情组件 -->
<template>
  <div style="height: 100%; display: flex; flex-direction: column; flex-grow: 1">
    <Row v-if="props.showBreadcrumb" class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item @click="back">{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item v-if="props.title">{{ props.title }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <!-- <div class="header-btn"> -->
        <Button @click="back">{{ t('返回') }}</Button>
        <!-- </div> -->
      </Col>
    </Row>
    <Card v-if="props.showHeader" ref="headerRef" class="header-card">
      <template #title>
        <div class="header-title">
          <div class="header-title-info">
            <BMTableTitle :title="props.headerTitle" />
            <Tag v-if="typeof props.tagItem?.value != 'undefined'" :bordered="false" :class="`mx-4 tag-1`" size="small">
              {{ props.tagItem.key }}
            </Tag>
            <span class="header-title-info-code">{{ props.headerExtra }}</span>
          </div>
          <slot name="extra"></slot>
        </div>
      </template>
      <slot name="header"></slot>
      <div v-if="props.showStep" style="height: 86px">
        <Steps
          class="header-step"
          :current="1"
          :items="
            props.stepItems.map(item => ({
              ...item,
              title: item.operator ? `${item.title}:${item.operator}` : item.title,
              status: stepStatusMap[item.status ?? 0],
              disabled: true,
            }))
          "></Steps>
      </div>
    </Card>
    <div
      ref="container"
      :style="{
        height: `calc(100% - ${headerHeight}px)`,
        backgroundColor: 'white',
      }">
      <div class="container">
        <Affix
          v-if="props.menuItems && props.menuItems.length > 0"
          style="z-index: 99999"
          :offset-top="0"
          :target="() => container">
          <Tabs v-model:activeKey="activeKey" tabPosition="left" @change="changeType">
            <TabPane v-for="item in props.menuItems" :key="item.key" :tab="item.label"></TabPane>
          </Tabs>
          <slot name="contentLeft" :cardRefs="cardRefs"></slot>
        </Affix>
        <div class="right-container">
          <slot name="cardBefore"></slot>
          <Card
            v-for="(item, index) in props.cardItems"
            :id="`card_${index}`"
            :ref="el => (cardRefs[index] = el)"
            :key="index"
            :title="item.title">
            <component :is="item.slot" v-if="item.slot"></component>
            <span v-else>{{ item.content }}</span>
          </Card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { viewProps } from './props';
  import { t } from '@bmos/i18n';
  import { useRouter } from 'vue-router';
  import { BMTableTitle } from '@bmos/components';
  import Card from '@/components/Card/index.vue';
  import { Affix, Tabs, TabPane } from 'ant-design-vue';

  const emits = defineEmits(['back']);

  const stepStatusMap = {
    0: 'wait',
    1: 'process',
    2: 'finish',
    3: 'error',
  };

  const props = defineProps({
    ...viewProps,
    backFn: {
      type: Function,
      default: null,
    },
  });

  const container = ref();

  const headerRef = ref();
  const headerHeight = ref(0);

  watch(
    () => [props.showBreadcrumb, props.showHeader, props.showStep],
    async () => {
      // 重新计算header高度
      await nextTick();
      headerHeight.value = headerRef.value?.$el?.clientHeight;
      if (props.showBreadcrumb) {
        headerHeight.value += 48;
      }
    },
    {
      immediate: true,
    },
  );

  const activeKey = ref<string | number>(0);

  const cardRefs = ref<any>({});

  const router = useRouter();
  const comRouter = computed(() => {
    return t(router.currentRoute.value.query.fromRouteId as string);
  });

  const changeType = (val: any) => {
    activeKey.value = val;
    // 滚动到对应的卡片
    document.getElementById(`card_${val}`)?.scrollIntoView({ behavior: 'smooth' });
    // cardRefs.value[val]?.scrollIntoView({ behavior: 'smooth' });
  };

  const back = () => {
    if (props.backFn) {
      props.backFn();
    } else {
      emits('back');
    }
  };

  // onMounted(() => {
  //   // 监听滚动事件，切换选中卡片
  //   if (props.menuItems && props.menuItems.length > 0) {
  //     console.log(document.getElementsByClassName('right-container')[0].addEventListener);
  //     document.getElementsByClassName('right-container')[0].addEventListener(
  //       'scroll',
  //       throttle(() => {
  //         const scrollTop = document.getElementsByClassName('right-container')[0].scrollTop;
  //         console.log(scrollTop);

  //         for (let i = props.menuItems.length - 1; i >= 0; i--) {
  //           const item = props.menuItems[i];
  //           const card = document.getElementById(`card_${item.key}`);
  //           // 获取card距离屏幕顶部的距离
  //           const cardTop = card?.getBoundingClientRect().top || 0;
  //           console.log(cardTop, item.key);
  //           if (cardTop > 100 && cardTop <= 134) {
  //             activeKey.value = item.key;
  //             break;
  //           }
  //         }
  //       }),
  //     );
  //   }
  // });
</script>

<style lang="less" scoped>
  .mx-4 {
    margin: 0 8px;
    height: 26px;
    line-height: 24px;
    padding-inline: 10px;
    border-radius: 13px;
  }

  .header {
    position: sticky;
    top: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    backdrop-filter: blur(6px);
    z-index: 1000;
    .crumb {
      // line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }

  .header-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #fff;
    flex-grow: 0;
    width: 100% !important;

    &-info {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      &-code {
        color: #18191a;
        font-size: 14px;
        line-height: 1;
      }
    }
  }

  .header-step {
    padding-top: 12px;
    margin-top: var(--bmos-margin-small);
  }
  .header-card {
    position: sticky;
    top: 0;
    z-index: 1000;
    // padding-bottom: 0;
    box-shadow: 0 2px 8px #f0f1f2;
  }

  .tag-1 {
    background-color: #59bf78;
    color: #ffffff;
  }

  .container {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    height: 100%;
    width: 100%;
    .right-container {
      overflow: auto;
      width: 100%;
      height: 100%;
    }
  }

  :deep(.bsms-tabs-nav) {
    margin: 0;
  }

  :deep(.ant-steps.ant-steps-horizontal:not(.ant-steps-label-vertical) .ant-steps-item-description) {
    max-width: 300px;
  }
</style>
