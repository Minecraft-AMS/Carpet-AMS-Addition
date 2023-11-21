"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.log = exports.inspectObject = void 0;
const util_1 = require("util");
// this format object as string, resolves circular reference
function inspectObject(object, options) {
    return (0, util_1.inspect)(object, options);
}
exports.inspectObject = inspectObject;
// wrapper to log to console
function log(...args) {
    return Reflect.apply(console.log, null, args);
}
exports.log = log;
//# sourceMappingURL=debug.js.map