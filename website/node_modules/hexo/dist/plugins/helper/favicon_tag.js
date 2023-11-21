"use strict";
const hexo_util_1 = require("hexo-util");
function faviconTagHelper(path) {
    return `<link rel="shortcut icon" href="${hexo_util_1.url_for.call(this, path)}">`;
}
module.exports = faviconTagHelper;
//# sourceMappingURL=favicon_tag.js.map