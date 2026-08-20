<template>
  <div style="display: flex;align-items: center;">
    <div
      class="point"
      :style="
        {backgroundColor: colorMap[props.data]}
      "></div>
    <div
      :style="{color: colorMap[props.data]}"  
    >
    {{ checkStatusMap[props.data] }}
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  CHECK_STATUS,
  checkStatusMap
} from '@/utils/enum'

const props = defineProps({
  data: {
    type: String,
    default: () => {
      return {}
    }
  }
})

const colorMap = {
  [CHECK_STATUS.CONFIRM]: '#FF9A2F',
  [CHECK_STATUS.TAKE]: '#FF9A2F',
  [CHECK_STATUS.INSPECT]: '#FF9A2F',
  [CHECK_STATUS.REPORT]: '#2871FF',
  [CHECK_STATUS.AUDIT_REPORT]: '#2871FF',
  [CHECK_STATUS.SIGN]: '#2871FF',
  [CHECK_STATUS.ALREADY_SIGN]: '#59BF78',
  [CHECK_STATUS.ALREADY_TERMINATION]: '#FF5633',
}

</script>

<style lang="less" scoped>
.point {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  margin-right: 8px;
}

</style>