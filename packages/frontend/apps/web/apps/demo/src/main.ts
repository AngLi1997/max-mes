import { createApp } from 'vue'
import App from './App.vue'
import AntD from 'ant-design-vue';

const app = createApp(App);
app.use(AntD)
app.mount('#app')