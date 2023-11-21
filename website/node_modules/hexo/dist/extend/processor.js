"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
const hexo_util_1 = require("hexo-util");
class Processor {
    constructor() {
        this.store = [];
    }
    list() {
        return this.store;
    }
    register(pattern, fn) {
        if (!fn) {
            if (typeof pattern === 'function') {
                fn = pattern;
                pattern = /(.*)/;
            }
            else {
                throw new TypeError('fn must be a function');
            }
        }
        if (fn.length > 1) {
            fn = bluebird_1.default.promisify(fn);
        }
        else {
            fn = bluebird_1.default.method(fn);
        }
        this.store.push({
            pattern: new hexo_util_1.Pattern(pattern),
            process: fn
        });
    }
}
module.exports = Processor;
//# sourceMappingURL=processor.js.map