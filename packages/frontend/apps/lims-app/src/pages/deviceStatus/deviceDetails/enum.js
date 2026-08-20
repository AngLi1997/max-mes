import { t } from '@/utils/useBmosI18n';

export const currentState = Object.freeze({
  1: {
    name: t('可用'),
    hex: 'success',
  },
  2: {
    name: t('不可用'),
    hex: 'warning',
  },
  3: {
    name: t('占用'),
    hex: 'primary',
  },
  4: {
    name: t('故障'),
    hex: 'error',
  },
});
export const buttonState = Object.freeze({
  1: {
    name: t('可用'),
    hex: 'release',
    signatureAction: 62,
  },
  2: {
    name: t('占用'),
    hex: 'occupation',
    signatureAction: 63,
  },
  3: {
    name: t('故障'),
    hex: 'fault',
    signatureAction: 61,
  },
  4: {
    name: t('恢复'),
    hex: 'recovery',
    signatureAction: 62,
  },
  5: {
    name: t('状态'),
    hex: 'state',
  },
});

export const stateEquipment = Object.freeze({
  CLEAN_001: 58, // 清洁
  DISINFECT_002: 59, // 消毒
  CALIBRATION_003: 60, // 校准
});
