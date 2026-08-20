<!-- 详情组件 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ props.title }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <!-- <div class="header-btn"> -->
        <Button @click="back">{{ t('返回') }}</Button>
        <!-- </div> -->
      </Col>
    </Row>
    <div style="height: calc(100% - 48px); background-color: white; overflow: auto">
      <Card v-if="props.showHeader" class="header-card">
        <template #title>
          <div class="header-title">
            <div class="header-title-info">
              <BMTableTitle class="mr-4" :title="props.headerTitle" />
              <Tag
                v-if="typeof props.tagItem?.value != 'undefined'"
                :bordered="false"
                :class="`mr-4 tag-1`"
                size="small">
                {{ props.tagItem.key }}
              </Tag>
              <span class="header-title-info-code">{{ props.headerExtra }}</span>
            </div>
          </div>
        </template>
        <slot name="header"></slot>
        <Steps v-if="props.showStep" :current="1" :items="props.stepItems" />
      </Card>
      <Card v-for="(item, _) in props.cardItems" :key="_" :title="item.title">
        <component :is="item.slot" v-if="item.slot"></component>
        <span v-else>{{ item.content }}</span>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { viewProps } from './props';
  import { t } from '@bmos/i18n';
  import { useRouter } from 'vue-router';
  import { BMTableTitle } from '@bmos/components';
  import Card from '@/components/Card/index.vue';

  const props = defineProps(viewProps);

  const router = useRouter();
  const comRouter = computed(() => {
    return t(router.currentRoute.value.meta.id as string);
  });

  const emits = defineEmits(['back']);

  const back = () => {
    emits('back');
  };
</script>

<style lang="less" scoped>
  .mr-4 {
    margin-right: 8px;
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
    justify-content: flex-start;
    align-items: center;
    background-color: #fff;
    flex-grow: 0;
    width: 100% !important;

    padding-bottom: 12px;
    margin-bottom: var(--bmos-margin-small);

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
  .header-card {
    position: sticky;
    top: 0;
    z-index: 1000;
    box-shadow: 0 2px 8px #f0f1f2;
  }

  .tag-1 {
    background-color: #59bf78;
    color: #ffffff;
  }
</style>
