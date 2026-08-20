import type { TableColumn } from '@bmos/components';
import { BMEllipsis } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button } from 'ant-design-vue';

export type UseTableParams = {
  openDecompose: Function;
  distribute: Function;
};
// 状态样式
const style = {
  width: '7px',
  height: '7px',
  borderRadius: '50%',
  marginRight: '8px',
};
const colorList: any = {
  WAIT_DECOMPOSE: '#FF9A2F',
  WAIT_CONFIRM: '#2894FF',
  WAIT_SEND: '#574EFA',
  SEND: '#59BF78',
};
export const useTable = ({ openDecompose, distribute }: UseTableParams) => {
  const columns: TableColumn[] = [
    {
      title: t('指令单编号'),
      align: 'left',
      fixed: 'left',
      dataIndex: 'planNo',
      sorter: true,
      width: 200,
      resizable: true,
    },
    {
      title: t('产品名称'),
      align: 'left',
      dataIndex: 'productName',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'productMergeCode',
      width: 200,
      resizable: true,
    },
    {
      title: t('产品规格'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'productSpecification',
    },
    {
      title: t('工艺名称'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'processName',
    },
    {
      title: t('生产批号'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'batchNo',
      sorter: true,
    },
    {
      title: t('计划生产时间'),
      align: 'left',
      dataIndex: 'productDate',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('指令单类型'),
      align: 'left',
      width: 200,
      resizable: true,
      dataIndex: 'type',
      // formItemProps: {
      //   component: 'Select',
      //   componentProps: () => ({
      //     options: [
      //       {
      //         label: t('生产批次'),
      //         value: 'PRODUCT',
      //       },
      //       {
      //         label: t('实验批次'),
      //         value: 'EXPERIMENT',
      //       },
      //       {
      //         label: t('验证批次'),
      //         value: 'VERIFY',
      //       },
      //     ],
      //   }),
      // },
      customRender: ({ record }) => <div>{record.type?.label}</div>,
    },
    {
      title: t('状态'),
      align: 'left',
      fixed: 'right',
      width: 130,
      dataIndex: 'instructStatus',
      customRender: ({ record }) => (
        <div style='display: flex;align-items: center;'>
          <div
            style={{
              ...style,
              backgroundColor: colorList[record.instructStatus?.value],
            }}></div>
          <div style={{ color: colorList[record.instructStatus?.value] }}>{record.instructStatus?.label}</div>
        </div>
      ),
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 120,
      resizable: true,
      customRender: ({ record }) => (
        <div class='bmos-action-list'>
          {record.instructStatus?.value != 'WAIT_DECOMPOSE' ? (
            <Button
              v-hasAuth='120030003000002'
              type='link'
              onClick={() => openDecompose('view', record)}
              style='max-width: 100px; min-width: 40px;color: #2871FF;'>
              <BMEllipsis style='cursor: pointer;'>{t('查看')}</BMEllipsis>
            </Button>
          ) : (
            ''
          )}
          {record.instructStatus?.value == 'WAIT_DECOMPOSE' ? (
            <Button
              v-hasAuth='120030003000001'
              type='link'
              onClick={() => openDecompose('decompose', record)}
              style='max-width: 100px; min-width: 40px;color: #2871FF;'>
              <BMEllipsis style='cursor: pointer;'>{t('分解')}</BMEllipsis>
            </Button>
          ) : (
            ''
          )}
          {record.instructStatus?.value == 'WAIT_SEND' ? (
            <Button
              v-hasAuth='120030003000003'
              type='link'
              onClick={() => distribute(record)}
              style='max-width: 100px; min-width: 40px;color: #2871FF;'>
              <BMEllipsis style='cursor: pointer;'>{t('下发')}</BMEllipsis>
            </Button>
          ) : (
            ''
          )}
        </div>
      ),
    },
  ];

  return {
    columns,
  };
};
