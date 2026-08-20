import { ref } from 'vue';
import { t } from "@/utils/useBmosI18n.js";

export const activeInfo = ref({
  materialMergeCode: "",
  consistenceParamName: "",
  targetConcentration: "",
})

export const infoData = ref({
  materialMergeCode: "",
  targetVolume: "",
}); //产出中间品信息展示

export const satisfied = ref(false)
