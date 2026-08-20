import {
	ref
} from 'vue';

export const useSubTab = () => {
	// 初始数据
	const currentList = ref();
	// 配料总量
	const orderQuantity = ref(0);
	// 理论总量
	const theoryAmount = ref(0);
	// 勾选的数据
	const selectedData = ref([]);
	return {
		currentList,
		orderQuantity,
		theoryAmount,
		selectedData
	};
};
