<template>
  <div class="bmos-approval-btn">
    <Space :size="16">
      <template v-for="item in btnList" :key="item.name">
        <Button v-if="item.name" :type="getType(item.type)" @click="() => handleClickBtn(item.type)">
          {{ item.name }}
        </Button>
      </template>
    </Space>
  </div>
  <ApprovalModal
    v-model:open="open"
    :curType="curType"
    :needPwdValidate="needPwdValidate"
    :needCommit="needCommit"
    :needRemark="needRemark"
    :needCopyTo="needCopyTo"
    :nodeId="nodeId"
    :deploymentId="deploymentId"
    :taskId="taskId"
    :executionId="executionId"
    :processInstanceId="processInstanceId"
    :mesAuditCompleteRequest="mesAuditCompleteRequest"
    :mesAuditCompleteNotApproveRequest="mesAuditCompleteNotApproveRequest"
    @action="action" />
</template>
<script lang="tsx" setup>
  import { Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import ApprovalModal from './components/ApprovalModal.vue';
  import { DataRequestFn } from '../../types';

  const emit = defineEmits<{
    (e: 'action', type: string): void;
  }>();

  const props = defineProps({
    settings: {
      required: true,
      type: Object as PropType<Recordable>,
      default: () => {},
    },
    taskId: {
      required: true,
      type: String,
      default: '',
    },
    processInstanceId: {
      required: true,
      type: String,
      default: '',
    },
    mesAuditCompleteRequest: {
      // 审核通过
      type: Function as PropType<DataRequestFn>,
      default: undefined,
    },
    mesAuditCompleteNotApproveRequest: {
      // 审核不通过
      type: Function as PropType<DataRequestFn>,
      default: undefined,
    },
    mesAuditBackToPrevRequest: {
      // 回退
      type: Function as PropType<DataRequestFn>,
      default: undefined,
    },
    nodeId: {
      required: true,
      type: String,
      default: '',
    },
    deploymentId: {
      required: true,
      type: String,
      default: '',
    },
    executionId: {
      required: true,
      type: String,
      default: '',
    },
  });

  const btnMap = new Map([
    ['pass', t('审核通过')],
    ['reject', t('审核不通过')],
    ['deliverTo', t('转交')],
    ['returnTo', t('回退')],
    // ['copyTo', t('抄送')],
  ]);

  const btnList = ref<
    {
      type: string;
      name: string;
    }[]
  >([]);

  const open = ref<boolean>(false);
  const curType = ref<string>('');
  // 是否需要密码验证
  const needPwdValidate = computed(() => {
    return props.settings?.needPwdValidate || false;
  });
  // 审核意见是否必填
  const needCommit = computed(() => {
    return props.settings?.needCommit || false;
  });
  // 备注是否必填
  const needRemark = computed(() => {
    return props.settings?.needRemark || false;
  });

  // 是都显示抄送人
  const needCopyTo = computed(() => {
    return props.settings?.buttons?.includes('copyTo') || false;
  });

  const handleClickBtn = (type: string) => {
    open.value = true;
    curType.value = type;
  };

  const getType = (type: string) => {
    switch (type) {
      case 'pass':
        return 'primary';
      default:
        return 'default';
    }
  };

  const action = (type: string) => {
    emit('action', type);
  };

  onMounted(async () => {
    await nextTick();
    try {
      if (props.settings?.buttons) {
        btnList.value = [];
        const buttons = props.settings?.buttons;
        buttons.forEach((item: string) => {
          btnList.value.push({
            type: item,
            name: btnMap.get(item) || '',
          });
        });
      }
    } catch (error) {}
  });
</script>
