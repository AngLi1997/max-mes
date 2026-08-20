<template>
  <BMModalForm
    ref="setFormRef"
    v-model:open="open"
    :title="t('日志')"
    :formProps="setFormProps"
    :okButtonProps="{disabled: true}"
    wrapClassName="modalSizeMedium">
    <Timeline>
      <TimelineItem v-for="(item, index) in logList" :key="index">
        <div style="width: 100%;">
          <h3>{{ item.operateTime }}</h3>
          <div class="content">
            <div class="items">
              <span>
                {{ item.result }}
              </span>
              <span>
                {{ item.operatorName }}
              </span>
            </div>
            <div style="margin-top: 8px" v-if="item.reason">
              {{ `${t('原因')}: ${item.reason}` }}
            </div>
          </div>
        </div>
      </TimelineItem>
    </Timeline>
  </BMModalForm>
</template>

<script setup lang="ts">
import {
  BMModalForm,
  ModalFormInstance
} from '@bmos/components';
import {
  Timeline,
  TimelineItem
} from 'ant-design-vue';
import { t } from '@bmos/i18n';
import { ref } from 'vue';

const setFormRef = ref<ModalFormInstance>();
const setFormProps = ref({
  labelCol: {
    span: 6,
  },
})
const open = ref<boolean>(false);

const logList = ref<any[]>([]);

const openModel = (data: any) => {
  logList.value = data;
  open.value = true;
}

defineExpose({
  openModel
})
</script>

<style lang="less" scoped>
.content {
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: flex-start;
  padding: 8px 16px;
  background: #F2F3F4;
  .items {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    .span {
      font-size: 12px;
    }
  }
  
}
</style>