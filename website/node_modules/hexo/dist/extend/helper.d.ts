interface StoreFunction {
    (...args: any[]): string;
}
interface Store {
    [key: string]: StoreFunction;
}
declare class Helper {
    store: Store;
    constructor();
    /**
     * @returns {Store} - The plugin store
     */
    list(): Store;
    /**
     * Get helper plugin function by name
     * @param {String} name - The name of the helper plugin
     * @returns {StoreFunction}
     */
    get(name: string): StoreFunction;
    /**
     * Register a helper plugin
     * @param {String} name - The name of the helper plugin
     * @param {StoreFunction} fn - The helper plugin function
     */
    register(name: string, fn: StoreFunction): void;
}
export = Helper;
