import { ref } from 'vue';

const queue = ref([]);
const quickQueue = ref([]);

// 添加监听队列
export function addListeningQueue(event, handler) {
  window.addEventListener(event, handler);
  queue.value.push({ event, handler });
}

// 清除监听队列
export function clearListeningQueue() {
    queue.value && queue.value.forEach((item) => {
    window.removeEventListener(item.event, item.handler);
  });
  queue.value = [];
}

// 添加快速监听队列
export function addQuickListeningQueue(event, handler) {
  window.addEventListener(event, handler);
  quickQueue.value.push({ event, handler });
}

// 清除快速监听队列
export function clearQuickListeningQueue() {
  quickQueue.value && quickQueue.value.forEach((item) => {
    window.removeEventListener(item.event, item.handler);
  });
  quickQueue.value = [];
}
