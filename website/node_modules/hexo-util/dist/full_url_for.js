"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const url_1 = require("url");
const encode_url_1 = __importDefault(require("./encode_url"));
const pretty_urls_1 = __importDefault(require("./pretty_urls"));
const cache_1 = __importDefault(require("./cache"));
const cache = new cache_1.default();
function fullUrlForHelper(path = '/') {
    const { config } = this;
    const prettyUrlsOptions = Object.assign({
        trailing_index: true,
        trailing_html: true
    }, config.pretty_urls);
    // cacheId is designed to works across different hexo.config & options
    return cache.apply(`${config.url}-${prettyUrlsOptions.trailing_index}-${prettyUrlsOptions.trailing_html}-${path}`, () => {
        if (/^(\/\/|http(s)?:)/.test(path))
            return path;
        const sitehost = (0, url_1.parse)(config.url).hostname || config.url;
        const data = new URL(path, `http://${sitehost}`);
        // Exit if input is an external link or a data url
        if (data.hostname !== sitehost || data.origin === 'null')
            return path;
        path = (0, encode_url_1.default)(config.url + `/${path}`.replace(/\/{2,}/g, '/'));
        path = (0, pretty_urls_1.default)(path, prettyUrlsOptions);
        return path;
    });
}
module.exports = fullUrlForHelper;
//# sourceMappingURL=full_url_for.js.map