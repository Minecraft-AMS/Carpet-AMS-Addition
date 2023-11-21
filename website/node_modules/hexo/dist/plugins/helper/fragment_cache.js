"use strict";
const hexo_util_1 = require("hexo-util");
module.exports = (ctx) => {
    const cache = new hexo_util_1.Cache();
    // reset cache for watch mode
    ctx.on('generateBefore', () => { cache.flush(); });
    return function fragmentCache(id, fn) {
        if (this.cache)
            return cache.apply(id, fn);
        const result = fn();
        cache.set(id, result);
        return result;
    };
};
//# sourceMappingURL=fragment_cache.js.map