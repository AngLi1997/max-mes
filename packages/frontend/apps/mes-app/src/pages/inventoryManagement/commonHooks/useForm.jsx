import { getStorageConfigTreeApi, scanWeighPositionCodeApi } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useForm = () => {
  const { showNotify } = useNotify();
  const cargoSpaceNameObj = ref({});

  const getShowName = (arr, parentName = '') => {
    arr.map((item) => {
      let name = '';
      if (parentName) {
        name = `${parentName}/`;
      }
      name += item.name;
      cargoSpaceNameObj.value[item.id] = name;
      if (item.children.length) {
        getShowName(item.children, name);
      }
    });
  };
  const formRef = ref();
  const signRef = ref();
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: '',
    loginName2: '',
    password2: '',
    userId2: '',
  });

  // 扫描货位
  const onScanSuccess = (res) => {
    if (res.startsWith('03')) {
      const code = res.substring(2);
      code && handleScan(code);
    }
    else {
      showNotify({ type: 'warning', message: t('请扫描正确的货位编号') });
    }
  };
  const handleScan = async (code) => {
    try {
      const { data } = await scanWeighPositionCodeApi({
        code,
      });
      formRef.value?.setFormModels({
        materialPositionId: data.id,
      });
    }
    catch (error) {
      showNotify({
        type: 'danger',
        message: error.message,
      });
    }
  };
  const formProps = reactive({
    schemas: [
      {
        field: 'materialPositionId',
        component: 'BMFormSelect',
        label: t('货位'),
        colProps: {
          span: 24,
        },
        required: true,
        componentProps: () => {
          return {
            request: async () => {
              const res = await getStorageConfigTreeApi();
              const data = res.data || [];
              getShowName(data);
              return data;
            },
            type: 'tree',
            title: t('选择货位'),
            fieldNames: {
              name: 'name',
              key: 'id',
              checkKey: 'level.value',
              checkKeyValue: 4,
            },
          };
        },
      },
      {
        field: 'linkExplain',
        component: 'Input',
        label: t('来源/去向'),
        required: true,
        colProps: {
          span: 24,
        },
      },
    ],
  });

  return {
    formRef,
    formProps,
    signRef,
    signValue,
    cargoSpaceNameObj,
    onScanSuccess,
  };
};
