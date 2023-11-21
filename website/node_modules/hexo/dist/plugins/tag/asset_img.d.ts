import type Hexo from '../../hexo';
/**
 * Asset image tag
 *
 * Syntax:
 *   {% asset_img [class names] slug [width] [height] [title text [alt text]]%}
 */
declare const _default: (ctx: Hexo) => (args: string[]) => string;
export = _default;
