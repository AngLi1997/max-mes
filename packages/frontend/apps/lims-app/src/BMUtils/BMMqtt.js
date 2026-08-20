import mqtt from 'mqtt/dist/mqtt.min.js'; // 引入mqtt依赖
import { arrayBufferToString, isArrayBuffer, isUint8Array, uint8ArrayToString } from './BMFunc.js';

class BMMqttClient {
  constructor(connectBaseUrl, topic, onMessage, userInfo = {}, connectCallback = () => {}) {
    this.connectBaseUrl = connectBaseUrl;
    this.topic = topic;
    this.onMessage = onMessage;
    this.userInfo = userInfo;
    this.clientId = `mqtt_${Math.random().toString(16).slice(3)}`;
    this.myOptions = {
      clean: true,
      connectTimeout: 4000,
      reconnectPeriod: 1000,
      clientId: this.clientId,
      ...this.userInfo,
    };
    this.connectCallback = connectCallback;
    this.client = mqtt.connect(`${this.connectBaseUrl}`, this.myOptions);

    this.client.on('connect', this.handleConnect.bind(this));
    this.client.on('message', this.handleMessage.bind(this));
    this.client.on('close', this.handleClose.bind(this));
  }

  handleConnect() {
    console.log('已经连接成功');
    if (this.connectCallback) {
      this.connectCallback();
    }
    this.client.subscribe([this.topic], () => {
      console.log(`订阅了主题 ${this.topic}`);
    });
  }

  handleMessage(topic, message) {
    let data;
    if (isUint8Array(message)) {
      data = uint8ArrayToString(message);
    }
    else if (isArrayBuffer(message)) {
      data = arrayBufferToString(message);
    }
    else {
      data = JSON.parse(message.toString());
    }
    console.log('返回的数据：', data);
    if (this.onMessage) {
      this.onMessage(topic, data);
    }
  }

  handlePublish(topic, message) {
    this.client.publish(topic, message);
  }

  handleClose() {
    console.log('已断开连接');
  }

  endMqtt() {
    this.client.end();
  }
}

export default BMMqttClient;
