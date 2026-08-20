<template>
  <BMModalForm
    ref="addModalFormRef"
    v-model:open="open"
    :title="addModalFormTitle"
    :formProps="modalFormProps"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <template v-if="!modalFormProps.disabled">
        <Button @click="open = false">{{ t('取消') }}</Button>
        <Button type="primary" @click="okAddModal">
          {{ t('确定') }}
        </Button>
      </template>
      <Button v-else type="primary" @click="open = false">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import type { TableInstance } from '@bmos/components';
  import { BMModalForm } from '@bmos/components';
  import { useAddModalForm } from './hooks/useAddModalForm';
  import { Button, message } from 'ant-design-vue';
  import { toRefs, Ref, watch } from 'vue';
  import { postCargoSaveApi, updateCargoApi } from '@/services';
  const props = defineProps<{
    isEdit: Ref<boolean>;
    id: Ref<string>;
    open: boolean;
    categoryTreeData: [];
    categoryType: number;
    initialValues: Ref<any>;
    tableInstance: TableInstance | null;
  }>();

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const open = computed({
    get() {
      return props.open;
    },
    set(value: boolean) {
      emit('update:open', value);
    },
  });

  const { categoryTreeData } = toRefs(props);
  const addModalFormRef = ref<any>(null);
  const addModalFormTitle = ref<string>('');
  const addTitles = [t('新增货品'), t('新增中间品'), t('新增产品')];
  const editTitles = [t('编辑货品'), t('编辑中间品'), t('编辑产品')];
  const viewTitles = [t('查看货品'), t('查看中间品'), t('查看产品')];
  const modalFormProps = ref<any>({ schemas: [] });
  const getMaterialPrincipalListFn = ref<Function>(() => {});
  const okAddModal = () => {
    addModalFormRef.value?.validate().then(async (data: any) => {
      try {
        await (data.id ? updateCargoApi : postCargoSaveApi)({
          ...data,
          unitId: data?.unitExtendId || data.unitId,
        });
        open.value = false;
        props.tableInstance?.fetchData(0);
        message.success(t(data.id ? t('修改成功') : t('新增成功')));
      } catch (error: any) {
        message.error(error.message);
      }
    });
  };
  // 初始化弹框数据
  const initDefaultData = async () => {
    if (props.id.value) {
      //  编辑或查看
      try {
        // 获取所属产品列表
        getMaterialPrincipalListFn.value(props.initialValues.value.cargoCategoryId);
        modalFormProps.value.disabled = !props.isEdit.value;
      } catch (error: any) {
        message.error(error.message);
      }
      if (props.isEdit.value) {
        addModalFormTitle.value = editTitles[props.categoryType];
      } else {
        addModalFormTitle.value = viewTitles[props.categoryType];
      }
    } else {
      // 新增
      modalFormProps.value.disabled = false;
      addModalFormTitle.value = addTitles[props.categoryType];
      if (props.initialValues.value.cargoCategoryId) {
        // 获取所属产品列表
        getMaterialPrincipalListFn.value(props.initialValues.value.cargoCategoryId);
      }
    }
  };
  watch(open, async newV => {
    if (newV) {
      const data = useAddModalForm({
        categoryTreeData,
        initialValues: props.initialValues,
        categoryType: props.categoryType,
        addModalFormRef,
      });
      modalFormProps.value = data.addModalFormProps;
      getMaterialPrincipalListFn.value = data.getMaterialPrincipalListFn;
      initDefaultData();
    }
  });
</script>

<style lang="less">
  .wms-input-number-disabled + .wms-input-number-group-addon {
    color: var(--bmos-fifth-level-text-color);
  }
</style>
