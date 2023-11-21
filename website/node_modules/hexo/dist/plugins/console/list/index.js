"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const abbrev_1 = __importDefault(require("abbrev"));
const page_1 = __importDefault(require("./page"));
const post_1 = __importDefault(require("./post"));
const route_1 = __importDefault(require("./route"));
const tag_1 = __importDefault(require("./tag"));
const category_1 = __importDefault(require("./category"));
const store = {
    page: page_1.default, post: post_1.default, route: route_1.default, tag: tag_1.default, category: category_1.default
};
const alias = (0, abbrev_1.default)(Object.keys(store));
function listConsole(args) {
    const type = args._.shift();
    // Display help message if user didn't input any arguments
    if (!type || !alias[type]) {
        return this.call('help', { _: ['list'] });
    }
    return this.load().then(() => Reflect.apply(store[alias[type]], this, [args]));
}
module.exports = listConsole;
//# sourceMappingURL=index.js.map