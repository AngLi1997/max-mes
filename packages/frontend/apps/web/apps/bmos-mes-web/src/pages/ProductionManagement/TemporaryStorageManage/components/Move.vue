<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :signatureAction="16"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import MoveTable from './MoveTable.vue';
  import Sign from '@/components/Sign';
  import { cloneDeep } from '@bmos/utils';
  import { LabelList } from '@/components/Sign/type';
  import { reqStorageMaterialMove } from '@/services';
  import { StorageLevel } from '../types';
  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      modalTableData?: Recordable[];
      currentNodes?: Recordable;
      treeData?: Recordable[number];
    }>(),
    {
      modalTableData: () => [],
      currentNodes: () => ({}),
      treeData: () => [],
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

  const title = ref<string>(t('物料移库'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('移库人'),
    },
  ];

  const request = async () => {
    try {
      await reqStorageMaterialMove({
        ...curFormModal.value,
        storageMaterialBatchId: props.currentNodes[0]?.id,
      });
      emit('updateTable');
      message.success(t('移库成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { receiverId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      moverId: receiverId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      signOpen.value = true;
      curFormModal.value = formModal;
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();
  const formProps = reactive<FormProps>({
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('移库物料'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'storageMaterialIdList',
        label: t('物料类型'),
        noLabel: true,
        required: true,
        colProps: {
          span: 24,
        },
        defaultValue: [],
        dynamicRules: () => {
          return [
            {
              required: true,
              validator: (_rule: any, value: any) => {
                if (value.length === 0) {
                  return Promise.reject(t('未勾选物料件号'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel, formInstance }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <MoveTable
                  selects={formModel.storageMaterialIdList}
                  modalTableData={props.modalTableData}
                  currentNodes={props.currentNodes}
                  onUpdate:selects={(selects: string[]) => {
                    formModel.storageMaterialIdList = selects;
                    formInstance.validateFields(['storageMaterialIdList']);
                  }}
                />
              </FormItemRest>
            </>
          );
        },
      },
      {
        field: 'field6',
        component: 'Divider',
        label: t('移库信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'targetMaterialPositionId',
        component: 'TreeSelect',
        label: t('移入货位'),
        required: true,
        colProps: {
          style: {
            marginRight: 'auto',
          },
          span: 12,
        },
        formItemProps: {
          style: {
            marginLeft: '36px',
          },
        },
        componentProps: {
          treeData: [],
          fieldNames: {
            children: 'children',
            label: 'name',
            value: 'id',
          },
        },
      },
      {
        field: 'linkExplain',
        component: 'InputTextArea',
        label: t('来源去向'),
        colProps: {
          span: 22,
        },
        required: true,
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        modalFormRef.value?.formRef?.updateSchema({
          field: 'targetMaterialPositionId',
          componentProps: {
            treeData: loopSelectableNotValueTree(
              cloneDeep(props.treeData?.[0]?.children as []) as Record<string, any>[],
              'level.value',
              StorageLevel.POSITION,
            ) as Record<string, any>[],
          },
        });
        try {
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
