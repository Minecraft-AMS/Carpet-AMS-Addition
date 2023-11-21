"use strict";
const hexo_util_1 = require("hexo-util");
module.exports = (ctx) => {
    return function fullUrlForTag([text, path]) {
        const url = hexo_util_1.full_url_for.call(ctx, path);
        const attrs = {
            href: url
        };
        return (0, hexo_util_1.htmlTag)('a', attrs, text);
    };
};
//# sourceMappingURL=full_url_for.js.map