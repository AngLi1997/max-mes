<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :form-props="formProps"
    wrap-class-name="modalSizeMedium"
    :submit="submit"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams } from '@bmos/components';
  import type { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { Select, message } from 'ant-design-vue';
  import { ref } from 'vue';
  import {
    postMaterialExtendUnitBindApi,
    getMaterialExtendUnitListApi,
    getExtendUnitListApi,
  } from '@/api/materialPlatform/materialInfo';

  const unitOptions = ref<any[]>([]);
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData: Recordable;
    }>(),
    {
      open: false,
      rowData: () => ({}),
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('单位配置'));

  const submit = async (formValues: Recordable) => {
    try {
      const res = await postMaterialExtendUnitBindApi({
        materialId: props.rowData.id,
        extendUnitIdList: formValues.extendUnitIdList,
      });
      message.success(t('操作成功'));
      open.value = false;
      return Promise.resolve(true);
    } catch (error) {
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 24,
    },
    schemas: [
      {
        field: 'unitName',
        component: 'Input',
        label: t('标准单位'),
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'extendUnitIdList',
        label: t('扩展单位'),
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <Select
                v-model:value={formModel['extendUnitIdList']}
                mode='multiple'
                showSearch={false}
                popupClassName='unit-config-select-popup'
                fieldNames={{
                  label: 'extendUnitName',
                  value: 'id',
                }}
                placeholder={t('请选择扩展单位')}
                options={unitOptions.value}
                allowClear>
                {{
                  option: (option: any) => {
                    return (
                      <div class='flex-between'>
                        <span>{option.extendUnitName}</span>
                        <span class='fourth-level-text abc'>{option.expression}</span>
                      </div>
                    );
                  },
                }}
              </Select>
            </>
          );
        },
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        try {
          // 获取物料绑定的扩展单位、查询标准单位下的所有扩展单位
          const [res, res1] = await Promise.all([
            getMaterialExtendUnitListApi({
              materialId: props.rowData.id,
            }),
            getExtendUnitListApi({ unitId: props.rowData.unitId }),
          ]);
          const ids = res.data?.map((item: any) => item.id) || [];
          unitOptions.value = res1.data || [];
          modalFormRef.value?.formRef?.setFormModels({
            ...props.rowData,
            extendUnitIdList: ids,
          });
        } catch (error) {}
      }
    },
  );
</script>

<style lang="less">
  .unit-config-select-popup {
    .plat-select-item-option-state {
      display: none;
    }
  }
</style>
