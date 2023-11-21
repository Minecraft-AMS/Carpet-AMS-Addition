"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const deepmerge_1 = __importDefault(require("deepmerge"));
const arrayMerge = (target, source, options) => {
    const destination = target.slice();
    source.forEach((item, index) => {
        if (typeof destination[index] === 'undefined') {
            destination[index] = options.cloneUnlessOtherwiseSpecified(item, options);
        }
        else if (options.isMergeableObject(item)) {
            destination[index] = (0, deepmerge_1.default)(target[index], item, options);
        }
        else if (!target.includes(item)) {
            destination.push(item);
        }
    });
    return destination;
};
function deepMerge(target, source) {
    return (0, deepmerge_1.default)(target, source, { arrayMerge });
}
module.exports = deepMerge;
//# sourceMappingURL=deep_merge.js.map