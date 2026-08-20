import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
export const useParams = () => {
  // 签名备注
  const remark = ref(false);
  // 切换签名
  const isSign = ref(1);
  // 签名
  const signOpen = ref(false);
  // 签名内容框
  const labelList = ref([]);
  const specifics = ref({});
  const paramsData = ref({});
  const segForm = reactive({
    params: {},
    rules: {
      operatorId: {
        rules: [
          {
            required: true,
            errorMessage: t('请选择清场人')
          }
        ]
      },
      verifierId: {
        rules: [
          {
            required: true,
            errorMessage: t('请选择复核人')
          }
        ]
      },
      clearanceTime: {
        rules: [
          {
            required: true,
            errorMessage: t('请选择清场时间')
          }
        ]
      },
      expireTime: {
        rules: [
          {
            required: true,
            errorMessage: t('请选择有效期')
          }
        ]
      }
    },
    form: [
      {
        name: t('清场人'),
        mode: 'field1',
        required: true,
        key: 'operatorId',
        value: '',
        data: []
      },
      {
        name: t('复核人'),
        mode: 'field2',
        required: true,
        key: 'verifierId',
        value: '',
        data: []
      },
      {
        name: t('清场时间'),
        mode: 'date-time',
        type: 'datetimerange',
        required: true,
        key: 'clearanceTime',
        placeholder: `${t('开始时间')} - ${t('结束时间')}`,
        value: ''
      },
      {
        name: t('有效期至'),
        mode: 'date-time',
        type: 'datetime',
        required: true,
        key: 'expireTime',
        value: ''
      }
    ]
  });
  return {
    isSign,
    signOpen,
    labelList,
    paramsData,
    segForm,
    remark,
    specifics
  };
};
