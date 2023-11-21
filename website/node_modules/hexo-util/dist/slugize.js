"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const escape_diacritic_1 = __importDefault(require("./escape_diacritic"));
const escape_regexp_1 = __importDefault(require("./escape_regexp"));
// eslint-disable-next-line no-control-regex
const rControl = /[\u0000-\u001f]/g;
const rSpecial = /[\s~`!@#$%^&*()\-_+=[\]{}|\\;:"'<>,.?/]+/g;
function slugize(str, options = {}) {
    if (typeof str !== 'string')
        throw new TypeError('str must be a string!');
    const separator = options.separator || '-';
    const escapedSep = (0, escape_regexp_1.default)(separator);
    const result = (0, escape_diacritic_1.default)(str)
        // Remove control characters
        .replace(rControl, '')
        // Replace special characters
        .replace(rSpecial, separator)
        // Remove continous separators
        .replace(new RegExp(`${escapedSep}{2,}`, 'g'), separator)
        // Remove prefixing and trailing separtors
        .replace(new RegExp(`^${escapedSep}+|${escapedSep}+$`, 'g'), '');
    switch (options.transform) {
        case 1:
            return result.toLowerCase();
        case 2:
            return result.toUpperCase();
        default:
            return result;
    }
}
module.exports = slugize;
//# sourceMappingURL=slugize.js.map