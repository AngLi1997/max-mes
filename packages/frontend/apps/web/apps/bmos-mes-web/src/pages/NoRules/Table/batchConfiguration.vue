<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('批量配置')"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium"
    @cancel="cancel">
    <template #footer>
      <div class="steps-action">
        <Button v-if="current < steps.length - 1" @click="cancel">
          {{ t('取消') }}
        </Button>
        <Button v-if="current < steps.length - 1" type="primary" @click="next">
          {{ t('下一步') }}
        </Button>

        <Button v-if="current > 0" style="margin-left: 8px" @click="prev">
          {{ t('上一步') }}
        </Button>
        <Button v-if="current == steps.length - 1" type="primary" @click="batchConfigurationOk">
          {{ t('确定') }}
        </Button>
      </div>
    </template>
    <!-- 第一步的内容 -->
    <Steps :current="current" :items="items" size="small"></Steps>
    <div v-show="current < steps.length - 1" class="firstStep">
      <Form ref="formRef" :model="formState" :wrapper-col="wrapperCol" :rules="rules">
        <Form.Item
          ref="productRules"
          :label="props.type == 'PRODUCT_PLAN_BATCH_NO' ? t('生产批号规则') : t('指令单编号规则')"
          :labelCol="{ span: 6 }"
          name="productRules">
          <Select
            v-model:value="formState.productRules"
            :placeholder="t('请选择')"
            allowClear
            showSearch
            :options="rulesList"
            :filter-option="(input, option: any) => option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0"
            style="width: 305px"
            @change="changeRules"></Select>
        </Form.Item>
      </Form>
    </div>
    <!-- 第二步的内容 -->
    <div v-show="current > 0">
      <!-- 选择树 -->
      <BMSearchTree
        ref="searchTreeRef"
        v-model:expandedKeys="expandedKeys"
        v-model:checkedKeys="checkedKeys"
        v-model:selectedKeys="selectedKeys"
        :showAllAddIcon="false"
        :showAddChildren="false"
        :showDeleteNode="false"
        :showAction="false"
        checkable
        v-bind="treeProps"
        @check="check"></BMSearchTree>
    </div>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { BMModalForm, ModalFormInstance, BMSearchTree, SearchTreeInstance } from '@bmos/components';
  import { reactive, ref, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button, Steps, Form, Select } from 'ant-design-vue';
  import { getDictNoRulesList, getRulesProcessList, getRulesDetailCode, batchConfigurationSave } from '@/services';
  import type { Rule } from 'ant-design-vue/es/form';
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    type: {
      type: String || undefined,
      default: '',
    },
  });
  const emits = defineEmits(['updateTableData']);

  const formState = reactive({
    productRules: undefined,
  });
  const expandedKeys = ref<string[]>(['all']); //展开的树节点
  const selectedKeys = ref<string[]>([]);
  const checkedKeys = ref<any[]>([]); //用于回显的数据

  // 表单校验
  const rules: Record<string, Rule[]> = {
    productRules: [{ required: true, trigger: 'blur' }],
  };
  const rulesList = ref([]);

  const wrapperCol = { span: 15 };
  const modalFormRef = ref<ModalFormInstance>();
  const formRef = ref();
  const open = ref<boolean>(false);
  const codeRuleName = ref(); //编码规则名称
  const codeRuleCode = ref(); //编码规则code
  const current = ref<number>(0);
  // 下一步
  const next = async () => {
    const res = await formRef.value?.validate();
    // 编码规则code查询工艺集合
    try {
      const data = {
        编码: 'code',
        code: res.productRules,
      };
      const res2 = await getRulesDetailCode(data.code);
      checkedKeys.value = res2.data; //回显树
      expandedKeys.value = res2.data; //展开树
    } catch (error: any) {
      message.error(error.message);
    }
    current.value++;
  };
  const prev = () => {
    current.value--;
  };
  const steps = ref([
    {
      title: t('选择编号规则'),
      content: 'First-content',
    },
    {
      title: t('选择工艺'),
      content: 'Second-content',
    },
  ]);
  const items = steps.value.map(item => ({
    key: item.title,
    title: item.title,
  }));

  const openModal = () => {
    open.value = true;
  };
  // 取消
  const cancel = () => {
    open.value = false;
    current.value = 0;
    formRef.value.resetFields();
  };
  // 下拉切换
  const changeRules = async (val: any, option: any) => {
    codeRuleName.value = option.name;
    codeRuleCode.value = option.value;
  };
  // 树方法
  const searchTreeRef = ref<SearchTreeInstance>();
  const treeProps = reactive<any>({
    addChildrenNeedCode: true,
    fieldNames: { title: 'name', key: 'id' },
    treeData: [],
  });
  // 选中复选框触发
  const check = (selectedKeys: any) => {
    checkedKeys.value = selectedKeys;
  };

  // 批量配置弹窗确定按钮
  const batchConfigurationOk = async () => {
    try {
      const data = {
        codeRuleName: codeRuleName.value,
        codeRuleCode: codeRuleCode.value,
        processIds: checkedKeys.value,
        type: props.type,
      };
      if (!data.processIds || data.processIds.length === 0) {
        return message.error(t('工艺不能为空'));
      }
      await batchConfigurationSave(data);
      message.success(t('操作成功'));
      formRef.value.resetFields();
      open.value = false;
      current.value = 0;
      // // 刷新列表
      emits('updateTableData');
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 获取编号规则的下拉框列表
  const getNoRulesList = async () => {
    try {
      const data = {
        dictId: props.type == 'PRODUCT_PLAN_BATCH_NO' ? '1729066680262463488' : '120020009002',
      };
      const res = await getDictNoRulesList(data);
      const datas = res.data.map((item: any) => {
        return {
          ...item,
          label: item.label + '-' + item.value,
          name: item.label,
        };
      });
      rulesList.value = datas;
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 查询全部工艺产品树
  const GetRulesProcessList = async () => {
    try {
      const res = await getRulesProcessList();
      treeProps.treeData = [
        {
          name: t('全部'),
          id: 'all',
          key: 'all',
          children: res.data,
        },
      ];
    } catch (error: any) {
      message.error(error.message);
    }
  };

  onMounted(() => {});

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getNoRulesList();
        GetRulesProcessList();
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal });
</script>
<style lang="less" scoped>
  .firstStep {
    padding: 35px 0px 0 40px;
    box-sizing: border-box;
    height: 358px;
  }
  .bmos-search-tree {
    height: 358px;
  }
</style>
