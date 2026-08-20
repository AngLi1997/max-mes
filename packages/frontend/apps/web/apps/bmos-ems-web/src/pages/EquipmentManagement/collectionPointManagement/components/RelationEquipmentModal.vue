<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('关联设备数据')"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm } from '@bmos/components';
  import { ref, computed, watch, nextTick } from 'vue';
  import { t } from '@bmos/i18n';
  import { message } from 'ant-design-vue';
  import { getQueryListDictDown, reqAcquisitionPointEquipmentData } from '@/services'; //获取设备数据下拉接口

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTableAndSelectedRows'): void;
  }>();
  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    selectedRows: {
      type: Array,
      default: () => [],
    },
  });
  const modalFormRef = ref<any>();
  const formProps = computed<any>(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'equipmentTagDataCode',
        component: 'Select',
        label: t('设备数据'),
        required: true,
        componentProps: {
          fieldNames: {
            label: 'showName',
            value: 'value',
          },
          request: async () => {
            // 获取设备数据
            try {
              const { data } = await getQueryListDictDown({ dictId: '160010002002' });
              return data?.map((item: any) => {
                return {
                  ...item,
                  showName: item.label + '-' + item.value,
                };
              });
            } catch (error: any) {
              message.error(error.message);
            }
          },
          onChange: () => {},
        },
      },
    ];
    return { initialValues, schemas, disabled: false };
  });
  // 确定
  const ok = async () => {
    const res = await modalFormRef.value?.validate();
    try {
      const data = {
        acquisitionPointList: props.selectedRows.map((item: any) => item.id),
        equipmentTagDataCode: res.equipmentTagDataCode,
      };
      await reqAcquisitionPointEquipmentData(data);
      message.success(t('操作成功'));
      open.value = false;
      emit('updateTableAndSelectedRows');
    } catch (error: any) {
      if (error.code == 8112045) {
        const str0 = error.message.match(/(\S*);/)[1];
        const str = error.message.match(/【(\S*)】/)[1];
        const strArr = str.split('；');
        message.error({
          content: h('div', { style: 'display:inline-table;text-align:left;vertical-align: top;' }, [
            ...[str0, <br />],
            strArr.map((item: any, index: any) => {
              return [item, index < strArr.length - 1 ? <br /> : null];
            }),
          ]),
        });
        return;
      }
      error.message && message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) return;
      if (props.selectedRows.length === 1) {
        modalFormRef.value?.formRef?.setFieldsValue({
          equipmentTagDataCode: props.selectedRows[0]?.equipmentTagDataCode, //只选中一个时,需回显
        });
      }
      try {
      } catch (error) {}
    },
    {
      immediate: true,
    },
  );
</script>

<style lang="less" scoped></style>
