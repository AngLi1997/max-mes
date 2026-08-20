<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok"></BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import {
    getMesUnitListApi,
    getMesUnitExtendListApi,
    reqTareWeighConfigCreate, //新增皮重
    reqTareWeighConfigEdit, //编辑皮重
  } from '@/services';
  import { message, Cascader } from 'ant-design-vue';
  import { isEmpty } from '@bmos/utils';

  const props = withDefaults(
    defineProps<{
      rowId?: string;
      type?: string;
      formData?: any;
    }>(),
    {
      rowId: '',
      type: '',
      formData: {},
    },
  );
  const emit = defineEmits(['updateTable']);
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const title = ref<any>('');
  const unitList = ref<any>([]); //新增时候的单位
  const formProps = computed<any>(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'tareWeigh',
        component: 'Input',
        label: t('重量'),
        dynamicRules: () => {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (isEmpty(value)) {
                  return Promise.reject(t('请输入重量'));
                }
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
                }
                // 如果值 整数部分最多为10位，小数位数最多为9位
                const reg = /^\d{1,10}(\.\d{1,9})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        componentProps: {
          disabled: props.type === 'look' ? true : false,
        },
      },
      {
        field: 'unitIds',
        label: t('单位'),
        component: ({ formModel }: any) => {
          return (
            <Cascader
              options={unitList.value}
              fieldNames={{ label: 'unitName', value: 'unitId' }}
              disabled={props.type === 'look' ? true : false}
              loadData={loadData}
              placeholder={t('请选择单位')}
              onChange={(value: any) => {
                if (!value) {
                  formModel.unit = '';
                  formModel.unitId = '';
                  return;
                }
                formModel.unitId = value[1];
              }}></Cascader>
          );
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (rule: any, value: any) => {
                if (!value) {
                  return Promise.reject(t('请选择单位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'describeInfo',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          disabled: props.type === 'look' ? true : false,
          maxLength: 200,
        },
      },
    ];
    return { initialValues, schemas, disabled: false };
  });
  // 确定
  const ok = async () => {
    if (props.type === 'look') return (open.value = false);
    const data: any = await modalFormRef.value?.validate();
    try {
      props.type === 'add' ? await reqTareWeighConfigCreate(data) : await reqTareWeighConfigEdit(data);
      props.type === 'add' ? message.success(t('新增成功')) : message.success(t('编辑成功'));
      open.value = false;
      emit('updateTable');
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 懒加载查扩展单位
  const loadData = (selectedOptions: any) => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    // // load options lazily
    getMesUnitExtendListApi(targetOption.unitId).then((res: any) => {
      targetOption.loading = false;
      (res.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.unitName = `${item.extendUnitName}(${item.expression})`;
        item.name = item.extendUnitName;
        item.parentId = targetOption.unitId;
      });
      targetOption.children = [
        {
          unitId: targetOption.unitId,
          unitName: `${targetOption.unitName}(${t('标准单位')})`,
          name: targetOption.unitName,
          isUnit: true,
        },
        ...res.data,
      ];
      unitList.value = [...unitList.value];
    });
  };
  const openModal = () => {
    open.value = true;
  };
  // 编辑或查看时候获取单位
  const getUnitList = async () => {
    try {
      const res = await getMesUnitListApi();
      unitList.value = res.data || [];
      const res2 = await getMesUnitExtendListApi(props.formData.basicUnitId);
      unitList.value?.forEach((item: any) => {
        if (item.unitId === props.formData.basicUnitId) {
          item.isLeaf = false;
          res2.data.forEach((item2: any) => {
            (item2.unitId = item2.id),
              (item2.unitName = `${item2.extendUnitName}(${item2.expression})`),
              (item2.name = item2.extendUnitName),
              (item2.parentId = props.formData.basicUnitId);
          });
          item.children = [
            {
              unitId: props.formData.basicUnitId,
              unitName: `${props.formData.basicUnit}(${t('标准单位')})`,
              name: props.formData.basicUnit,
              isUnit: true,
            },
            ...res2.data,
          ];
        } else {
          item.isLeaf = false;
          item.children = [];
        }
        modalFormRef.value?.formRef?.setFormModels({
          id: props.formData.id,
          tareWeigh: props.formData.tareWeigh,
          describeInfo: props.formData?.describeInfo,
          unitId: props.formData.unitId,
          unitIds: [props.formData.basicUnitId, props.formData.unitId],
        });
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) return;
      switch (props.type) {
        case 'add':
          title.value = t('新增皮重');
          modalFormRef.value?.formRef?.setFormModels({});
          try {
            const res = await getMesUnitListApi();
            unitList.value = res.data || [];
            unitList.value?.forEach((item: any) => {
              item.isLeaf = false;
              item.children = [];
            });
          } catch (error: any) {
            message.error(error.message);
          }
          break;
        case 'edit':
          title.value = t('编辑皮重');
          getUnitList();
          break;
        case 'look':
          title.value = t('查看皮重');
          getUnitList();
          break;
        default:
          break;
      }
    },
    {
      immediate: true,
    },
  );
  defineExpose({
    openModal,
  });
</script>

<style lang="less" scoped></style>
