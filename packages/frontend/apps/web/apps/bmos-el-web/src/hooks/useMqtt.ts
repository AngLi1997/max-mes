import mqtt, { MqttClient as Client, IClientOptions } from 'mqtt'; // 引入mqtt依赖

type OnMessageCallback = (topic: string, message: any, otherInfo: any) => void;

class MqttClient {
  connectBaseUrl: string;
  topic: string;
  onMessage: OnMessageCallback;
  userInfo: any;
  otherInfo: any;
  clientId: string;
  myOptions: IClientOptions;
  client: Client;

  constructor(connectBaseUrl: string, topic: string, onMessage: OnMessageCallback, userInfo = {}, otherInfo = {}) {
    this.connectBaseUrl = connectBaseUrl;
    this.topic = topic;
    this.onMessage = onMessage;
    this.userInfo = userInfo;
    this.otherInfo = otherInfo;
    this.clientId = `mqtt_${Math.random().toString(16).slice(3)}`;
    this.myOptions = {
      clean: true,
      connectTimeout: 4000,
      reconnectPeriod: 1000,
      clientId: this.clientId,
      ...this.userInfo,
    };
    this.client = mqtt.connect(`${this.connectBaseUrl}`, this.myOptions);

    this.client.on('connect', this.handleConnect.bind(this));
    this.client.on('message', this.handleMessage.bind(this));
    this.client.on('close', this.handleClose.bind(this));
  }

  handleConnect() {
    console.log('已经连接成功');
    this.client.subscribe([this.topic], () => {
      console.log(`订阅了主题 ${this.topic}`);
    });
  }

  handleMessage(topic: string, message: Buffer) {
    const data = JSON.parse(message.toString());
    console.log('返回的数据：', data);
    if (this.onMessage) {
      this.onMessage(topic, data, this.otherInfo);
    }
  }

  handlePublish(topic: string, message: string) {
    this.client.publish(topic, message);
  }

  handleClose() {
    console.log('已断开连接');
  }

  endMqtt() {
    this.client.end();
  }
}

export default MqttClient;
