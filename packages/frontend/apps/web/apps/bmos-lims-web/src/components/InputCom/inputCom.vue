<!-- 录入 -->
<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ t('检验管理') }}</breadcrumb-item>
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('录入') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <Space :size="16">
          <!-- <div class="header-btn"> -->
          <Button type="primary" @click="submit">{{ t('提交') }}</Button>
          <Button type="primary" @click="save">{{ t('保存') }}</Button>
          <div style="width: 1px; height: 26px; background-color: #d4d7d9"></div>
          <Button @click="back">{{ t('返回') }}</Button>
          <!-- </div> -->
        </Space>
      </Col>
    </Row>
    <LimsCard style="height: calc(100% - 80px)">
      <BMTable
        ref="tableInstance"
        :data-request="loadData"
        :columns="columns"
        row-key="id"
        :showRefresh="false"
        :showIndex="false"
        :scroll="{ y: 500 }"
        :formProps="formProps">
        <template #headerTitle>
          <BMTableTitle :title="t('检验录入')"></BMTableTitle>
        </template>
      </BMTable>
    </LimsCard>
  </div>
  <Sign
    ref="signModalRef"
    v-model:open="signOpen"
    v-bind="signModalProps"
    :signatureDataFn="signatureDataFn"
    @signSuccess="submitSuccess"></Sign>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { BMTableTitle, BMTable, TableInstance } from '@bmos/components';
  import { LimsCard } from '@/components/Card';
  import { computed, reactive, ref } from 'vue';
  import {
    getCheckOrderAnalyzeInfo,
    saveCheckOrderInspect,
    submitCheckOrderInspect,
    getCheckOrderAnalyzeValid,
  } from '@/services/index';
  import Sign from '@/components/Sign';
  import { Alert, Input, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { useRouter } from 'vue-router';

  const props = defineProps({
    data: {
      type: Object,
      default: () => {
        return {};
      },
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  });

  const router = useRouter();

  const comRouter = computed(() => {
    return t(router.currentRoute.value.meta.id as string);
  });

  const tableInstance = ref<TableInstance>();
  const signModalRef = ref<InstanceType<typeof Sign>>();
  const signOpen = ref(false);

  const signatureDataFn = (formModal: any) => {
    const list = Object.keys(resultMap.value).map(id => {
      return {
        checkAnalyzeId: id,
        orderNo: resultMap.value[id].orderNo,
        result: resultMap.value[id].result,
      };
    });
    const list2 = [] as any;
    if (Object.keys(resultMap.value).length == 0) {
      props.data.forEach(item => {
        list2.push({
          orderNo: item,
        });
      });
    }
    return JSON.stringify(formModal.reason ? { reason: formModal.reason } : [...list, ...list2]);
  };

  const signModalProps = reactive({
    title: t('检验终止'),
    extraSchemas: [
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
        componentProps: {
          maxLength: 100,
        },
      },
    ],
    signatureAction: 22,
  });

  // 结果和数据的映射
  const resultMap = ref<any>({});

  const emit = defineEmits(['back']);

  const back = () => {
    emit('back');
  };

  // 列表总数
  const total = ref(0);

  // 驼峰转下划线
  const toLine = (str: string) => {
    if (!str) return '';
    return str.replace(/([A-Z])/g, '_$1').toLowerCase();
  };

  const loadData = async (params: any, onChangeParams: any) => {
    const orderMap = {
      ascend: 'asc',
      descend: 'desc',
    };
    let orderBy = '';
    let dir = '';
    if (onChangeParams && onChangeParams.length >= 3) {
      orderBy = toLine(onChangeParams[2].field);
      dir = orderMap[onChangeParams[2].order];
    }
    try {
      const data = {
        ...params,
        orderBy,
        dir,
        orderNoList: props.data.join(','),
      };
      const res = await getCheckOrderAnalyzeInfo(data);

      const ans = {
        ...res,
        data: {
          ...res.data,
          list: res.data.list.map((item: any) => {
            return {
              ...item,
              result: resultMap.value[item.id]?.result ?? item.result,
              oldRes: item.result,
            };
          }),
        },
      };
      total.value = ans.data.total;
      return ans;
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  // 提交
  const submit = async () => {
    // signModalProps.title = t('提交')
    const temp = Object.keys(resultMap.value).map(id => {
      return {
        id,
        ...resultMap.value[id],
      };
    });
    const count =
      temp.filter((item: any) => item.result && !item.oldRes).length -
      temp.filter((item: any) => !item.result && item.oldRes).length;
    const { data } = await getCheckOrderAnalyzeValid({ orderNoList: props.data.join(','), count });
    signModalProps.extraSchemas =
      data <= 0
        ? []
        : [
            {
              field: 'label',
              component: () => (
                <Alert
                  class='approval-alert'
                  message={`${t('存在')}${data}${t('项未录入分析项，是否批量录为N/A并提交')}`}
                  type='warning'
                  showIcon={true}
                  icon={<ExclamationCircleOutlined />}
                />
              ),
            },
          ];
    signModalProps.signatureAction = 20;
    signOpen.value = true;
  };

  const save = () => {
    signModalProps.title = t('保存');
    signModalProps.extraSchemas = [];
    signModalProps.signatureAction = 21;
    signOpen.value = true;
  };
  const submitSuccess = async () => {
    try {
      const list = Object.keys(resultMap.value).map(id => {
        return {
          checkAnalyzeId: id,
          orderNo: resultMap.value[id].orderNo,
          result: resultMap.value[id].result,
        };
      });
      const list2 = [] as any;
      if (Object.keys(resultMap.value).length == 0) {
        props.data.forEach(item => {
          list2.push({
            orderNo: item,
          });
        });
      }
      if (signModalProps.signatureAction == 20) {
        await submitCheckOrderInspect([...list, ...list2]);
      } else {
        await saveCheckOrderInspect([...list]);
      }

      emit('back');
    } catch (error: any) {
      message.error(error?.message);
    }
  };

  const columns = reactive([
    {
      title: t('检验项目'),
      dataIndex: 'inspectName',
      resizable: true,
      sorter: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        },
      },
    },
    {
      title: t('检验单编码'),
      dataIndex: 'orderNo',
      resizable: true,
      sorter: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        },
      },
    },
    {
      title: t('检品名称'),
      dataIndex: 'productsName',
      resizable: true,
      hideInSearch: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        },
      },
    },
    {
      title: t('规格'),
      dataIndex: 'productsSpecification',
      resizable: true,
      hideInSearch: true,
      width: 120,
    },
    {
      title: t('批号'),
      dataIndex: 'batchNo',
      hideInSearch: true,
      resizable: true,
      width: 160,
    },
    {
      title: t('分析项'),
      dataIndex: 'name',
      resizable: true,
      width: 160,
      formItemProps: {
        componentProps: {
          maxlength: 30,
        },
      },
    },
    {
      title: t('标准规定'),
      dataIndex: 'standard',
      hideInSearch: true,
      resizable: true,
      width: 190,
    },
    {
      title: t('结果'),
      dataIndex: 'result',
      fixed: 'right',
      hideInSearch: true,
      resizable: true,
      width: 160,
      customRender: ({ record }) => {
        return props.disabled ? (
          <span>{record.result}</span>
        ) : (
          <Input
            v-model:value={record.result}
            placeholder={t('请输入')}
            maxlength={30}
            allowClear={true}
            onChange={_e => {
              record.result = record.result.trim();
              resultMap.value[record.id] = record;
            }}
          />
        );
      },
    },
  ]);

  const formProps = {
    showAdvancedButton: false,
  };
</script>

<style lang="less" scoped>
  .mr-16 {
    margin-right: 16px;
  }

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    .crumb {
      line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }
</style>
