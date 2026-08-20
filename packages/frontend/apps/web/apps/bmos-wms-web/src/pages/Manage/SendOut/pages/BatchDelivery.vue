<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="handleClickBack">
          {{ t('仓库发料') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ breadcrumbTitle }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="handleClickBack">{{ t('返回') }}</Button>
      <Button type="primary" @click="handleDone">{{ t('完成') }}</Button>
    </template>
    <div class="product-info">
      <BMTableTitle :title="t('生产信息')"></BMTableTitle>
      <Skeleton
        v-if="loading"
        active
        :title="false"
        :paragraph="{
          rows: 2,
        }" />
      <BMDescriptions v-else :list="productInfoList" :column="3"></BMDescriptions>
    </div>
    <div class="delivery-info">
      <BMTableTitle :title="t('发料信息')"></BMTableTitle>
      <BMTable
        ref="tableInstance"
        :data-source="deliveryList"
        :columns="deliveryColumns"
        row-key="cargoId"
        :pagination="false"
        :scroll="{ x: 1380, y: 400 }"
        :showToolBar="false"
        :search="false"></BMTable>
    </div>
  </BreadcrumbButton>
  <Delivery v-model:open="deliveryOpen" :rowData="rowData" :sendOutDetail="sendOutDetail" @updateTable="updateTable" />
  <Sign
    v-model:open="signOpen"
    :signatureData="JSON.stringify(signatureData)"
    :userList="permissionCodeUserList"
    :signatureAction="signatureAction"
    :labelList="labelList"
    @signSuccess="signSuccess"></Sign>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import {
    BMTableTitle,
    BMDescriptions,
    DescriptionsItemProps,
    BMTable,
    TableColumn,
    Recordable,
    TableInstance,
  } from '@bmos/components';
  import Delivery from '../components/Delivery.vue';
  import { LabelList } from '@/components/Sign/type';
  import { reqSendOutQueryDetail, reqSendOutSendout } from '@/services';
  import { usePermissionCodeUserList } from '@/hooks';
  import { message } from 'ant-design-vue';
  import Sign from '@/components/Sign';
  import { SendOrderType } from '../enum';

  const router = useRouter();
  const route = useRoute();

  const handleClickBack = () => {
    router.push({
      name: 'SendOut',
    });
  };

  const tableInstance = ref<TableInstance>();

  const breadcrumbTitle = ref<string>(t('批次发料'));
  const productInfoList = ref<DescriptionsItemProps[]>();

  const deliveryList = ref<Recordable[]>([]);
  const sendOutDetail = ref<Recordable>({});
  const loading = ref<boolean>(false);
  const getDeliveryList = async (id: string) => {
    try {
      loading.value = true;
      const { data } = await reqSendOutQueryDetail(id);
      const {
        list,
        productId,
        productName,
        productCode,
        productSpecification,
        processName,
        batchNo,
        pullOrderNo,
        sendOrderType,
      } = data;
      if (sendOrderType?.value === SendOrderType.CARGO) {
        breadcrumbTitle.value = t('货品发料');
        tableInstance.value?.removeColumn('batchNo');
      }
      deliveryList.value = list;
      sendOutDetail.value = {
        productId,
        productName,
        productCode,
        productSpecification,
        processName,
        batchNo,
        pullOrderNo,
        sendOrderType: sendOrderType.value,
      };
      productInfoList.value = [
        {
          label: t('产品名称'),
          value: productName as string,
        },
        {
          label: t('产品编码'),
          value: productCode as string,
        },
        {
          label: t('产品规格'),
          value: productSpecification as string,
        },
        {
          label: t('工艺名称'),
          value: processName as string,
        },
        {
          label: t('生产批号'),
          value: batchNo as string,
        },
        {
          label: t('领料单'),
          value: pullOrderNo as string,
        },
      ];
      loading.value = false;
    } catch (error) {}
  };

  const signatureAction = computed(() => {
    return sendOutDetail.value.sendOrderType === SendOrderType.BATCH ? 36 : 37;
  });

  const rowData = ref<Recordable>({});
  const deliveryOpen = ref<boolean>(false);
  const deliveryColumns: TableColumn[] = [
    {
      title: t('货品名称'),
      dataIndex: 'cargoName',
      fixed: 'left',
    },
    {
      title: t('货品编码'),
      dataIndex: 'cargoCode',
    },
    {
      title: t('货品规格'),
      dataIndex: 'cargoSpecification',
    },
    {
      title: t('货品批号'),
      dataIndex: 'batchNo',
    },
    {
      title: t('计划量'),
      dataIndex: 'targetQuantity',
    },
    {
      title: t('实发量'),
      dataIndex: 'realQuantity',
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 100,
      actions: ({ record }) => [
        {
          label: t('发料'),
          onClick: () => {
            rowData.value = record;
            deliveryOpen.value = true;
          },
        },
      ],
    },
  ];

  const updateTable = (data: Recordable) => {
    const { productList, realQuantity, cargoId, inventoryBatchId, type } = data;
    const index = deliveryList.value.findIndex((item): any =>
      type === SendOrderType.BATCH ? item.inventoryBatchId === inventoryBatchId : item.cargoId === cargoId,
    );
    deliveryList.value[index].productList = productList;
    deliveryList.value[index].realQuantity = realQuantity;
  };

  const { getPermissionCodeUserList, permissionCodeUserList } = usePermissionCodeUserList();

  const signOpen = ref<boolean>(false);
  const signatureData = ref<any>({});
  const labelList: LabelList[] = [
    {
      label: t('发料人'),
    },
    {
      label: t('复核人'),
    },
  ];
  const curParams = ref<Recordable>({});
  const handleDone = async () => {
    curParams.value = {
      id: route.query.id as string,
      sendOrderType: sendOutDetail.value.sendOrderType,
      sendList: deliveryList.value
        ?.map((item): any => {
          if (item.productList?.length) {
            return {
              businessId: item.inventoryBatchId ? item.inventoryBatchId : item.cargoId,
              inventoryIds: item.productList?.map((product: any) => product.id),
            };
          }
          return false;
        })
        .filter(Boolean),
    };
    signatureData.value = curParams;
    signOpen.value = true;
  };
  const signSuccess = async (data: Recordable) => {
    try {
      const { submitterId, receiverId } = data;
      curParams.value = {
        ...curParams.value,
        senderId: submitterId,
        reCheckerId: receiverId,
      };
      await reqSendOutSendout(curParams.value);
      message.success(t('发料成功'));
      router.push({
        name: 'SendOut',
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  onMounted(() => {
    const { id } = route.query;
    getDeliveryList(id as string);
    getPermissionCodeUserList('150020003000002');
  });
</script>

<style scoped lang="less">
  .delivery-info {
    padding-top: var(--bmos-padding-small);
    flex: 1;
    display: flex;
    flex-direction: column;
    .bmos-table-title {
      margin-bottom: var(--bmos-margin-small);
    }
  }
</style>
