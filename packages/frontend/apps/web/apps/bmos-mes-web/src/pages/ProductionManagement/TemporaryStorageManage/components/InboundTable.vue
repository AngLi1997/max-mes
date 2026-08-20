<template>
  <div class="inbound-materials">
    <div class="add-input">
      <Space>
        <FormItemRest>
          <Input v-model:value="inputMaterialsCode" :placeholder="t('请输入物料件号')" @keyup.enter="null"></Input>
        </FormItemRest>
        <Button type="primary" @click="addMaterial">{{ t('添加') }}</Button>
      </Space>
    </div>
    <div class="inbound-materials-table">
      <BMTable
        :columns="columns"
        :dataSource="materialList"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 300 }" />
    </div>

    <Row class="total">
      <Col :span="4">
        <span>
          {{ `${t('已选择')}：` }}
          <span class="numbers">{{ `${totalReactive.num} ${t('件')}` }}</span>
        </span>
      </Col>
      <Col :span="20">
        <span>
          {{ `${t('总计')}：` }}
          <span class="numbers">{{ `${totalReactive.total || '-'} ${materialList[0]?.unit || ''}` }}</span>
        </span>
      </Col>
    </Row>
  </div>
</template>
<script lang="tsx" setup>
  import { postUnitCalcSumAdapt, reqStorageMaterialInfoByNo } from '@/services';
  import { BMTable, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { BMIcons } from '@bmos/icons';
  import { debounce } from '@bmos/utils';
  import { message } from 'ant-design-vue';

  const materialList = defineModel<any[]>('materialList', { default: [] });

  const inputMaterialsCode = ref<string>('');

  const addMaterial = async () => {
    try {
      if (!inputMaterialsCode.value) {
        message.error(t('请输入物料件号'));
        return;
      }
      const { data } = await reqStorageMaterialInfoByNo(inputMaterialsCode.value);
      if (data && !data.materialPositionId) {
        if (materialList.value.some((r: any) => r.id === data.id)) {
          message.error(t('当前物料件已存在'));
          return;
        }
        materialList.value.push(data);
        rowChange();
      } else if (data && data.materialPositionId) {
        message.error(t('当前物料件已在暂存货位中'));
      } else {
        message.error(t('物料件不存在'));
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const deleteRecord = (record: any) => {
    materialList.value = materialList.value.filter((r: any) => r.id !== record.id);
    rowChange();
  };

  const columns: TableColumn[] = [
    {
      title: '',
      dataIndex: 'delete',
      fixed: 'left',
      width: 50,
      customRender: ({ record }: any) => {
        return (
          <BMIcons
            icon='CircleDelete'
            style={{
              fontSize: '16px',
              width: '16px',
              height: '16px',
            }}
            onClick={() => {
              deleteRecord(record);
            }}
          />
        );
      },
    },
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
      fixed: 'left',
      width: 100,
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
      width: 100,
    },
    {
      title: t('物料批号'),
      dataIndex: 'materialBatchNo',
      width: 100,
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
      width: 100,
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
      width: 100,
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
      width: 100,
    },
  ];

  const totalReactive = reactive<{
    num: number;
    total: number;
  }>({
    num: 0,
    total: 0,
  });

  const rowChange = debounce(async () => {
    try {
      if (!materialList.value.length) {
        totalReactive.num = 0;
        totalReactive.total = 0;
        return;
      }
      const dataModel = {
        list: materialList.value.map((r: any) => {
          return { unitId: r.unitId, value: r.availableQuantity };
        }),
        targetUnitId: materialList.value[0]?.finalUnitId,
      };
      const res = await postUnitCalcSumAdapt(dataModel);
      totalReactive.num = materialList.value?.length;
      totalReactive.total = res.data?.value;
    } catch (error: any) {
      console.log(error);
    }
  }, 800);
</script>
<style lang="less" scoped>
  .inbound-materials {
    .add-input {
      display: flex;
      justify-content: flex-end;
    }
    .inbound-materials-table {
      height: 240px;
      margin-top: var(--bmos-margin-large);
    }
    .total {
      margin-top: 20px;
      background-color: var(--bmos-primary-color-background);
      border-radius: 4px;
      padding: 10px;
    }
    .numbers {
      color: var(--bmos-primary-color);
    }
  }
</style>
