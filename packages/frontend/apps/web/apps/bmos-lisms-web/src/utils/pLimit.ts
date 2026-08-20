type Fn<T> = (...args: any[]) => Promise<T>;
type Resolve<T> = (value: T | PromiseLike<T>) => void;

export class PLimit {
  private concurrency: number;
  private activeCount: number;
  private queue: Array<() => void>;

  constructor(concurrency: number) {
    this.concurrency = concurrency;
    this.activeCount = 0;
    this.queue = [];
  }

  next() {
    this.activeCount--;
    if (this.queue.length > 0) {
      const task = this.queue.shift();
      task && task();
    }
  }

  async run<T>(fn: Fn<T>, resolve: Resolve<T>, args: any) {
    this.activeCount++;
    const result = (async () => fn(...args))();
    resolve(result);
    try {
      await result;
    } catch {}
    this.next();
  }

  enqueue<T>(fn: Fn<T>, resolve: Resolve<T>, args: any) {
    this.queue.push(this.run.bind(this, fn, resolve, args));
    console.log('入列', this.activeCount);
    (async () => {
      await Promise.resolve();
      console.log('出列', this.activeCount);
      if (this.activeCount < this.concurrency && this.queue.length > 0) {
        const task = this.queue.shift();
        task && task();
      }
    })();
  }

  createTask<T>(fn: Fn<T>, ...args: any): Promise<T> {
    console.log('创建任务');
    return new Promise((resolve: Resolve<T>) => {
      this.enqueue(fn, resolve, args);
    });
  }
}
