"use strict";
const hexo_util_1 = require("hexo-util");
function imageTagHelper(path, options = {}) {
    const attrs = Object.assign({
        src: hexo_util_1.url_for.call(this, path)
    }, options);
    if (attrs.class && Array.isArray(attrs.class)) {
        attrs.class = attrs.class.join(' ');
    }
    return (0, hexo_util_1.htmlTag)('img', attrs);
}
module.exports = imageTagHelper;
//# sourceMappingURL=image_tag.js.map