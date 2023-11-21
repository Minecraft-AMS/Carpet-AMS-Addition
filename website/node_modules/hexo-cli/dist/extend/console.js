"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
const abbrev_1 = __importDefault(require("abbrev"));
class Console {
    constructor() {
        this.store = {};
        this.alias = {};
    }
    get(name) {
        name = name.toLowerCase();
        return this.store[this.alias[name]];
    }
    list() {
        return this.store;
    }
    register(name, desc, options, fn) {
        if (!name)
            throw new TypeError('name is required');
        if (!fn) {
            if (options) {
                if (typeof options === 'function') {
                    fn = options;
                    if (typeof desc === 'object') { // name, options, fn
                        options = desc;
                        desc = '';
                    }
                    else { // name, desc, fn
                        options = {};
                    }
                }
                else {
                    throw new TypeError('fn must be a function');
                }
            }
            else {
                // name, fn
                if (typeof desc === 'function') {
                    fn = desc;
                    options = {};
                    desc = '';
                }
                else {
                    throw new TypeError('fn must be a function');
                }
            }
        }
        if (fn.length > 1) {
            fn = bluebird_1.default.promisify(fn);
        }
        else {
            fn = bluebird_1.default.method(fn);
        }
        this.store[name.toLowerCase()] = fn;
        const c = fn;
        c.options = options;
        c.desc = desc;
        this.alias = (0, abbrev_1.default)(Object.keys(this.store));
    }
}
module.exports = Console;
//# sourceMappingURL=console.js.map