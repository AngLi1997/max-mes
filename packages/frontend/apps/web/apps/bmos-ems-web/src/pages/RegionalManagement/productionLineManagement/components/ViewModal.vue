<template>
  <BMModalForm ref="modalFormRef" v-model:open="open" :title="t('查看产线信息')" wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
    <div v-for="(item, i) in basicItems" :key="i" :class="`content content${item.field}`">
      <div>{{ item.label }}</div>
      <div>{{ formData[item?.field] ?? '-' }}</div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { reqFactoryLineInfo } from '@/services';
  import { cloneDeep } from '@bmos/utils';

  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
    }>(),
    {
      rowId: '',
    },
  );
  const formData = ref<any>({ roomNameList: [], roomCodeList: [], stationNameList: [], stationCodeList: [] });
  const basicItems = reactive<any>([
    {
      label: t('绑定房间'),
      field: 'roomNameList',
    },
    {
      label: t('房间编码'),
      field: 'roomCodeList',
    },
    {
      label: t('绑定工位'),
      field: 'stationNameList',
    },
    {
      label: t('工位编码'),
      field: 'stationCodeList',
    },
    {
      label: t('所属分类'),
      field: 'moduleName',
    },
    {
      label: t('分类编码'),
      field: 'moduleCode',
    },
    {
      label: t('产线名称'),
      field: 'name',
    },
    {
      label: t('产线编码'),
      field: 'code',
    },
    {
      label: t('描述'),
      field: 'description',
    },
  ]);
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          const res = await reqFactoryLineInfo(props.rowId);
          formData.value = res.data;
          const temp: any = cloneDeep(res.data.roomNameList);
          const temp2: any = cloneDeep(res.data.stationNameList);
          formData.value.roomNameList = temp?.map((item: any) => item?.name)?.join('、'); //名称
          formData.value.roomCodeList = temp?.map((item: any) => item?.code)?.join('、'); //编码
          formData.value.stationNameList = temp2?.map((item: any) => item?.name)?.join('、');
          formData.value.stationCodeList = temp2?.map((item: any) => item?.code)?.join('、');
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
  .contentroomCodeList {
    border-bottom: 1px solid #e1e3e5;
    padding-bottom: 20px;
  }
  .contentstationCodeList {
    border-bottom: 1px solid #e1e3e5;
    padding-bottom: 20px;
  }
</style>
