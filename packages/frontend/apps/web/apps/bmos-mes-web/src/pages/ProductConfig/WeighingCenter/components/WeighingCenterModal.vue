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
    reqWeighCentreCreate, //新建
    reqWeighCentreEdit, //编辑
  } from '@/services';
  import { reqWeighCentreCategoryTree, getResourcePermissionTreeDept } from '@/services';
  import { message } from 'ant-design-vue';
  import { Button } from 'ant-design-vue';
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
        field: 'categoryId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: {
          disabled: props.type === 'add' ? false : true,
          fieldNames: {
            label: 'showName',
            value: 'id',
          },
          treeData: treeData.value,
          request: async () => {
            const res: any = await reqWeighCentreCategoryTree();
            return loopTree(res.data);
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('称量中心'),
        required: true,
      },
      {
        field: 'code',
        component: 'Input',
        required: true,
        label: t('编码'),
        // componentProps: {
        //   disabled: props.type === 'edit' ? true : false,
        // },
      },
      {
        field: 'deptIds',
        label: t('部门授权'),
        required: true,
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
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
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
    return Promise.resolve(true);
  };
  // 确定
  const ok = async () => {
    if (props.type === 'look') return (open.value = false);
    const data: any = await modalFormRef.value?.validate();
    try {
      props.type === 'add' ? await reqWeighCentreCreate(data) : await reqWeighCentreEdit(data);
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
        let res: any;
        switch (props.type) {
          case 'look':
            title.value = t('查看称量中心');
            res = await getResourcePermissionTreeDept({ resourceId: props.formData.id });
            modalFormRef.value?.formRef?.setFormModels({
              ...props.formData,
              deptIds: res.data,
            });
            modalFormRef.value?.formRef?.setFormProps({
              disabled: true,
            });
            break;
          case 'add':
            title.value = t('新增称量中心');
            modalFormRef.value?.formRef?.setFormModels({
              categoryId: props.formData.id && props.formData.id !== 'all' ? props.formData.id : undefined,
            });

            break;
          case 'edit':
            title.value = t('编辑称量中心');
            res = await getResourcePermissionTreeDept({ resourceId: props.formData.id });
            modalFormRef.value?.formRef?.setFormModels({
              ...props.formData,
              deptIds: res.data,
            });
            break;
          default:
            break;
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
