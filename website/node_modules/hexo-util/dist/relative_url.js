"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const encode_url_1 = __importDefault(require("./encode_url"));
const cache_1 = __importDefault(require("./cache"));
const cache = new cache_1.default();
function relativeUrlHelper(from = '', to = '') {
    return cache.apply(`${from}-${to}`, () => {
        const fromParts = from.split('/');
        const toParts = to.split('/');
        const length = Math.min(fromParts.length, toParts.length);
        let i = 0;
        for (; i < length; i++) {
            if (fromParts[i] !== toParts[i])
                break;
        }
        let out = toParts.slice(i);
        for (let j = fromParts.length - i - 1; j > 0; j--) {
            out.unshift('..');
        }
        const outLength = out.length;
        // If the last 2 elements of `out` is empty strings, replace them with `index.html`.
        if (outLength > 1 && !out[outLength - 1] && !out[outLength - 2]) {
            out = out.slice(0, outLength - 2).concat('index.html');
        }
        return (0, encode_url_1.default)(out.join('/').replace(/\/{2,}/g, '/'));
    });
}
module.exports = relativeUrlHelper;
//# sourceMappingURL=relative_url.js.map