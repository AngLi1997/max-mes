import { type TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button } from 'ant-design-vue';

type ItemType = {
  label: string;
  field: string;
  vIf?: boolean;
  renderFn?: (record: any) => any;
};

export const useDetail = () => {
  const router = useRouter();

  /**
   * @description: 基础信息
   */
  const basicItems = reactive<ItemType[]>([
    {
      label: t('标本类型'),
      field: 'sampleType',
      renderFn: (record: any) => {
        return record?.sampleType?.name;
      },
    },
    {
      label: t('标本编号'),
      field: 'sampleNo',
    },
    {
      label: t('标本箱号'),
      field: 'boxId',
    },
    {
      label: t('标本批号'),
      field: 'sampleBatchNo',
    },
    {
      label: t('采浆日期'),
      field: 'slurryDate',
    },
    {
      label: t('标本状态'),
      field: 'sampleStatus',
      renderFn: (record: any) => {
        return record?.sampleStatus?.name;
      },
    },
    {
      label: t('来源单位'),
      field: 'originOrg',
    },
    {
      label: t('血浆编号'),
      field: 'plasmaNo',
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
    },
    {
      label: t('性别'),
      field: 'sex',
      renderFn: (record: any) => {
        return record?.sex?.name;
      },
    },
    {
      label: t('血型'),
      field: 'bloodType',
      renderFn: (record: any) => {
        return record?.bloodType?.name;
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
   * @description: 入库验收信息
   */
  const storageCheckItems = reactive<ItemType[]>([
    {
      label: t('浆站出库批号'),
      field: 'syncBatchNo',
    },
    {
      label: t('浆站出库日期'),
      field: 'stationOutDate',
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
      field: 'sampleAcceptanceStatus',
      renderFn: (record: any) => {
        return record?.sampleAcceptanceStatus?.name;
      },
    },
    {
      label: t('验收人'),
      field: 'acceptanceBy',
    },
    {
      label: t('验收日期'),
      field: 'acceptanceDate',
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
        return record?.warehouse?.name;
      },
    },
    {
      label: t('入库人'),
      field: 'inWarehouseBy',
    },
    {
      label: t('入库日期'),
      field: 'inWarehouseDate',
    },
    {
      label: t('货位号'),
      field: 'cargoSpaceNo',
    },
    {
      label: t('托盘号'),
      field: 'palletNo',
    },
    {
      label: t('库存状态'),
      field: 'sampleWarehouseStatus',
      renderFn: (record: any) => {
        return record?.sampleWarehouseStatus?.name;
      },
    },
    {
      label: t('有效期'),
      field: 'validateDate',
    },
    {
      label: t('剩余时间(天)'),
      field: 'remainingTime',
    },
  ]);

  /**
   * @description: 检验信息
   */
  const inspectionItems = reactive<ItemType[]>([
    {
      label: t('请验人'),
      field: 'inspectionBy',
    },
    {
      label: t('请验日期'),
      field: 'inspectionDate',
    },
    {
      label: t('审核状态'),
      field: 'inspectionAuditStatus',
      renderFn: (record: any) => {
        return record?.inspectionAuditStatus?.name;
      },
    },
    {
      label: t('审核人'),
      field: 'inspectionAuditBy',
    },
    {
      label: t('审核日期'),
      field: 'inspectionAuditDate',
    },
    {
      label: t('接收状态'),
      field: 'censorshipStatus',
      renderFn: (record: any) => {
        return record?.censorshipStatus?.name;
      },
    },
    {
      label: t('收验人'),
      field: 'receiveBy',
    },
    {
      label: t('收验日期'),
      field: 'receiveDate',
    },
    {
      label: t('检验日期'),
      field: 'testDate',
    },
  ]);

  /**
   * @description: 检验结果
   */
  const inspectionResultItems = reactive<ItemType[]>([
    {
      label: t('蛋白含量'),
      field: 'proteinContentResult',
      renderFn: (record: any) => {
        return record?.proteinContentResult?.name;
      },
    },
    {
      label: t('ALT'),
      field: 'altResult',
      renderFn: (record: any) => {
        return record?.altResult?.name;
      },
    },
    {
      label: t('HBsAg'),
      field: 'elisaHbsagResult',
      renderFn: (record: any) => {
        return record?.elisaHbsagResult?.name;
      },
    },
    {
      label: t('抗-HCV'),
      field: 'elisaHcvResult',
      renderFn: (record: any) => {
        return record?.elisaHcvResult?.name;
      },
    },
    {
      label: t('抗-HIV'),
      field: 'elisaHivResult',
      renderFn: (record: any) => {
        return record?.elisaHivResult?.name;
      },
    },
    {
      label: t('抗-TP'),
      field: 'elisaTpResult',
      renderFn: (record: any) => {
        return record?.elisaTpResult?.name;
      },
    },
    {
      label: t('HBV DNA'),
      field: 'pcrHbvResult',
      renderFn: (record: any) => {
        return record?.pcrHbvResult?.name;
      },
    },
    {
      label: t('HCV RNA'),
      field: 'pcrHcvResult',
      renderFn: (record: any) => {
        return record?.pcrHcvResult?.name;
      },
    },
    {
      label: t('HIV RNA'),
      field: 'pcrHivResult',
      renderFn: (record: any) => {
        return record?.pcrHivResult?.name;
      },
    },
    {
      label: t('免疫类型'),
      field: 'immunityType',
      renderFn: (record: any) => {
        return record?.immunityType?.name;
      },
    },
    {
      label: t('效价值'),
      field: 'titerValue',
    },
  ]);

  /**
   * @description: 维护信息
   */
  const maintainColumns = reactive<TableColumn[]>([
    {
      title: t('维护人'),
      dataIndex: 'maintainBy',
      width: 100,
      resizable: true,
    },
    {
      title: t('维护日期'),
      dataIndex: 'maintainDate',
      width: 150,
      resizable: true,
    },
    {
      title: t('维护状态'),
      dataIndex: 'maintainStatus',
      width: 100,
      resizable: true,
      customRender: ({ record }) => {
        return record?.maintainStatus?.name ?? '-';
      },
    },
    {
      title: t('备注'),
      dataIndex: 'maintainRemark',
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
      width: 170,
      resizable: true,
    },
    {
      title: t('操作事项'),
      dataIndex: 'content',
      width: 170,
      resizable: true,
    },
    {
      title: t('备注'),
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
    inspectionItems,
    inspectionResultItems,
    maintainColumns,
    operationColumns,
  };
};
