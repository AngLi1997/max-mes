import { Position } from '../type';
class MarkArrOperation {
  markArr;
  constructor(arr: Position[] = []) {
    this.markArr = new Map();
    if (arr.length === 0) return;
    arr.forEach(item => {
      this.markArr.set(`${item.r}${item.c}`, { r: item.r, c: item.c });
    });
  }
  addMarkArr(targets: Array<Position>|Position) {
    if (Array.isArray(targets)) {
      targets.forEach(target => {
        this.setValue(`${target.r}${target.c}`, { r: target.r, c: target.c });
      });
    } else {
      this.setValue(`${targets.r}${targets.c}`, { r: targets.r, c: targets.c });
    }
  }
  setValue(key: string, value: any) {
    if (!this.has(key)) {
      this.markArr.set(key, value);
    }
  }
  deleteMarkArr(targets: Array<Position> | Position) {
    if (targets instanceof Array) {
      targets.forEach((target: Position) => {
        this.markArr.delete(`${target.r}${target.c}`);
      });
    } else {
      this.markArr.delete(`${targets.r}${targets.c}`);
    }
  }
  clear() {
    this.markArr.clear();
  }
  has(key: string) {
    return this.markArr.has(key);
  }
  values() {
    return this.markArr.values();
  }
}

let mark: MarkArrOperation | null = null;
const initMark = (arr = []) => {
  if (!mark) {
    mark = new MarkArrOperation(arr);
  }
  const destroy = () => {
    if (mark) {
      mark.clear();
    }
  };
  return {
    destroy,
    markArrOperation: mark,
  };
};

export default initMark;
