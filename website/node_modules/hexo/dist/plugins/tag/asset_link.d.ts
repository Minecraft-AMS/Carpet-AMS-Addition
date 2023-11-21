import type Hexo from '../../hexo';
/**
 * Asset link tag
 *
 * Syntax:
 *   {% asset_link slug [title] [escape] %}
 */
declare const _default: (ctx: Hexo) => (args: string[]) => string;
export = _default;
