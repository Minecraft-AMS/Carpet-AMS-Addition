type Entry = 'head_begin' | 'head_end' | 'body_begin' | 'body_end';
type Store = {
    [key in Entry]: {
        [key: string]: Set<unknown>;
    };
};
declare class Injector {
    store: Store;
    cache: any;
    page: any;
    constructor();
    list(): Store;
    get(entry: Entry, to?: string): any[];
    getText(entry: Entry, to?: string): string;
    getSize(entry: Entry): any;
    register(entry: Entry, value: string | (() => string), to?: string): void;
    _getPageType(pageLocals: any): string;
    _injector(input: string, pattern: string | RegExp, flag: Entry, isBegin: boolean, currentType: string): string;
    exec(data: string, locals?: {
        page: {};
    }): string;
}
export = Injector;
