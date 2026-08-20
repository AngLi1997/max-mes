import { getImmunetypeList } from '@/services';
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useDict = defineStore('dictStore', () => {
  // 免疫类型
  const immunityTypeDict = ref<any>([]);

  // 更新免疫类型
  const setImmunityTypeDict = async () => {
    try {
      const { data } = await getImmunetypeList({ pageNum: 1, pageSize: 100 });
      if (data.list && data.list.length) {
        immunityTypeDict.value = data.list.map((item: any) => {
          return {
            label: item.immunityName,
            value: item.id,
            colour: item.colour,
          };
        });
      }
    } catch (err) {
      console.log(err);
    }
  };

  const getImmuniTypeDict = async () => {
    if (!immunityTypeDict.value.length) {
      await setImmunityTypeDict();
    }
    return immunityTypeDict.value;
  };

  // 查找免疫类型
  const findImmunityType = (id: string) => {
    return immunityTypeDict.value.find((item: any) => item.value === id);
  };

  return { immunityTypeDict, getImmuniTypeDict, findImmunityType, setImmunityTypeDict };
});
