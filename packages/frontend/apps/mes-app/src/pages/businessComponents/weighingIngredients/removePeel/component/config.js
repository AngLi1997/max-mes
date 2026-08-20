import {
	t
} from '@/utils/useBmosI18n.js';
import {
	ref
} from 'vue';
// 物料数据配置
export const msgList = ref([
	{
		filed: 'quantity',
		label: t('物料总量'),
		class: ''
	},
	{
		filed: 'targetQuantity',
		label: t('目标量'),
		class: ''
	},
	{
		filed: 'weighedQuantity',
		label: t('已称量'),
		class: 'green'
	},
	{
		filed: 'unWeighedQuantity',
		label: t('未称量'),
		class: 'orange'
	},
	{
		filed: 'unit',
		label: t('单位'),
		class: ''
	}
]);
export const tableConfig = ref([
	{
		label: t('序号'),
		filed: 'index',
		width: '50'
	},
	{
		label: t('净重'),
		filed: 'netWeight',
		width: '100'
	},
	{
		label: t('皮重'),
		filed: 'tareWeight',
		width: '100'
	},
	{
		label: t('毛重'),
		filed: 'grossWeight',
		width: '100'
	},
	{
		label: t('单位'),
		filed: 'unit',
		width: '100'
	},
	{
		label: t('容器'),
		filed: 'containerName',
		width: '100'
	},
	{
		label: t('暂存货位'),
		filed: 'materialPositionName',
		width: '100'
	}
]);
