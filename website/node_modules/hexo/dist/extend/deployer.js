"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
class Deployer {
    constructor() {
        this.store = {};
    }
    list() {
        return this.store;
    }
    get(name) {
        return this.store[name];
    }
    register(name, fn) {
        if (!name)
            throw new TypeError('name is required');
        if (typeof fn !== 'function')
            throw new TypeError('fn must be a function');
        if (fn.length > 1) {
            fn = bluebird_1.default.promisify(fn);
        }
        else {
            fn = bluebird_1.default.method(fn);
        }
        this.store[name] = fn;
    }
}
module.exports = Deployer;
//# sourceMappingURL=deployer.js.map