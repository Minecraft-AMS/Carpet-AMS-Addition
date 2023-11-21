import type Hexo from '../../hexo';
/**
 * Full url for tag
 *
 * Syntax:
 *   {% full_url_for text path %}
 */
declare const _default: (ctx: Hexo) => ([text, path]: [any, any]) => string;
export = _default;
