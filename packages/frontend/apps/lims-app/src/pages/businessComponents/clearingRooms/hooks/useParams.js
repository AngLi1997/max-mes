import { reactive, ref } from 'vue';

export const useParams = () => {
    const filterData = ref({ name: '', code: '' });
    // 工序id
    const currentList = ref(null);
    // 是否跳转
    const isEsy = ref(false);
    // 是否下拉刷新
    const triggered = ref(false);
    // 瀑布流全部数据
    const roomManList = reactive({
        data: [],
        listA: [],
        listB: [],
        listC: []
    });
    return {
        isEsy,
        triggered,
        roomManList,
        currentList,
        filterData
    };
};
