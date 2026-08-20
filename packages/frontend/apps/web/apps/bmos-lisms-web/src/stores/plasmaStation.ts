import { getPlasmaStationList } from '@/services';
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const usePlasmaStation = defineStore('plasmaStation', () => {
  const plasmaStations = ref<any>([]); // 浆站信息列表

  // 更新浆站列表
  const setPlasmaStation = async () => {
    try {
      const res = await getPlasmaStationList();
      if (res.data && res.data.length) {
        plasmaStations.value = res.data.map((item: any) => {
          return {
            label: item.shorterName,
            value: item.stationCode,
          };
        });
      }
    } catch (err) {
      console.log(err);
    }
  };

  const getPlasmaStations = async () => {
    if (!plasmaStations.value.length) {
      await setPlasmaStation();
    }
    return plasmaStations.value;
  };

  return { plasmaStations, getPlasmaStations, setPlasmaStation };
});
