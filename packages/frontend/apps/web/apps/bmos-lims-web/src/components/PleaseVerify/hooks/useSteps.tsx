import NotStarted from '@/assets/images/notStarted.png'
import InProcess from '@/assets/images/inProcess.png'
import Complete from '@/assets/images/complete.png'
import Stop from '@/assets/images/stop.png'
import { ref } from 'vue'

export const useSteps = ({ props }) => {
  const stepItems = ref<any>([])

  return {
    stepItems
  }
}