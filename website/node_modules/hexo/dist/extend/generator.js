"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
class Generator {
    constructor() {
        this.id = 0;
        this.store = {};
    }
    list() {
        return this.store;
    }
    get(name) {
        return this.store[name];
    }
    register(name, fn) {
        if (!fn) {
            if (typeof name === 'function') { // fn
                fn = name;
                name = `generator-${this.id++}`;
            }
            else {
                throw new TypeError('fn must be a function');
            }
        }
        if (fn.length > 1)
            fn = bluebird_1.default.promisify(fn);
        this.store[name] = bluebird_1.default.method(fn);
    }
}
module.exports = Generator;
//# sourceMappingURL=generator.js.map