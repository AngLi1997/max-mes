import { getWeighDataListApi } from '@/api';
import { goBackToTargetPath } from '@/utils/func.js';
import { t } from '@/utils/useBmosI18n.js';
import { onMounted, reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useResult = ({ props }) => {
  const { showNotify } = useNotify();

  const tableRef = ref();

  const tableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'INDEX',
        label: t('序号'),
        width: 50,
      },
      {
        prop: 'weight',
        label: t('秤具示数'),
        width: 200,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 200,
      },
      {
        prop: 'weigher',
        label: t('称量人'),
        width: 200,
      },
      {
        prop: 'weighTime',
        label: t('称量时间'),
        width: 200,
      },

    ],
  });

  const leftClick = () => {
    uni.navigateBack();
  };
  const exitWeighing = () => {
    goBackToTargetPath();
  };

  // 获取称量结果详情
  const getResultDetail = async () => {
    try {
      const res = await getWeighDataListApi({
        componentInstanceId: props.id,
      });
      tableProps.data = res.data;
    }
    catch (error) {
      error.message
      && showNotify({
        message: error.message,
        type: 'warning',
      });
    }
  };

  onMounted(async () => {
    await getResultDetail();
  });

  return {
    tableRef,
    tableProps,
    leftClick,
    exitWeighing,
  };
};
