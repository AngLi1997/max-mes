import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button } from 'ant-design-vue';

type ItemType = {
  label: string;
  field: string;
  vIf?: boolean;
  componentProps?: any;
  renderFn?: any;
};

export const useDetail = () => {
  const router = useRouter();

  /**
   * @description: 基础信息
   */
  const basicItems = reactive<ItemType[]>([
    {
      label: t('血浆编号'),
      field: 'plasmaNo',
    },
    {
      label: t('血浆箱号'),
      field: 'containerNo',
    },
    {
      label: t('血浆批号'),
      field: 'batchNo',
    },
    {
      label: t('采浆日期'),
      field: 'slurryDate',
    },
    {
      label: t('血浆类型'),
      field: 'plasmaType',
      renderFn: (record: any) => {
        return record?.plasmaType?.label;
      },
    },
    {
      label: t('免疫类型'),
      field: 'immunityType',
    },
    {
      label: t('来源单位'),
      field: 'originOrg',
    },
    {
      label: t('限制级血浆'),
      field: 'restrictedFlag',
      renderFn: (record: any) => {
        return record?.restrictedFlag?.label;
      },
    },
  ]);

  /**
   * @description: 献浆者信息
   */
  const donorItems = reactive<ItemType[]>([
    {
      label: t('献浆者编号'),
      field: 'no',
    },
    {
      label: t('姓名'),
      field: 'name',
      componentProps: {
        span: 2,
      },
    },
    {
      label: t('性别'),
      field: 'sex',
      componentProps: {
        span: 2,
      },
      renderFn: (record: any) => {
        return record?.sex?.label;
      },
    },
    {
      label: t('血型'),
      field: 'bloodType',
      componentProps: {
        span: 2,
      },
      renderFn: (record: any) => {
        return record?.bloodType?.label;
      },
    },
    {
      label: t('操作'),
      field: 'operation',
      renderFn: (record: any) => {
        return record?.id ? (
          <Button
            type='link'
            onClick={() => {
              router.push({
                name: 'plasma-donor-detail',
                params: { id: record.id },
              });
            }}>
            {t('对应献浆详情')}
          </Button>
        ) : (
          ''
        );
      },
    },
  ]);

  /**
   * @description: 验收信息
   */
  const storageCheckItems = reactive<ItemType[]>([
    {
      label: t('浆站出库批号'),
      field: 'syncBatchNo',
    },
    {
      label: t('浆站出库日期'),
      field: 'beginTime',
    },
    {
      label: t('运输时间'),
      field: 'transitTime',
    },
    {
      label: t('运输温度'),
      field: 'temperature',
    },
    {
      label: t('验收状态'),
      field: 'acceptanceStatus',
      renderFn: (record: any) => {
        return record?.acceptanceStatus?.label;
      },
    },
    {
      label: t('验收人'),
      field: 'acceptanceByName',
    },
    {
      label: t('验收日期'),
      field: 'acceptanceTime',
    },
    {
      label: t('验收备注'),
      field: 'acceptanceRemark',
    },
  ]);

  /**
   * @description: 库存信息
   */
  const storageItems = reactive<ItemType[]>([
    {
      label: t('入库批号'),
      field: 'inWarehouseBatchNo',
    },
    {
      label: t('入库仓库'),
      vIf: getWarehouseConfigByCode.value,
      field: 'warehouse',
      renderFn: (record: any) => {
        return record?.warehouse?.label;
      },
    },
    {
      label: t('入库人'),
      field: 'warehouseByName',
    },
    {
      label: t('入库日期'),
      field: 'warehouseTime',
    },
    {
      label: t('货位号'),
      field: 'cargoSpaceNo',
    },
    {
      label: t('托盘号'),
      field: 'bigContainerNo',
    },
    {
      label: t('库存状态'),
      field: 'warehouseStatus',
      renderFn: (record: any) => {
        return record?.warehouseStatus?.label;
      },
    },
    {
      label: t('有效期'),
      field: 'validityDate',
    },
    {
      label: t('剩余时间(天)'),
      field: 'remainingTime',
    },
  ]);

  /**
   * @description: 检验结果
   */
  const inspectionResultItems = reactive<ItemType[]>([
    {
      label: t('外观'),
      field: 'appearance',
      renderFn: (record: any) => {
        return record?.appearance?.label;
      },
    },
    {
      label: t('蛋白含量'),
      field: 'proteinContentResult',
      renderFn: (record: any) => {
        return record?.proteinContentResult?.label;
      },
    },
    {
      label: t('ALT'),
      field: 'altResult',
      renderFn: (record: any) => {
        return record?.altResult?.label;
      },
    },
    {
      label: t('HBsAg'),
      field: 'elisaHBsAgResult',
      renderFn: (record: any) => {
        return record?.elisaHBsAgResult?.label;
      },
    },
    {
      label: t('抗-HCV'),
      field: 'elisaHcvResult',
      renderFn: (record: any) => {
        return record?.elisaHcvResult?.label;
      },
    },
    {
      label: t('抗-HIV'),
      field: 'elisaHivResult',
      renderFn: (record: any) => {
        return record?.elisaHivResult?.label;
      },
    },
    {
      label: t('抗-TP'),
      field: 'elisaTpResult',
      renderFn: (record: any) => {
        return record?.elisaTpResult?.label;
      },
    },
    {
      label: t('HBV DNA'),
      field: 'pcrHbvResult',
      renderFn: (record: any) => {
        return record?.pcrHbvResult?.label;
      },
    },
    {
      label: t('HCV RNA'),
      field: 'pcrHcvResult',
      renderFn: (record: any) => {
        return record?.pcrHcvResult?.label;
      },
    },
    {
      label: t('HIV RNA'),
      field: 'pcrHivResult',
      renderFn: (record: any) => {
        return record?.pcrHivResult?.label;
      },
    },
    {
      label: t('免疫类型'),
      field: 'immunityType',
    },
    {
      label: t('效价值'),
      field: 'titerValue',
    },
  ]);

  /**
   * @description: 检疫期信息
   */
  const quarantinePeriodItems = reactive<ItemType[]>([
    {
      label: t('对应编号'),
      field: 'corrPlasmaNo',
    },
    {
      label: t('对应关系'),
      field: 'corrRelationType',
      renderFn: (record: any) => {
        return record?.corrRelationType?.label;
      },
    },
    {
      label: t('检验日期'),
      field: 'quarantineTime',
    },
    {
      label: t('化验总结果'),
      field: 'totalResult',
      renderFn: (record: any) => {
        return record?.totalResult?.label;
      },
    },
    {
      label: t('不合格项目'),
      field: 'unqualifiedItem',
    },
    {
      label: t('采浆日期'),
      field: 'slurryDate',
    },
    {
      label: t('操作'),
      field: 'operation',
      renderFn: (record: any) => {
        return record?.plasmaNo ? (
          <Button
            type='link'
            onClick={() => {
              router.push({
                name: 'CheckQuery',
                query: { no: record.plasmaNo },
              });
            }}>
            {t('查看')}
          </Button>
        ) : (
          ''
        );
      },
    },
  ]);

  /**
   * @description: 分拣信息
   */
  const sortingItems = reactive<ItemType[]>([
    {
      label: t('分拣前批号'),
      field: 'batchNo',
    },
    {
      label: t('分拣后批号'),
      field: 'sortingBatchNo',
    },
    {
      label: t('分拣前箱/托盘号'),
      field: 'containerNoBefore',
    },
    {
      label: t('分拣后箱/托盘号'),
      field: 'containerNoAfter',
    },
    {
      label: t('分拣人'),
      field: 'sortingByName',
    },
    {
      label: t('分拣日期'),
      field: 'sortingTime',
    },
  ]);

  /**
   * @description: 维护信息
   */
  const maintainColumns = reactive<TableColumn[]>([
    {
      title: t('维护人'),
      dataIndex: 'byName',
      width: 100,
      resizable: true,
    },
    {
      title: t('维护日期'),
      dataIndex: 'time',
      width: 150,
      resizable: true,
    },
    {
      title: t('维护状态'),
      dataIndex: 'status',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.status?.label ?? '-';
      },
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 200,
      resizable: true,
    },
  ]);

  /**
   * @description: 操作信息
   */
  const operationColumns = reactive<TableColumn[]>([
    {
      title: t('操作人'),
      dataIndex: 'createBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('操作日期'),
      dataIndex: 'createTime',
      width: 150,
      resizable: true,
    },
    {
      title: t('操作事项'),
      dataIndex: 'content',
      width: 200,
      resizable: true,
    },
    {
      title: t('操作备注'),
      dataIndex: 'remark',
      width: 200,
      resizable: true,
    },
  ]);

  return {
    basicItems,
    donorItems,
    storageCheckItems,
    storageItems,
    inspectionResultItems,
    quarantinePeriodItems,
    sortingItems,
    maintainColumns,
    operationColumns,
  };
};
