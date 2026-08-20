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
  import OutMaterialsTable from './OutMaterialsTable.vue';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import { usePositionUserList } from '../hooks';
  import { reqStorageMaterialOutbound } from '@/services';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      modalTableData?: Recordable[];
      currentNodes?: Recordable;
    }>(),
    {
      modalTableData: () => [],
      currentNodes: () => ({}),
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

  const title = ref<string>(t('物料出库'));

  const signOpen = ref<boolean>(false);
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();
  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('出库人'),
        action: 53,
        disabled: true,
      },
      {
        label: t('领用人'),
        action: 54,
        options: positionUserList.value,
      },
    ];
  });
  const request = async () => {
    try {
      await reqStorageMaterialOutbound({
        ...curFormModal.value,
        storageMaterialBatchId: props.currentNodes[0]?.id,
      });
      emit('updateTable');
      message.success(t('出库成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      senderId: userId0,
      receiverId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPositionUserList('120030008000017');
      signOpen.value = true;
      const { linkExplain, outboundList } = formModal;
      curFormModal.value = {
        linkExplain,
        outboundList: outboundList.map((item: any) => {
          return {
            id: item.id,
            quantity: item.quantity,
          };
        }),
      };
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
        label: t('出库物料'),
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
        field: 'outboundList',
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
                <OutMaterialsTable
                  selects={formModel.outboundList.map((item: any) => item.id)}
                  modalTableData={props.modalTableData}
                  currentNodes={props.currentNodes}
                  onUpdate:selects={(selects: string[]) => {
                    formModel.outboundList = selects;
                    formInstance.validateFields(['outboundList']);
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
        label: t('出库信息'),
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
        curPositionId.value = props.modalTableData?.[0]?.materialPositionId;
      }
    },
  );
</script>
<style lang="less"></style>
