import AntD from 'ant-design-vue';
import { createApp } from 'vue';
import App from './App.vue';

const app = createApp(App);

app.use(AntD);
app.mount('#app');
