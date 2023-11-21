declare const _default: {
    new <T>(): {
        cache: Map<string, T>;
        set(id: string, value: T): void;
        has(id: string): boolean;
        get(id: string): T;
        del(id: string): void;
        apply(id: string, value: any): T;
        flush(): void;
        size(): number;
        dump(): {
            [k: string]: T;
        };
    };
};
export = _default;
