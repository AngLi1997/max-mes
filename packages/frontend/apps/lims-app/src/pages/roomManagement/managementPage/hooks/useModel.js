export const useModel = ({ UseParams, UseTagContent, filterData }) => {
    const {
        params
    } = UseParams;
    const { roomList } = UseTagContent;
    // 确定
    const filterConfirm = () => {
        params.value = {
            ...params.value,
            ...filterData.value,
            pageNum: 1
        };
        roomList();
    };
    // 重置
    const resetConfirm = () => {
        const data = { code: '', name: '', status: '' };
        params.value = {
            ...params.value,
            ...data,
            pageNum: 1
        };
        roomList();
    };
    return {
        filterConfirm,
        resetConfirm
    };
};
