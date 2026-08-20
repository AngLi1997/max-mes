<template>
  <Drawer
    v-model:open="open"
    class="flow-task-right-drawer-config"
    root-class-name="flow-task-right-drawer-config-root"
    :title="t('任务节点配置')"
    :footer="footer"
    destroyOnClose
    placement="right"
    @afterOpenChange="afterOpenChange">
    <TabComponent v-model:activeKey="tabActiveKey" :tabList="tabList" class="config-tab" />
    <BMForm ref="setFormRef" v-bind="setFormProps"></BMForm>
  </Drawer>
  <AllocationPerson
    v-model:open-people="openSelectReviewPeople"
    :hasCheckPeople="reviewPersonOptions"
    :isView="isView"
    @update:people="updateReviewPeople" />
  <AllocationPerson
    v-model:open-people="openSelectMakePeople"
    :hasCheckPeople="makePersonOptions"
    :isView="isView"
    @update:people="updateMakePeople" />
  <AllocationPerson
    v-model:open-people="openSelectAuditMegDTOList"
    :hasCheckPeople="auditMegDTOListOptions"
    :isView="isView"
    @update:people="updateAuditMegDTOList" />
</template>

<script lang="tsx" setup>
  import { Button, Space, message } from 'ant-design-vue';
  import { Recordable, BMForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks/useForm';
  import { TaskDrawerTabKeys } from '../../types';
  import TabComponent from '@/components/TabComponent/TabComponent.vue';
  import { TabPane } from '@/components/TabComponent/type';
  import AllocationPerson from '@/components/AllocationPerson/allocationPerson.vue';

  const emit = defineEmits(['update:open', 'updateFormValue']);
  const props = defineProps({
    open: {
      type: Boolean,
      default: false,
    },
    settingNodeId: {
      type: String,
      default: '',
    },
    settingNodeFormData: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
    isView: {
      type: Boolean,
      default: false,
    },
  });

  // computed set gte 监听open变化
  const open = computed({
    get() {
      return props.open;
    },
    set(val) {
      emit('update:open', val);
    },
  });

  const tabList: TabPane[] = [
    {
      key: TaskDrawerTabKeys.BasicInfo,
      title: t('基本信息'),
      associationList: ['name', 'remark', 'needPwdValidate'],
    },
    {
      key: TaskDrawerTabKeys.AuditConfig,
      title: t('审核配置'),
      associationList: [
        'reviewPerson',
        'reviewRole',
        'buttons',
        'makePerson',
        'completeType',
        'strategy',
        'needCommit',
        'needRemark',
      ],
    },
    // {
    //   key: TaskDrawerTabKeys.NoticeConfig,
    //   title: t('通知配置'),
    //   associationList: ['auditMegDTOList'],
    // },
  ];

  const cancelDrawer = () => {
    open.value = false;
  };

  const tabActiveKey = ref<TaskDrawerTabKeys>(TaskDrawerTabKeys.BasicInfo);

  const jumpTab = (errorFields: { name: string[] }[]) => {
    const curAssociationList = tabList.find(item => item.key === tabActiveKey.value)?.associationList;
    // 当前页面有报错的字段
    let curTabErrorFlag: boolean = false;
    errorFields.forEach(item => {
      if (curAssociationList?.includes(item.name[0])) {
        curTabErrorFlag = true;
      }
    });
    if (curTabErrorFlag) return;
    // 当前页面没有报错的字段
    const errorField = errorFields[0];
    const { name } = errorField;
    const tab = tabList.find(item => item.associationList && item.associationList.includes(name[0]));
    if (tab) {
      tabActiveKey.value = tab.key as TaskDrawerTabKeys;
    }
  };

  const ok = async () => {
    try {
      const res = await setFormRef.value?.submit();
      open.value = false;
      emit('updateFormValue', props.settingNodeId, res);
      message.success(t('设置成功'));
    } catch (error: any) {
      if (error.errorFields) {
        jumpTab(error.errorFields);
      } else {
        message.error(t('校验失败'));
      }
    }
  };

  const okBtnLoading = ref<boolean>(false);
  const footer = (
    <Space class='footer-action'>
      {/* 如果 isView 为 true, 不显示 确定按钮 */}
      {!props.isView && (
        <Button type='primary' loading={okBtnLoading.value} onClick={() => ok()}>
          {t('确定')}
        </Button>
      )}
      <Button onClick={() => cancelDrawer()}>{t('取消')}</Button>
    </Space>
  );

  const {
    setFormProps,
    setNodeFormData,
    setFormRef,
    openSelectReviewPeople,
    updateReviewPeople,
    reviewPersonOptions,

    openSelectMakePeople,
    makePersonOptions,
    updateMakePeople,

    openSelectAuditMegDTOList,
    auditMegDTOListOptions,
    updateAuditMegDTOList,
  } = useForm({
    tabActiveKey,
    props,
  });

  watch(
    () => props.open,
    async val => {
      await nextTick();
      if (val) {
        if (props.isView) {
          setFormRef.value?.setFormProps({
            disabled: true,
          });
        }
      }
    },
    {
      immediate: true,
    },
  );
  const afterOpenChange = (open: boolean) => {
    if (open) {
      okBtnLoading.value = true;
      try {
        setNodeFormData(props.settingNodeFormData);
      } catch (error: any) {
        error.message && message.error(error.message);
      } finally {
        okBtnLoading.value = false;
      }
    }
  };
</script>

<style lang="less">
  .flow-task-right-drawer-config {
    .mes-drawer-header-title {
      flex-direction: row-reverse;
      .mes-drawer-close {
        margin-right: 0;
      }
    }
    .mes-drawer-body {
      padding-top: 0;
    }
    .mes-drawer-footer {
      .footer-action {
        display: flex;
        justify-content: end;
        flex-direction: row-reverse;
      }
    }
    .clear-label {
      position: absolute;
      right: 20%;
      top: 50%;
      transform: translateY(-50%);
      cursor: pointer;
      z-index: 99;
    }
    .right-add-icon-btn {
      text-align: center;
      cursor: pointer;
      line-height: 36px;
      width: 15%;
      color: var(--bmos-primary-color);
      font-size: 20px;
    }
    .config-tab {
      height: 80px;
    }
  }
</style>
