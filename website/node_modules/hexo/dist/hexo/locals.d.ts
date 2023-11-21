declare class Locals {
    cache: any;
    getters: any;
    constructor();
    get(name: string): any;
    set(name: string, value: any): this;
    remove(name: string): this;
    invalidate(): this;
    toObject(): {};
}
export = Locals;
