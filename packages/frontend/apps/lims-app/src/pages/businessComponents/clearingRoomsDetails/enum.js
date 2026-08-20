import { t } from '@/utils/useBmosI18n.js';
export const stateCorol = {
    1: 'primary',
    2: 'warning',
    3: 'success'
};
export const currentState = {
    1: t('在用'),
    2: t('待清场'),
    3: t('已清场')
};

export const segmentedList = Object.freeze({
    CLEAN_CHECK: 'CLEAN_CHECK', // 清场检查
    CLEAN_INFO: 'CLEAN_INFO', // 清场信息
    CLEAN_IMPLEMENT: 'CLEAN_IMPLEMENT' // 清场执行
});
