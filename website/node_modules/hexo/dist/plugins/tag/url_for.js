"use strict";
const hexo_util_1 = require("hexo-util");
module.exports = (ctx) => {
    return function urlForTag([text, path, relative]) {
        const url = hexo_util_1.url_for.call(ctx, path, relative ? { relative: relative !== 'false' } : undefined);
        const attrs = {
            href: url
        };
        return (0, hexo_util_1.htmlTag)('a', attrs, text);
    };
};
//# sourceMappingURL=url_for.js.map