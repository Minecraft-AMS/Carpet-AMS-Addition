interface Options {
    languages?: string[];
}
declare class i18n {
    data: object;
    languages: string[];
    constructor(options?: Options);
    get(languages?: string[] | string): {};
    set(lang: string, data: object): this;
    remove(lang: string): this;
    list(): string[];
    __(lang?: string[]): (key: string, ...args: any[]) => string;
    _p(lang?: string[]): (key: string, ...args: any[]) => string;
}
export = i18n;
