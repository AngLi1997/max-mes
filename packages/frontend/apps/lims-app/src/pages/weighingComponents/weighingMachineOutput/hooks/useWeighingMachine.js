import {
  queryTareWeighConfigByIdApi,
} from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { useScan } from '@/utils/useScan.js';
import { ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useMaterialWeighing = ({ value, props }) => {
  const { showNotify } = useNotify();
  const { bmosScanCode } = useScan();

  const tare = ref('');
  // 获取皮重信息
  const getTareInfo = async (id) => {
    try {
      const res = await queryTareWeighConfigByIdApi({
        tareWeighId: id,
        unitId: props.unitId,
      });
      if (res.data) {
        tare.value = res.data.tareWeigh || '';
        value.value.tareWeight = tare.value;
      }
      else {
        showNotify({
          message: t('请扫描正确的皮重'),
          type: 'danger',
        });
      }
    }
    catch (error) {
      error.message && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  // 扫描
  const iconClick = async () => {
    const success = async (res) => {
      const { result } = res;
      if (!result) {
        return;
      }
      const type = result.slice(0, 2);
      const code = result.slice(2);
      if (type === '06') {
        getTareInfo(code);
      }
      else {
        showNotify({
          message: t('请扫描皮重二维码'),
          type: 'danger',
        });
      }
    };
    // #ifdef APP-PLUS
    bmosScanCode({
      success,
      fail: () => {
        showNotify({
          message: t('扫码失败'),
          type: 'danger',
        });
      },
    });
    // #endif
    // #ifdef H5
    success({ result: tare.value });
    // #endif
  };
  return {
    tare,
    iconClick,
  };
};
