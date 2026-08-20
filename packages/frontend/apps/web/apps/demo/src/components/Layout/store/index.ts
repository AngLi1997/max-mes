import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useMenuStore = defineStore('menus', () => {
  const menus = ref<any[]>([]);

  const registerMenus = (list: any[]) => {
    console.log('registerMenus');
    
    menus.value = list;
  };

  return { menus, registerMenus };
});

