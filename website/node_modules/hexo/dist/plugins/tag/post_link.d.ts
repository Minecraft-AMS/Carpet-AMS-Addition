import type Hexo from '../../hexo';
/**
 * Post link tag
 *
 * Syntax:
 *   {% post_link slug | title [title] [escape] %}
 */
declare const _default: (ctx: Hexo) => (args: string[]) => string;
export = _default;
