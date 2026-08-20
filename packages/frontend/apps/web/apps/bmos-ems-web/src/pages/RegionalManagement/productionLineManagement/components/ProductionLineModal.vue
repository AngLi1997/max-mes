<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeMedium"
    @okModal="ok">
    <template #DEPART="{ formModel }">
      <div class="depart-modal-tree">
        <ModalBtn :submit="() => departMentSubmit(formModel, 'deptIds')" :title="t('部门授权')">
          <DepartMent ref="departMent" :checks="formModel['deptIds']" :isAdd="true" :type="false"></DepartMent>
          <template #trigger>
            <Button :icon="departIcon(formModel, 'deptIds')" class="depart-btn">
              {{ t('选择部门') }}
            </Button>
          </template>
        </ModalBtn>
      </div>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { loopTree } from '../utils';
  import {
    reqFactoryLineSave, //新建产线
    reqFactoryLineUpdate, //编辑产线
  } from '@/services';
  import { reqLineModuleTreeList, getResourcePermissionTreeDept } from '@/services';
  import { message } from 'ant-design-vue';
  import ModalBtn from '@/components/ModalBtn/index.vue';
  import DepartMent from '@/components/DepartMent/index.vue';
  import { BMIcons } from '@bmos/icons';
  const departIcon = (model: any, field: string) => {
    const style_icon = {
      width: '15px',
      height: '15px',
      marginRight: '5px',
      marginBottom: '1px',
      verticalAlign: 'sub',
    };
    if (!model[field] || model[field]?.length === 0) {
      return h(BMIcons, {
        icon: 'Depart',
        style: style_icon,
      });
    } else {
      return h(BMIcons, {
        icon: 'Success',
        style: style_icon,
      });
    }
  };

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
  const departMent = ref();

  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const title = ref<any>('');
  const treeData = ref<any>([]);
  const formProps = computed<any>(() => {
    const initialValues = {};
    const schemas = [
      {
        field: 'moduleId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: {
          disabled: props.type === 'edit' ? true : false,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          treeData: treeData.value,
          request: async () => {
            const res: any = await reqLineModuleTreeList();
            return loopTree(res.data);
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('产线名称'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        required: true,
        label: t('产线编码'),
        componentProps: {
          disabled: props.type === 'edit' ? true : false,
        },
      },
      {
        field: 'deptIds',
        label: t('部门授权'),
        required: true,
        vShow: props.type === 'edit' ? false : true,
        slot: 'DEPART',
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: true,
              validator: () => {
                if (!formModel['deptIds'] || formModel['deptIds']?.length === 0) {
                  return Promise.reject(t('请选择部门授权'));
                }
                return Promise.resolve();
              },
              trigger: 'change',
            },
          ];
        },
      },
      {
        field: 'description',
        component: 'InputTextArea',
        label: t('描述'),
        componentProps: {
          maxLength: 200,
        },
      },
    ];
    return { initialValues, schemas, disabled: false };
  });
  const departMentSubmit = (model: any, field: string) => {
    const keys = departMent.value.getSelectKeys();
    model[field] = keys;
    if (keys?.length > 0) {
      modalFormRef.value?.formRef?.clearValidate(['deptIds']);
    }
    return Promise.resolve(true);
  };
  // 确定
  const ok = async () => {
    const data: any = await modalFormRef.value?.validate();
    try {
      props.type === 'add' ? await reqFactoryLineSave(data) : await reqFactoryLineUpdate(data);
      props.type === 'add' ? message.success(t('新增成功')) : message.success(t('编辑成功'));
      open.value = false;
      emit('updateTable');
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const openModal = () => {
    open.value = true;
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (!val) return;
      try {
        if (props.type === 'add') {
          title.value = t('新增产线');
          modalFormRef.value?.formRef?.setFormModels({
            moduleId: props.formData.id && props.formData.id !== 'all' ? props.formData.id : undefined,
          });
        } else {
          title.value = t('编辑产线');
          const res = await getResourcePermissionTreeDept({ resourceId: props.formData.id });
          modalFormRef.value?.formRef?.setFormModels({
            ...props.formData,
            deptIds: res.data,
          });
        }
      } catch (error) {}
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
