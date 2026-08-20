import {
	t
} from '@/utils/useBmosI18n.js';
import {
	ref
} from 'vue';
export const tableConfig = ref([
	{
		label: t('序号'),
		filed: 'index',
		width: '50'
	},
	{
		label: t('状态'),
		filed: 'signStatus',
		width: '70',
		align: 'center'
	},
	{
		label: t('物料编码'),
		filed: 'materialCode',
		width: '100'
	},
	{
		label: t('物料名称'),
		filed: 'materialName',
		width: '100'
	},
	{
		label: t('物料批号'),
		filed: 'materialBatchNo',
		width: '150'
	},
	{
		label: t('物料件号'),
		filed: 'no',
		width: '150'
	},
	{
		label: t('净重'),
		filed: 'netWeight',
		width: '120'
	},
	{
		label: t('毛重'),
		filed: 'grossWeight',
		width: '80'
	},
	{
		label: t('皮重'),
		filed: 'tareWeight',
		width: '80'
	},
	{
		label: t('单位'),
		filed: 'unit',
		width: '50'
	},
	{
		label: t('称量人'),
		filed: 'weigherName',
		width: '150'
	},
	{
		label: t('复核人'),
		filed: 'reCheckerName',
		width: '150'
	},
	{
		label: t('容器'),
		filed: 'containerName',
		width: '150'
	},
	{
		label: t('货位'),
		filed: 'materialPositionName',
		width: '150'
	},
	{
		label: t('称量时间'),
		filed: 'weighDate',
		width: '150'
	}
]);
