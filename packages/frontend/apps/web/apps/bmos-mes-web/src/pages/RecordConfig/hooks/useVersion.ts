import { t } from '@bmos/i18n';
import { RadioChangeEvent } from 'ant-design-vue';
import { computed, ref } from 'vue';
import { METHOD } from '../enum';
export const useVersion = () => {
  const METHOD_S = ref<number>();
  const versionDefaultValue = ref<Record<string, any>>({
    method: METHOD.COPY,
  });

  const VersionFormProps = computed(() => {
    const schema = [
      {
        field: 'name',
        component: 'Input',
        label: t('版本号'),
        required: true,
      },
      {
        field: 'method',
        component: 'Input',
        label: t('方式'),
        required: true,
        slot: 'VERSION_METHOD',
      },
    ];
    if (METHOD_S.value === METHOD.COPY) {
      schema.push({
        field: 'test1',
        component: 'Input',
        label: t('复制已有版本'),
        required: true,
      });
    } else {
      schema.push({
        field: 'test1',
        component: 'Input',
        label: t('重新上传记录'),
        required: false,
        slot: 'VERSION_UPLOAD',
      });
    }
    return [
      ...schema,
      {
        field: 'test2',
        component: 'Input',
        label: t('备注'),
      },
    ];
  });

  const RadioGroupChange = (e: RadioChangeEvent) => {
    METHOD_S.value = e.target.value;
    versionDefaultValue.value.method = e.target.value;
  };

  return { VersionFormProps, RadioGroupChange, versionDefaultValue };
};
