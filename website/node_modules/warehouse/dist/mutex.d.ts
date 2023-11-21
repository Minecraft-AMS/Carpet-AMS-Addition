declare class Mutex {
    private _locked;
    private readonly _queue;
    constructor();
    lock(fn: () => void): void;
    unlock(): void;
}
export default Mutex;
