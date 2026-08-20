<template>
  <BMModalForm ref="modalFormRef" v-model:open="open" :title="t('查看用户')" wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
    <div v-for="(item, i) in basicItemsData" :key="i" :class="`content content-${item.field}`">
      <div>{{ item.label }}</div>
      <div v-if="item.field === 'gender'">
        {{ formData[item?.field] == 0 ? t('男') : t('女') }}
      </div>
      <div v-if="showList(item.field, '1')">
        <div v-for="(item2, i2) in formData[item?.field]" :key="i2">{{ item2 || '-' }}</div>
      </div>
      <div v-if="showList(item.field, '2')">
        {{ formData[item?.field] ?? '-' }}
      </div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { reqUserIdGetUser } from '@/api';
  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowData: any;
    }>(),
    {
      rowData: () => {},
    },
  );
  const formData = ref<any>({});
  const basicItems = ref<any>([
    {
      label: t('用户名称'),
      field: 'userName',
    },
    {
      label: t('用户账号'),
      field: 'loginName',
    },
    {
      label: t('性别'),
      field: 'gender',
    },
    {
      label: t('手机号'),
      field: 'phone',
    },
    {
      label: t('用户邮箱'),
      field: 'email',
    },
    {
      label: t('备注'),
      field: 'remark',
    },
    {
      label: t('解锁时间'),
      field: 'unLockTime',
    },
    {
      label: t('部门'),
      field: 'deptNameList',
    },
    {
      label: t('角色'),
      field: 'roleNameList',
    },
    {
      label: t('工位'),
      field: 'stationNameList',
    },
  ]);
  const basicItemsData = ref<any>([]);
  const showList = (data: any, type: string) => {
    if (type === '1') {
      return data === 'roleNameList' || data === 'deptNameList' || data === 'stationNameList';
    }
    if (type === '2') {
      return data !== 'gender' && data !== 'roleNameList' && data !== 'deptNameList' && data !== 'stationNameList';
    }
  };
  const ensureArray = (value: any) => {
    return Array.isArray(value) && value?.length > 0 ? value : ['-'];
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          const { data } = await reqUserIdGetUser(props.rowData?.userId);
          formData.value = props.rowData;
          formData.value.roleNameList = ensureArray(data?.roleNameList);
          formData.value.deptNameList = ensureArray(data?.deptNameList);
          formData.value.stationNameList = ensureArray(data?.stationNameList);
          formData.value.unLockTime = data?.unLockTime;
          if (!data.unLockTime) {
            basicItemsData.value = basicItems.value.filter((item: any) => item.field !== 'unLockTime'); //未锁定则不展示解锁时间
          } else {
            basicItemsData.value = basicItems.value;
          }
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
  .content-unLockTime {
    > div:nth-child(1) {
      color: #ff5633;
    }
    > div:nth-child(2) {
      color: #ff5633;
    }
  }
</style>
