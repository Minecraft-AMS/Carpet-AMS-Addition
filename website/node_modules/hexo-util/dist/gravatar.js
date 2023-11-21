"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const crypto_1 = require("crypto");
const querystring_1 = require("querystring");
const cache_1 = __importDefault(require("./cache"));
const cache = new cache_1.default();
function md5(str) {
    return (0, crypto_1.createHash)('md5').update(str).digest('hex');
}
function gravatarHelper(email, options) {
    if (typeof options === 'number') {
        options = { s: options };
    }
    const hash = cache.has(email) ? cache.get(email) : md5(email.toLowerCase());
    let str = `https://www.gravatar.com/avatar/${hash}`;
    const qs = (0, querystring_1.stringify)(options);
    if (qs)
        str += `?${qs}`;
    cache.set('email', hash);
    return str;
}
module.exports = gravatarHelper;
//# sourceMappingURL=gravatar.js.map