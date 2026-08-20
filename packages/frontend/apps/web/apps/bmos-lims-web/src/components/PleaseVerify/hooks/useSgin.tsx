import NotStarted from '@/assets/images/notStarted.png'
import InProcess from '@/assets/images/inProcess.png'
import Complete from '@/assets/images/complete.png'
import Stop from '@/assets/images/stop.png'
import { ref } from 'vue'
import {
  CHECK_STATUS
} from '@/utils/enum'

export const useSgin = ({params}) => {
  const signOpen = ref(false)

  const signData = ref({})

  const signModalProps = ref({
    title: t('检验终止'),
    extraSchemas: [
      {
        field: 'reason',
        label: t('原因'),
        component: 'Input',
        required: true,
      }
    ],
    signatureAction: 22
  })

  const signatureDataFn = (formModal: any) => {
    if(Array.isArray(params.value)) {
      signData.value = [...params.value]
    }else {
      signData.value = {
        ...params.value,
        ...formModal,
        loginName: undefined,
        password: undefined,
      }
    }
    return JSON.stringify(signData.value);
  }

  return {
    signOpen,
    signModalProps,
    signatureDataFn,
    signData,
  }
}