<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge"
    :submit="submit"></BMModalForm>
  <SignModal
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :labelList="labelList"
    @signSuccess="signSuccess"></SignModal>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import { StorageLevel } from '../types';
  import { reqStorageMaterialInbound } from '@/services';
  import { cloneDeep } from '@bmos/utils';
  import InboundTable from './InboundTable.vue';
  import { usePositionUserList } from '../hooks';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      treeNode?: Recordable;
      treeData?: Recordable[];
    }>(),
    {
      open: false,
      treeNode: () => ({}),
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

  const title = ref<string>(t('物料入库'));

  const signOpen = ref<boolean>(false);
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();

  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('接收人'),
        action: 81,
        disabled: true,
      },
      {
        label: t('递交人'),
        action: 82,
        options: positionUserList.value,
      },
    ];
  });

  const request = async () => {
    try {
      await reqStorageMaterialInbound(curFormModal.value);
      message.success(t('入库成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      receiverId: userId0,
      senderId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({}); // 当前表单数据
  const submit = async (formModal: Recordable) => {
    try {
      const { materialPositionId, linkExplain, inboundList } = formModal;
      await getPositionUserList('120030008000016');
      signOpen.value = true;
      curFormModal.value = {
        materialPositionId,
        linkExplain,
        sendBackList: inboundList.map((r: any) => ({
          id: r.id,
          quantity: r.quantity,
        })),
      };
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const modalFormRef = ref<ModalFormInstance>();

  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 11,
    },
    schemas: [
      {
        field: 'materialInfo',
        component: 'Divider',
        label: t('入库物料'),
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
        field: 'inboundList',
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
              validator: (rule: any, value: any) => {
                if (value.length === 0) {
                  return Promise.reject(t('未添加物料件'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <InboundTable v-model:materialList={formModel.inboundList} />
              </FormItemRest>
            </>
          );
        },
      },
      {
        field: 'materialPositionId',
        component: 'TreeSelect',
        label: t('暂存货位'),
        required: true,
        colProps: {
          span: 11,
        },
        componentProps: {
          fieldNames: {
            value: 'id',
            label: 'name',
          },
          showSearch: true,
          treeNodeFilterProp: 'name',
          dropdownMatchSelectWidth: 340,
          onChange: (value: any) => {
            curPositionId.value = value;
          },
        },
      },
      {
        field: 'linkExplain',
        component: 'InputTextArea',
        label: t('来源去向'),
        colProps: {
          span: 11,
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
        modalFormRef.value?.formRef?.updateSchema([
          {
            field: 'materialPositionId',
            componentProps: {
              treeData: loopSelectableNotValueTree(
                cloneDeep(props.treeData?.[0]?.children as []) as Record<string, any>[],
                'level.value',
                StorageLevel.POSITION,
              ) as Record<string, any>[],
            },
          },
        ]);
        if (props.treeNode && props.treeNode?.id !== 'all' && props.treeNode?.level?.value === StorageLevel.POSITION) {
          modalFormRef.value?.formRef?.setFieldsValue({
            materialPositionId: props.treeNode.id,
          });
        }
        curPositionId.value = props.treeNode?.id;
      }
    },
  );
</script>
<style lang="less"></style>
