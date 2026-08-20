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
    :userList="permissionCodeUserList"
    :signatureAction="30"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, RenderCallbackParams, Recordable } from '@bmos/components';
  import { FormItemRest, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import OutMaterialsTable from './OutMaterialsTable.vue';
  import Sign from '@/components/Sign';
  import { LabelList } from '@/components/Sign/type';
  import { usePermissionCodeUserList } from '@/hooks';
  import { reqInventoryOutbound } from '@/services';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      currentNodes?: Recordable[number];
      treeId?: any;
      inventoryList: any;
    }>(),
    {
      currentNodes: () => [],
      treeId: () => ({}),
      inventoryList: () => [],
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

  const title = ref<string>(t('货品出库'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('出库人'),
    },
    {
      label: t('领用人'),
    },
  ];
  const { getPermissionCodeUserList, permissionCodeUserList } = usePermissionCodeUserList();
  const request = async () => {
    try {
      await reqInventoryOutbound({
        ...curFormModal.value,
        inventoryBatchId: props.currentNodes[0]?.id,
      });
      emit('updateTable');
      message.success(t('出库成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { submitterId, receiverId } = data;
    curFormModal.value = {
      ...curFormModal.value,
      senderId: receiverId,
      receiverId: submitterId,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      await getPermissionCodeUserList('150020001000008');
      signOpen.value = true;
      const { linkExplain, inventoryIds } = formModal;
      curFormModal.value = {
        linkExplain,
        inventories: inventoryIds.map((item: any) => {
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

  const errorInput = ref<Recordable[]>([]);

  const modalFormRef = ref<ModalFormInstance>();
  const formProps: Ref<FormProps> = ref({
    schemas: [
      {
        field: 'field6',
        component: 'Divider',
        label: t('出库货品'),
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
                const reg = /^\d{1,10}(\.\d{1,9})?$/;
                const errorItem = value.filter((item: any) => {
                  console.log(item.quantity, !reg.test(Number(item.quantity)));
                  if (isNaN(Number(item.quantity)) || Number(item.quantity) <= 0 || !reg.test(Number(item.quantity))) {
                    return true;
                  }
                  return false;
                });

                if (errorItem.length > 0) {
                  errorInput.value = errorItem;
                  return Promise.reject(
                    `${t('货品件')}${errorItem.map((item: any) => item.inventoryNo).join(',')}${t(
                      '出库数量必须为整数部分最多为10位，小数位数最多为9位的整数',
                    )}`,
                  );
                } else {
                  errorInput.value = [];
                }
                return Promise.resolve();
              },
            },
          ];
        },
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <FormItemRest>
              <OutMaterialsTable
                selects={formModel.inventoryIds}
                inventoryList={props.inventoryList}
                currentNodes={props.currentNodes}
                errorInput={errorInput.value}
                onUpdate:selects={(selects: any[]) => {
                  formModel.inventoryIds = selects || [];
                }}
              />
            </FormItemRest>
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
        try {
          // 获取详细信息
          // 设置curPositionId 的值
        } catch (error) {}
      }
    },
  );
</script>
<style lang="less"></style>
