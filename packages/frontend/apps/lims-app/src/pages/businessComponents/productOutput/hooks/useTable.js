import {
	getOutputFinishedList
} from '@/api';
import {
	ref,
	reactive
} from 'vue';
import { t } from '@/utils/useBmosI18n.js';
export const useTable = (finishedId) => {
	const tableRef = ref();
  const tableProps = reactive({
    pagination: false,
    dataRequest: async(params) => {
      return await getOutputFinishedList({
				id: finishedId.value
			});
    },
    border: true,
    tableColProps: [
			{
        prop: 'INDEX',
        label: t('序号'),
				width: 60
      },
      {
        prop: 'productMergeCode',
        label: t('成品编码')
      },
			{
				prop: 'productName',
				label: t('成品名称')
			},
			{
				prop: 'specification',
				label: t('成品规格')
			},
			{
				prop: 'productBatchNo',
				label: t('成品批号')
			},
			{
				prop: 'singleQuantity',
				label: t('单件量')
			},
			{
				prop: 'unitName',
				label: t('单位')
			},
			{
				prop: 'number',
				label: t('件数')
			},
			{
				prop: 'operatorName',
				label: t('操作人')
			},
			{
				prop: 'createTime',
				label: t('操作时间')
			}
    ]
  });
	return {
		tableRef,
		tableProps
	};
};
