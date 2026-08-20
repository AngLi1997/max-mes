import {
  ingredientDetail,
} from '@/api'
import {
  getCurrentCopyRecordItem,
} from '@/pages/webview/logic/fn/webViewEventCallbacks.js'
import {
  t,
} from '@/utils/useBmosI18n.js'
import {
  ref,
} from 'vue'

export const useSubTab = ({
  UseCommon,
  UseTable,
  toast,
}) => {
  const {
    paramsData,
    current,
    subDisabled,
  } = UseCommon
  const {
    materialTable,
  } = UseTable

  const splitSigning = ref([])
  // 切换侧边栏
  const change = async ({ value }) => {
    const data = splitSigning.value?.find(item => item.id === value)
    if (data) {
      current.active = data.id
      current.currentList = {
        ...data,
        ingredientPlanId: current.currentList?.ingredientPlanId,
        batchId: paramsData.value.productPlanId,
      }
      await materialTable()
    }
  }

  // 跳转处理界面(添加物料按钮)
  const toMaterial = () => {
    if (subDisabled.value)
      return toast.show(t('配料计划已完成'))
    if (current.currentList?.quantityType?.value === 2)
      return toast.show(t('物料数量类型为适量，无需添加物料批次'))
    const query = Object.keys(current.currentList)
      .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(current.currentList[key])}`)
      .join('&')
    uni.navigateTo({
      url: `/pages/businessComponents/addBatch/index?${query}`,
    })
  }
  // sub详情
  const ingredientName = ref('')
  const subDetails = async () => {
    try {
      const {
        version,
      } = getCurrentCopyRecordItem()
      const data = {
        ...paramsData.value,
        componentId: paramsData.value?.id,
        copyVersion: version,
      }
      const res = await ingredientDetail(data)
      splitSigning.value = res.data.materialList
      ingredientName.value = res.data.name
      current.currentList = {
        ...res.data?.materialList[0],
        ingredientPlanId: res.data?.id,
        batchId: paramsData.value.productPlanId,
      }
      subDisabled.value = res.data.completed || false
      current.active = res.data?.materialList[0]?.id// 默认选中侧边栏第一个
      await materialTable()
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      })
    }
  }
  return {
    splitSigning,
    change,
    toMaterial,
    subDetails,
    ingredientName,
  }
}
