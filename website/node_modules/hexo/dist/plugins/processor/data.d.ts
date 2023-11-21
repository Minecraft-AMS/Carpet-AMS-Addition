import { Pattern } from 'hexo-util';
import type Hexo from '../../hexo';
import type { _File } from '../../box';
declare const _default: (ctx: Hexo) => {
    pattern: Pattern;
    process: (file: _File) => any;
};
export = _default;
