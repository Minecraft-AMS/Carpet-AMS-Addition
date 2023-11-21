"use strict";
const url_1 = require("url");
const querystring_1 = require("querystring");
const encodeURL = (str) => {
    if ((0, url_1.parse)(str).protocol) {
        const parsed = new URL(str);
        // Exit if input is a data url
        if (parsed.origin === 'null')
            return str;
        parsed.search = encodeURI((0, querystring_1.unescape)(parsed.search));
        // preserve IDN
        return (0, url_1.format)(parsed, { unicode: true });
    }
    return encodeURI((0, querystring_1.unescape)(str));
};
module.exports = encodeURL;
//# sourceMappingURL=encode_url.js.map