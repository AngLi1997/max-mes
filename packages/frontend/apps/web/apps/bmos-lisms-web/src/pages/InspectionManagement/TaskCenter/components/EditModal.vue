<template>
  <NormalModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('检验项目编辑')"
    :okButtonText="t('提交')"
    :destroyOnClose="true"
    wrapClassName="modalSizeLarge"
    :submit="submit">
    <Tabs v-model:activeKey="tabKey">
      <TabPane :key="ProjectTypeEnum.FIXED_TIEM" :tab="t('固定项目')">
        <div :style="{ height: fixedInspectItems.length > 4 ? '40vh' : 'auto' }">
          <BMTable
            ref="fixedTableRef"
            :search="false"
            :data-source="fixedInspectItems"
            :columns="columns"
            :showIndex="true"
            :pagination="false"
            row-key="code"
            :showToolbar="false"
            :scroll="{ x: 800, y: 400 }"></BMTable>
        </div>
      </TabPane>
      <TabPane :key="ProjectTypeEnum.SPECIAL_TIEM" :tab="t('特殊项目')">
        <div :style="{ height: specialInspectItems.length > 4 ? '40vh' : 'auto' }">
          <BMTable
            ref="specialTableRef"
            :search="false"
            :data-source="specialInspectItems"
            :columns="columns"
            :showIndex="true"
            :pagination="false"
            row-key="code"
            :showToolbar="false"
            :scroll="{ x: 800, y: 400 }"></BMTable>
        </div>
      </TabPane>
    </Tabs>
  </NormalModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { Recordable, BMTable, TableColumn, NormalModalForm } from '@bmos/components';
  import { Checkbox, Tabs, TabPane, message } from 'ant-design-vue';
  import { ProjectTypeEnum } from '@/types';
  import { postInspectTaskEdit } from '@/services';

  defineOptions({
    inheritAttrs: false,
  });
  const emit = defineEmits(['ok']);
  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const props = withDefaults(
    defineProps<{
      sampleNo?: string;
      fixedInspectItems: Recordable[];
      specialInspectItems: Recordable[];
    }>(),
    {
      sampleNo: '',
      fixedInspectItems: () => [],
      specialInspectItems: () => [],
    },
  );

  const tabKey = ref<ProjectTypeEnum>(ProjectTypeEnum.FIXED_TIEM);

  const modalFormRef = ref<InstanceType<typeof NormalModalForm>>();

  const columns: TableColumn[] = [
    {
      title: t('项目名称'),
      dataIndex: 'name',
      width: 170,
    },
    {
      title: t('是否检验'),
      dataIndex: 'selected',
      width: 100,
      customRender: ({ record }) => {
        return record.selected ? (
          <Checkbox v-model:checked={record.selected} disabled={tabKey.value === ProjectTypeEnum.FIXED_TIEM} />
        ) : (
          <Checkbox v-model:checked={record.newSelected} />
        );
      },
    },
  ];

  const fixedTableRef = ref<any>();
  const specialTableRef = ref<any>();
  const submit = async () => {
    try {
      const fixedInspectData = fixedTableRef.value?.getTableData() ?? props.fixedInspectItems;
      const specialInspectData = specialTableRef.value?.getTableData() ?? props.specialInspectItems;
      await postInspectTaskEdit({
        sampleNo: props.sampleNo,
        inspectItemCodes: [...fixedInspectData, ...specialInspectData]
          ?.filter((item: any) => item.selected || item.newSelected)
          .map((item: any) => item.code),
      });
      message.success(t('操作成功'));
      emit('ok');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less" scoped></style>
