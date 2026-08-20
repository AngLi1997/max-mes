import { setStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useTabbarStore = defineStore('tabbar', () => {
  const showFlag = ref(true);
  const tabBars = ref([
    {
      pagePath: 'pages/home/kanban/index',
      iconPath: '/static/tabBarIcon/Kanban.svg',
      selectedIconPath: '/static/tabBarIcon/AKanban.svg',
      text: t('看板'),
      show: false,
      id: 3,
    },
    {
      pagePath: 'pages/home/todo/index',
      iconPath: '/static/tabBarIcon/Todo.svg',
      selectedIconPath: '/static/tabBarIcon/ATodo.svg',
      text: t('待办'),
      show: true,
      id: 0,
    },
    {
      pagePath: 'pages/home/workbench/index',
      iconPath: '/static/tabBarIcon/WorkBench.svg',
      selectedIconPath: '/static/tabBarIcon/AWorkBench.svg',
      text: t('工作台'),
      show: true,
      id: 1,
    },
    {
      pagePath: 'pages/home/personal/index',
      iconPath: '/static/tabBarIcon/Personal.svg',
      selectedIconPath: '/static/tabBarIcon/APersonal.svg',
      text: t('个人'),
      show: true,
      id: 2,
    },
  ]);

  const selectedId = ref(0);
  // 待办总数
  const todoCount = ref(0);

  function setSelectedId(index) {
    selectedId.value = index;
  }

  function setTodoCount(count) {
    todoCount.value = count;
  }

  const updateTabBars = (values) => {
    tabBars.value = tabBars.value.map((item) => {
      const newItem = values.find(v => v.id === item.id);
      return newItem || item;
    });
    setStorageSync('tabBars', tabBars.value.map((item) => {
      return {
        id: item.id,
        show: item.show,
      };
    }));
  };

  const updateTabBarShow = (arr = []) => {
    tabBars.value = tabBars.value.map((item) => {
      const newItem = arr?.find(v => v.id === item.id);
      return {
        ...item,
        ...(newItem && {
          show: newItem?.show,
        }),
      };
    });
  };

  return { showFlag, selectedId, todoCount, tabBars, updateTabBarShow, setSelectedId, setTodoCount, updateTabBars };
});
