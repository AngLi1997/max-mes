<template>
  <BMPageComponent
    ref="tableInstance"
    :hide-right-tree="true"
    :rowKeys="['id', 'id']"
    :tableFields="[
      {
        field: {
          id: 'id',
        },
      },
      {
        field: {
          id: 'id',
        },
      },
    ]"
    :requests="[standardUnitsList, getExtendedUnitsList]"
    :columns="columns"
    :titles="[t('标准单位'), t('扩展单位')]"
    :search="[true, true]"
    :formProps="[
      {
        showAdvancedButton: false,
        actionColOptions: {
          span: 18,
        },
      },
      {},
    ]"
    :rowClick="handleClickRow">
    <template #tableHeaderToolbar0>
      <Button v-hasAuth="100040001000001" type="primary" @click="lookOrEdit('11', 'add')">
        {{ t('新增') }}
      </Button>
      <!-- 标准单位弹框 -->
      <BMModalForm
        ref="modalFormRef"
        v-model:open="open"
        :title="title"
        :formProps="formProps"
        :cancelText="t('取消')"
        :okText="t('确定')"
        wrapClassName="modalSizeMedium"
        @cancelModal="cancel"
        @okModal="ok"></BMModalForm>
    </template>
    <template #tableHeaderToolbar1="{ currentNodes }">
      <Button
        v-hasAuth="100040001000006"
        type="primary"
        :disabled="buttonDisabled(currentNodes)"
        @click="lookOrEdit2('11', 'add')">
        {{ t('新增') }}
      </Button>
      <!-- 扩展单位弹框 -->
      <BMModalForm
        ref="modalFormRef2"
        v-model:open="open2"
        :title="title2"
        :formProps="formProps2"
        :cancelText="t('取消')"
        :okText="t('确定')"
        wrapClassName="modalSizeMedium"
        @cancelModal="cancel2"
        @okModal="ok2">
        <template #selectA="{ formModel, field }">
          <div class="conversion">
            <Input v-model:value="value1" style="width: 30%" disabled :addon-after="formModel.extendUnitName" />
            <div style="width: 5%; text-align: center">=</div>
            <Input v-model:value="formModel[field]" style="width: 65%" disable :addon-after="addonAfter2" />
          </div>
        </template>
      </BMModalForm>
    </template>
  </BMPageComponent>
</template>

<script setup lang="tsx">
  import {
    standardUnitsList,
    addStandardUnit,
    editStandardUnit,
    deleteStandardUnit,
    getRoundingList,
    extendedUnitsList,
    addExtendedUnit,
    editExtendedUnit,
    deleteExtendedUnit,
    updateExtendState,
  } from '@/api/materialPlatform/unitsManagement';
  import { reactive, ref, createVNode } from 'vue';
  import { Modal, message, Button, Switch } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { t } from '@bmos/i18n';
  import { BMPageComponent, TableInstance, TableColumn, BMModalForm } from '@bmos/components';
  import type { Recordable } from '@bmos/components';
  import { usePermissionStore } from '@/stores/permission';

  const { hasPermission } = usePermissionStore();
  const tableInstance = ref<TableInstance>();
  const modalFormRef = ref<any>();
  const open = ref<boolean | undefined>(false);
  const title = ref<String>('');
  const modalFormRef2 = ref<any>();
  const open2 = ref<boolean | undefined>(false);
  const title2 = ref<String>('');
  const value1 = ref<any>('1');
  const addonAfter2 = ref<String>('g'); //换算表达式的标准单位后缀
  const accuracy = ref();
  const accuracy2 = ref();
  const standardUnitName = ref();
  const standardUnitNameId = ref();
  const standardUnitsAccuracy = ref();
  // 精度校验[0-9]的自然数
  const validatorAccuracy = async (_rule: any, value: any) => {
    if (!value && value !== 0) {
      return Promise.reject(t('请输入精度'));
    } else if (!/^[0-9]$/.test(value)) {
      return Promise.reject(t('[0,9]的自然数'));
    } else {
      return Promise.resolve();
    }
  };
  // 换算表达式校验 正数,整数部分最多10位 小数部分最多9位
  const validatorExpression = async (_rule: any, value: any) => {
    if (!value) {
      return Promise.reject(t('请输入换算表达式'));
    }
    if (value == 0) {
      return Promise.reject(t('正数,整数部分最多10位,小数位数最多为9位'));
    } else if (!/^\d{1,10}$|^\d{1,10}[.]\d{1,9}$/.test(value)) {
      return Promise.reject(t('正数,整数部分最多10位,小数位数最多为9位'));
    } else {
      return Promise.resolve();
    }
  };
  // 标准单位弹窗formProps
  const formProps = reactive<any>({
    initialValues: {},
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'unitName',
        component: 'Input',
        label: t('单位名称'),
        required: true,
      },
      {
        field: 'unitPrecision',
        component: 'InputNumber',
        label: t('精度'),
        required: true,
        rules: [{ required: true, validator: validatorAccuracy, trigger: 'blur' }],
        helpMessage: [t('精度为单位所需保留的小数位数')],
      },
      {
        field: 'roundCode',
        component: 'Select',
        label: t('修约方式'),
        required: true,
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: false,
      },
    ],
  });
  // 扩展单位弹窗
  const formProps2 = reactive<any>({
    initialValues: {},
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'unitId',
        component: 'Select',
        label: t('标准单位'),
        required: true,
        componentProps: {
          options: [],
        },
      },
      {
        field: 'extendUnitName',
        component: 'Input',
        label: t('单位名称'), //扩展单位名称
        required: true,
      },
      {
        field: 'expressionValue',
        component: 'Input',
        label: t('换算表达式'),
        required: true,
        rules: [{ required: true, validator: validatorExpression, trigger: 'blur' }],
        slot: 'selectA',
      },
      {
        field: 'extendPrecision',
        component: 'InputNumber',
        label: t('精度'),
        helpMessage: [t('精度为单位所需保留的小数位数')],
        dynamicRules: ({ formModel }: any) => {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: (rule: any, value: any) => {
                // 存换算表达式
                accuracy.value = formModel.expressionValue ? formModel.expressionValue : 0;
                accuracy2.value =
                  Math.ceil(-Math.log10(1 / Number(accuracy.value))) + Number(standardUnitsAccuracy.value);
                if (!value && value !== 0) {
                  return Promise.reject(t('请输入精度'));
                } else if (!/^[0-9]$/.test(value)) {
                  return Promise.reject(t('[0,9]的自然数'));
                } else {
                  return Promise.resolve();
                }
              },
            },
          ];
        },
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        required: false,
      },
    ],
  });
  const buttonDisabled = (currentNodes: any) => {
    return !(currentNodes && currentNodes[0] && currentNodes[0]?.id);
  };

  const columns: TableColumn[][] = [
    [
      {
        title: t('标准单位名称'),
        align: 'left',
        dataIndex: 'unitName',
        fixed: 'left',
        resizable: true,
        formItemProps: {
          labelWidth: 98,
        },
      },
      {
        title: t('精度'),
        align: 'left',
        dataIndex: 'unitPrecision',
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('修约方式'),
        align: 'left',
        dataIndex: 'roundName',
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('启停'),
        align: 'left',
        dataIndex: 'state',
        hideInSearch: true,
        formItemProps: {
          component: 'Select',
          componentProps: () => ({
            options: [
              {
                label: t('停用'),
                value: 0,
              },
              {
                label: t('启用'),
                value: 1,
              },
            ],
          }),
        },
        customRender: ({ record }) => (
          <Switch
            disabled={!hasPermission('100040001000005')}
            checked={record.state == 1 ? true : false}
            onChange={(h, event: any) => {
              event.stopPropagation();
              changeSwitch(record);
            }}></Switch>
        ),
      },
      {
        title: t('备注'),
        align: 'left',
        dataIndex: 'remark',
        resizable: true,
        hideInSearch: true,
      },
      {
        title: t('操作'),
        align: 'left',
        key: 'ACTION',
        fixed: 'right',
        width: 220,
        actions: ({ record }) => [
          {
            label: t('查看'),
            ifShow: hasPermission('100040001000002'),
            onClick: () => {
              lookOrEdit(record, 'look');
            },
          },
          {
            label: t('编辑'),
            ifShow: !record.state && hasPermission('100040001000003'),
            onClick: () => {
              lookOrEdit(record, 'edit');
            },
          },
          {
            label: t('删除'),
            ifShow: !record.state && hasPermission('100040001000004'),
            onClick: () => {
              Modal.confirm({
                title: t('提示'),
                icon: createVNode(ExclamationCircleOutlined),
                closable: true,
                content: t('是否删除该单位'),
                okText: t('确定'),
                cancelText: t('取消'),
                onOk() {
                  deleteStandardUnits(record);
                },
              });
            },
          },
        ],
      },
    ],
    [
      {
        title: t('扩展单位名称'),
        align: 'left',
        dataIndex: 'extendUnitName',
        hideInSearch: true,
        fixed: 'left',
        resizable: true,
      },
      {
        title: t('换算表达式'),
        align: 'left',
        dataIndex: 'expression',
        hideInSearch: true,
        resizable: true,
      },
      {
        title: t('精度'),
        align: 'left',
        dataIndex: 'extendPrecision',
        hideInSearch: true,
        resizable: true,
      },

      {
        title: t('启停'),
        align: 'left',
        dataIndex: 'state',
        hideInSearch: true,
        formItemProps: {
          component: 'Select',
          componentProps: () => ({
            options: [
              {
                label: t('停用'),
                value: 0,
              },
              {
                label: t('启用'),
                value: 1,
              },
            ],
          }),
        },
        customRender: ({ record }) => (
          <Switch
            disabled={!hasPermission('100040001000010')}
            checked={record.state == 1 ? true : false}
            onClick={() => changeSwitch2(record)}></Switch>
        ),
      },
      {
        title: t('备注'),
        align: 'left',
        dataIndex: 'remark',
        hideInSearch: true,
        resizable: true,
      },
      {
        title: t('操作'),
        align: 'left',
        key: 'ACTION',
        fixed: 'right',
        width: 220,
        actions: ({ record }) => [
          {
            label: t('查看'),
            ifShow: hasPermission('100040001000007'),
            onClick: () => {
              lookOrEdit2(record, 'look');
            },
          },
          {
            label: t('编辑'),
            ifShow: !record.state && hasPermission('100040001000008'),
            onClick: () => {
              lookOrEdit2(record, 'edit');
            },
          },
          {
            label: t('删除'),
            ifShow: !record.state && hasPermission('100040001000009'),
            onClick: () => {
              Modal.confirm({
                title: t('提示'),
                icon: createVNode(ExclamationCircleOutlined),
                closable: true,
                content: t('是否删除该单位'),
                okText: t('确定'),
                cancelText: t('取消'),
                onOk() {
                  deleteExtendedUnits2(record);
                },
              });
            },
          },
        ],
      },
    ],
  ];

  // 获取扩展单位信息
  const getExtendedUnitsList = async (params: any) => {
    if (params.id) {
      return extendedUnitsList(params);
    }
    return Promise.resolve({
      data: [],
      total: 0,
    });
  };
  // 获取修约方式
  const getRounding = async () => {
    try {
      const { data } = await getRoundingList({});
      modalFormRef.value?.formRef?.updateSchema({
        field: 'roundCode',
        componentProps: {
          options: data || [],
        },
      });
    } catch (error: any) {
      message.error(error.message);
    }
  };

  const handleClickRow = (row: Recordable, index: number) => {
    if (index === 0) {
      standardUnitName.value = row.unitName;
      standardUnitNameId.value = row.id;
      standardUnitsAccuracy.value = row.unitPrecision;
    }
  };
  // 切换标准单位开关
  const changeSwitch = (val: any) => {
    let content = val.state ? t('是否停用该单位') : t('是否启用该单位');
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content,
      okText: t('确定'),
      cancelText: t('取消'),
      onOk() {
        confirmChangeSwitch(val);
      },
    });
  };
  const confirmChangeSwitch = async (row: any) => {
    // 若最先为开启true
    if (row.state) {
      try {
        const data = { ...row, state: false };
        const res: any = await editStandardUnit(data);
        if (res.code === 0) {
          message.success(t('停用成功'));
          tableInstance.value?.fetchData(0);
          return;
        }
        // 若标准单位下有启用状态的扩展单位，无法停用该标准单位,并给提示
        Modal.confirm({
          title: t('提示'),
          icon: createVNode(ExclamationCircleOutlined),
          closable: true,
          content: t(res.message),
          okText: t('确定'),
          cancelText: t('取消'),
        });
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else {
      try {
        const data = { ...row, state: true };
        const res: any = await editStandardUnit(data);
        if (res.code === 0) {
          message.success(t('启用成功'));
          tableInstance.value?.fetchData(0);
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    }
  };
  // 新增编辑查看按钮
  const lookOrEdit = async (val: any, type: String) => {
    getRounding();
    if (type === 'add') {
      title.value = t('新增标准单位');
      formProps.initialValues = {};
      formProps.disabled = false;
    }
    if (type === 'look') {
      title.value = t('查看标准单位');
      formProps.initialValues = val;
      formProps.disabled = true;
    }
    if (type === 'edit') {
      title.value = t('编辑标准单位');
      formProps.initialValues = val;
      formProps.disabled = false;
    }
    // 打开弹框
    open.value = true;
  };
  // 确定
  const ok = async () => {
    if (title.value == t('查看标准单位')) {
      open.value = false;
    }
    if (title.value == t('新增标准单位')) {
      const data = await modalFormRef.value?.validate();
      try {
        const res: any = await addStandardUnit(data);
        if (res.code === 0) {
          message.success(t('新增成功'));
          tableInstance.value?.fetchData(0);
          open.value = false;
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else if (title.value == t('编辑标准单位')) {
      const data = await modalFormRef.value?.validate();
      try {
        const editData = {
          ...data,
          roundName: undefined,
          id: formProps.initialValues?.id,
          state: formProps.initialValues?.state,
        };
        await editStandardUnit(editData);
        message.success(t('编辑成功'));
        open.value = false;
        tableInstance.value?.fetchData(0);
      } catch (error: any) {
        message.error(t(error.message));
      }
    }
  };
  // 弹框取消按钮
  const cancel = () => {
    modalFormRef.value?.resetForm();
    formProps.initialValues = {};
  };
  const deleteStandardUnits = async (row: any) => {
    try {
      const data = { id: row.id };
      await deleteStandardUnit(data);
      message.success(t('删除成功'));
      tableInstance.value?.fetchData(0);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 切换扩展单位开关
  const changeSwitch2 = (val: any) => {
    let content = val.state ? t('是否停用该单位') : t('是否启用该单位');
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      closable: true,
      content,
      okText: t('确定'),
      cancelText: t('取消'),
      onOk() {
        confirmChangeSwitch2(val);
      },
    });
  };
  const confirmChangeSwitch2 = async (row: any) => {
    // 若最先为开启true
    if (row.state) {
      try {
        const data = { ...row, state: false };
        const res: any = await updateExtendState(data);
        if (res.code === 0) {
          message.success(t('停用成功'));
          tableInstance.value?.fetchData(1);
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else {
      try {
        const data = { ...row, state: true };
        const res: any = await updateExtendState(data);
        if (res.code === 0) {
          message.success(t('启用成功'));
          tableInstance.value?.fetchData(1);
          return;
        }
        // 若标准单位为停用，无法启用该扩展单位
        Modal.confirm({
          title: t('提示'),
          icon: createVNode(ExclamationCircleOutlined),
          closable: true,
          content: t('标准单位未启用'),
          okText: t('确定'),
          cancelText: t('取消'),
        });
      } catch (error: any) {
        message.error(t(error.message));
      }
    }
  };
  // 回显标准单位方法
  const echo = () => {
    const data = [{ label: standardUnitName.value, value: standardUnitNameId.value }];
    formProps2.schemas[0].componentProps.options = data;
    formProps2.initialValues.unitId = data[0].value;
    addonAfter2.value = standardUnitName.value;
  };
  // 新增编辑查看按钮
  const lookOrEdit2 = async (val: any, type: String) => {
    if (type === 'add') {
      title2.value = t('新增扩展单位');
      formProps2.initialValues = {};
      formProps2.disabled = false;
      formProps2.schemas[0].componentProps.disabled = true;
      echo();
    }
    if (type === 'look') {
      title2.value = t('查看扩展单位');
      formProps2.initialValues = val;
      formProps2.disabled = true;
      echo();
    }
    if (type === 'edit') {
      title2.value = t('编辑扩展单位');
      formProps2.initialValues = { ...val };
      formProps2.disabled = false;
      formProps2.schemas[0].componentProps.disabled = true;
      // 编辑扩展单位时回显标准单位下拉
      echo();
    }
    // 打开弹框
    open2.value = true;
  };
  // 确定
  const ok2 = async () => {
    if (title2.value === t('新增扩展单位')) {
      const data: any = await modalFormRef2.value?.validate();
      try {
        const res: any = await addExtendedUnit(data);
        if (res.code === 0) {
          if (data?.extendPrecision < accuracy2.value) {
            message.warning(t('单位配置的精度在换算时可能存在精度丢失；确认后单位仍配置成功'));
          }
          message.success(t('新增成功'));
          tableInstance.value?.fetchData(1);
          open2.value = false;
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else if (title2.value === t('编辑扩展单位')) {
      const data = await modalFormRef2.value?.validate();
      try {
        const res: any = await editExtendedUnit(data);
        if (res.code === 0) {
          if (data?.extendPrecision < accuracy2.value) {
            message.warning(t('单位配置的精度在换算时可能存在精度丢失；确认后单位仍配置成功'));
          }
          message.success(t('编辑成功'));
          open2.value = false;
          tableInstance.value?.fetchData(1);
          return;
        }
        message.error(t(res.message));
      } catch (error: any) {
        message.error(t(error.message));
      }
    } else {
      open2.value = false;
    }
  };
  // 弹框取消按钮
  const cancel2 = () => {
    modalFormRef2.value?.resetForm();
    formProps2.initialValues = {};
  };
  const deleteExtendedUnits2 = async (row: any) => {
    try {
      const data = { id: row.id };
      const res: any = await deleteExtendedUnit(data);
      if (res.code === 0) {
        message.success(t('删除成功'));
        tableInstance.value?.fetchData(1);
        return;
      }
      Modal.confirm({
        title: t('提示'),
        icon: createVNode(ExclamationCircleOutlined),
        closable: true,
        content: t(res.message + '，无法删除'),
        okText: t('确定'),
        cancelText: t('取消'),
      });
    } catch (error: any) {
      message.error(t(error.message));
    }
  };
</script>

<style scoped lang="less">
  .conversion {
    width: 100%;
    display: flex;
    align-items: center;
  }
  :deep(.plat-input-group-wrapper-status-error .plat-input-group-addon) {
    color: #242526;
    border-color: #d4d7d9;
  }
</style>
