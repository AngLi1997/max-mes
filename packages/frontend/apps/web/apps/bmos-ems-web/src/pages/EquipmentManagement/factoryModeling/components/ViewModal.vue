<template>
  <BMModalForm ref="modalFormRef" v-model:open="open" :title="t('查看工位信息')" wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
    <div v-for="(item, i) in basicItems" :key="i" :class="`content content${i}`">
      <div>{{ item.label }}</div>
      <div>{{ formData[item?.field] ?? '' }}</div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { reqEquipmentStationInfo } from '@/services';

  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
    }>(),
    {
      rowId: '',
    },
  );
  const formData = ref<any>({ equipmentDetail: [], userDetail: [] });
  const basicItems = reactive<any>([
    {
      label: t('所属模型'),
      field: 'moduleName',
    },
    {
      label: t('工位名称'),
      field: 'name',
    },
    {
      label: t('工位编码'),
      field: 'code',
    },
    {
      label: t('描述'),
      field: 'description',
    },
    {
      label: t('绑定设备'),
      field: 'equipmentDetail',
    },
    {
      label: t('绑定人员'),
      field: 'userDetail',
    },
  ]);
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          const res = await reqEquipmentStationInfo(props.rowId);
          formData.value = res.data;
          formData.value.equipmentDetail = res.data.equipmentDetail.join('、');
          formData.value.userDetail = res.data.userDetail.join('、');
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
  .content4 {
    border-top: 1px solid #e1e3e5;
    border-bottom: 1px solid #e1e3e5;
    padding: 20px 0px;
  }
</style>
