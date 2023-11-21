import Promise from 'bluebird';
import type Hexo from '../../hexo';
declare function cleanConsole(this: Hexo): Promise<[void, void, any]>;
export = cleanConsole;
