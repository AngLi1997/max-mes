<template>
  <div :class="`lims-card ${cardStyleMap[props.type]}`">
    <!-- <div class="lims-card-header">
      {{ props.title }}
    </div> -->
    <BMTableTitle v-if="props.title" :title="props.title" />
    <div class="lims-card-content">
      <slot></slot>
    </div>
    <Divider v-if="props.type === 'item'"></Divider>
  </div>
</template>

<script setup lang="ts">
import { BMTableTitle } from '@bmos/components';
import { onMounted, ref } from 'vue';
import {
  Divider
} from 'ant-design-vue';

const props = defineProps({
  title: {
    type: String,
    default: '',
  },
  type: {
    type: String,
    default: 'card', // 默认为card，可选值为card，item
  }
});

const cardStyleMap = {
  card: 'my-mb-small',
  item: '',
};


</script>

<style lang="less" scoped>
.my-mb-small {
  margin-bottom: var(--bmos-margin-small);
}

.lims-card {
  background-color: #fff;
  width: 100% !important;
  padding: 16px;
  // margin-bottom: var(--bmos-margin-small);
  &-header {
    width: 100%;
    color: #18191A;
    font-weight: 600;
    font-size: 16px;
    line-height: 1.5;
    margin-bottom: 16px;
  }
}

.lims-card-content {
    height: calc(100% - 40px);
}

</style>