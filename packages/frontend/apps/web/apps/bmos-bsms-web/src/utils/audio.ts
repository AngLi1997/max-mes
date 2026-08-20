import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';

// 播放语音文件
let currentAudioSource: AudioBufferSourceNode | null = null;

// 播放语音文件
export const playAudio = async (url: string) => {
  try {
    if (currentAudioSource) {
      currentAudioSource.stop();
    }

    const audioContext = new AudioContext();
    const response = await fetch(url);
    const audioBuffer = await audioContext.decodeAudioData(await response.arrayBuffer());

    currentAudioSource = audioContext.createBufferSource();
    currentAudioSource.buffer = audioBuffer;
    currentAudioSource.connect(audioContext.destination);
    currentAudioSource.start();
  } catch (_error) {
    message.error(t('语音播放失败'));
  }
};
