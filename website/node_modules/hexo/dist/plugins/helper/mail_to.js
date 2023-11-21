"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const hexo_util_1 = require("hexo-util");
const moize_1 = __importDefault(require("moize"));
function mailToHelper(path, text, options = {}) {
    if (Array.isArray(path))
        path = path.join(',');
    if (!text)
        text = path;
    const attrs = Object.assign({
        href: `mailto:${path}`,
        title: text
    }, options);
    if (attrs.class && Array.isArray(attrs.class)) {
        attrs.class = attrs.class.join(' ');
    }
    const data = {};
    ['subject', 'cc', 'bcc', 'body'].forEach(i => {
        const item = attrs[i];
        if (item) {
            data[i] = Array.isArray(item) ? item.join(',') : item;
            attrs[i] = null;
        }
    });
    const querystring = new URLSearchParams(data).toString();
    if (querystring)
        attrs.href += `?${querystring}`;
    return (0, hexo_util_1.htmlTag)('a', attrs, text);
}
module.exports = (0, moize_1.default)(mailToHelper, {
    maxSize: 10,
    isDeepEqual: true
});
//# sourceMappingURL=mail_to.js.map