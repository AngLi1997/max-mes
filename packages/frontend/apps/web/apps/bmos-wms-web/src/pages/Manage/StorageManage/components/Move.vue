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
    :signatureAction="31"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import MoveTable from './MoveTable.vue';
  import Sign from '@/components/Sign';
  import { cloneDeep, loopSelectableNotValueTree } from '@bmos/utils';
  import { LabelList } from '@/components/Sign/type';
  import { reqInventoryMove } from '@/services';
  import { StorageLevel } from '../types';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      inventoryList?: any;
      currentNodes?: Recordable[number];
      treeData?: Recordable[number];
      curSelect?: Recordable;
    }>(),
    {
      inventoryList: () => [],
      treeData: () => [],
      currentNodes: () => [],
      curSelect: () => ({}),
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

  const title = ref<string>(t('货品移库'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('移库人'),
    },
  ];

  const request = async () => {
    try {
      await reqInventoryMove({
        ...curFormModal.value,
        inventoryBatchId: props.currentNodes[0]?.id,
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
  const formProps: Ref<FormProps> = ref({
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('移库货品'),
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
        field: 'inventoryIds',
        label: t('货品类型'),
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
              validator: (rule: any, value: any) => {
                if (value.length === 0) {
                  return Promise.reject(t('未勾选货品件号'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <FormItemRest>
              <MoveTable
                selects={formModel.inventoryIds}
                inventoryList={props.inventoryList}
                currentNodes={props.currentNodes}
                onUpdate:selects={(selects: string[]) => {
                  formModel.inventoryIds = selects;
                }}
              />
            </FormItemRest>
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
        field: 'targetPositionId',
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
        modalFormRef.value?.formRef?.setFieldsValue({
          targetPositionId: props.curSelect?.id,
        });
        modalFormRef.value?.formRef?.updateSchema({
          field: 'targetPositionId',
          componentProps: {
            treeData: loopSelectableNotValueTree(
              cloneDeep(props.treeData[0].children) as Record<string, any>[],
              'level.value',
              StorageLevel.POSITION,
            ),
          },
        });
        try {
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
