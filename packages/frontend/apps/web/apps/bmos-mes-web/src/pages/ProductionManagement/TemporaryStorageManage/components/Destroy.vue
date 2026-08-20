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
  import SendBackTable from './SendBackTable.vue';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import { reqStorageMaterialDestroyAndConsumeMobile } from '@/services';
  import { usePositionUserList } from '../hooks';

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

  const title = ref<string>(t('物料销毁'));

  const signOpen = ref<boolean>(false);
  const { getPositionUserList, curPositionId, positionUserList } = usePositionUserList();

  const labelList = computed<LabelList[]>(() => {
    return [
      {
        label: t('销毁人'),
        action: 122,
        disabled: true,
      },
      {
        label: t('复核人'),
        action: 123,
        options: positionUserList.value,
      },
    ];
  });

  const request = async () => {
    try {
      await reqStorageMaterialDestroyAndConsumeMobile({
        ...curFormModal.value,
      });
      emit('updateTable');
      message.success(t('销毁成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      operatorId: userId0,
      reCheckerId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPositionUserList('120030008000023');
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
        label: t('销毁物料'),
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
                <SendBackTable
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
        label: t('销毁信息'),
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
