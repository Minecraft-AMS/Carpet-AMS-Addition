import { Pattern } from 'hexo-util';
import type File from '../box/file';
interface StoreFunction {
    (file: File): any;
}
type Store = {
    pattern: Pattern;
    process: StoreFunction;
}[];
type patternType = Exclude<ConstructorParameters<typeof Pattern>[0], ((str: string) => string)>;
declare class Processor {
    store: Store;
    constructor();
    list(): Store;
    register(fn: StoreFunction): void;
    register(pattern: patternType, fn: StoreFunction): void;
}
export = Processor;
