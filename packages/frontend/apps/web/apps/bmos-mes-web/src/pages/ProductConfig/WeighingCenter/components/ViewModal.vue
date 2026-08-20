<template>
  <BMModalForm ref="modalFormRef" v-model:open="open" :title="t('查看信息')" wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
    <div v-for="(item, i) in basicItems" :key="i" :class="`content content${i}`">
      <div>{{ item.label }}</div>
      <div v-if="item?.field === 'timeLimit'">{{ formData[item?.field] ?? '' }}h</div>
      <div v-else>{{ formData[item?.field] ?? '' }}</div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { watch } from 'vue';
  import { message } from 'ant-design-vue';
  // import { reqFactoryRoomInfo } from '@/services';

  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
    }>(),
    {
      rowId: '',
    },
  );
  const formData = ref<any>({ stationDetails: [] });
  const basicItems = reactive<any>([
    {
      label: t('所属分类'),
      field: 'moduleName',
    },
    {
      label: t('房间名称'),
      field: 'name',
    },
    {
      label: t('房间编码'),
      field: 'code',
    },
    {
      label: t('清场时限'),
      field: 'timeLimit',
    },
    {
      label: t('描述'),
      field: 'description',
    },
    {
      label: t('绑定工位'),
      field: 'stationDetails',
    },
  ]);
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          // const res = await reqFactoryRoomInfo(props.rowId);
          // formData.value = res.data;
          // formData.value.stationDetails = res.data.stationDetails.join('、');
        } catch (error: any) {
          message.error(error.message);
        }
      }
    },
    {
      immediate: true,
    },
  );
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .content {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    > div:nth-child(1) {
      width: 80px;
      text-align: right;
      margin-right: 20px;
      color: #606266;
    }
    > div:nth-child(2) {
      width: calc(100% - 100px);
      color: #242526;
    }
  }
  .content5 {
    border-top: 1px solid #e1e3e5;
    padding-top: 20px;
  }
</style>
