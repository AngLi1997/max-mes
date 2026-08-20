import { ref } from 'vue';
import { t } from '@/utils/useBmosI18n.js';
export const useParams = () => {
    const isDate = ref(false);
    // 是否跳转
    const isEin = ref(false);
    // 当前页ID
    const tabData = ref(null);
    // 详情信息
    const specifics = ref();
    // 签名
    const signOpen = ref(false);
    // 签名参数
    const signatureParams = ref({});
    // 签名内容框
    const labelList = ref([{
        label: t('操作人'),
        signatureAction: 45
    }, {
        label: t('复核人'),
        signatureAction: 45,
        menuId: 121030002
    }]);
    // 签名备注
    const remark = ref(false);
    return {
        signOpen,
        tabData,
        specifics,
        signatureParams,
        labelList,
        remark,
        isEin,
        isDate
    };
};
