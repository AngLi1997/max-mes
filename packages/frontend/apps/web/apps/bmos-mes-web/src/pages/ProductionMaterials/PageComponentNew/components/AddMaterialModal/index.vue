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
  import type { FormProps, TableInstance } from '@bmos/components';
  import { BMModalForm } from '@bmos/components';
  import { useAddModalForm } from './hooks/useAddModalForm';
  import { Button, message } from 'ant-design-vue';
  import { toRefs, Ref, watch } from 'vue';
  import { postProductMaterialSaveApi, updateProductMaterialApi } from '@/services';
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
  const addModalFormRef = ref(null);
  const addModalFormTitle = ref<string>('');
  const addTitles = [t('新增原辅包'), t('新增中间品'), t('新增产品')];
  const editTitles = [t('编辑原辅包'), t('编辑中间品'), t('编辑产品')];
  const viewTitles = [t('查看原辅包'), t('查看中间品'), t('查看产品')];
  const modalFormProps = ref<FormProps>({ schemas: [] });
  const getMaterialPrincipalListFn = ref<Function>(() => {});
  const okAddModal = () => {
    addModalFormRef.value.validate().then(async data => {
      try {
        const res = await (data.id ? updateProductMaterialApi : postProductMaterialSaveApi)({
          categoryCode: props.categoryCode,
          ...data,
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
        getMaterialPrincipalListFn.value(props.initialValues.value.materialCategoryId);
        modalFormProps.value.disabled = !props.isEdit.value;
      } catch (error) {
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
      if (props.initialValues.value.materialCategoryId) {
        // 获取所属产品列表
        getMaterialPrincipalListFn.value(props.initialValues.value.materialCategoryId);
      }
    }
  };
  watch(open, async (newV, oldV) => {
    if (newV) {
      const data = useAddModalForm({
        categoryTreeData,
        initialValues: props.initialValues,
        categoryType: props.categoryType,
      });
      modalFormProps.value = data.addModalFormProps;
      getMaterialPrincipalListFn.value = data.getMaterialPrincipalListFn;
      initDefaultData();
    }
  });
</script>

<style lang="less">
  .mes-input-number-disabled + .mes-input-number-group-addon {
    color: var(--bmos-fifth-level-text-color);
  }
</style>
