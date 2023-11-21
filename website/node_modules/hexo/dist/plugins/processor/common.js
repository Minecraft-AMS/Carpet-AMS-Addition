"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.isMatch = exports.timezone = exports.toDate = exports.isExcludedFile = exports.isHiddenFile = exports.isTmpFile = exports.ignoreTmpAndHiddenFile = void 0;
const hexo_util_1 = require("hexo-util");
const moment_timezone_1 = __importDefault(require("moment-timezone"));
const micromatch_1 = __importDefault(require("micromatch"));
const DURATION_MINUTE = 1000 * 60;
function isMatch(path, patterns) {
    if (!patterns)
        return false;
    return micromatch_1.default.isMatch(path, patterns);
}
exports.isMatch = isMatch;
function isTmpFile(path) {
    return path.endsWith('%') || path.endsWith('~');
}
exports.isTmpFile = isTmpFile;
function isHiddenFile(path) {
    return /(^|\/)[_.]/.test(path);
}
exports.isHiddenFile = isHiddenFile;
function isExcludedFile(path, config) {
    if (isTmpFile(path))
        return true;
    if (isMatch(path, config.exclude))
        return true;
    if (isHiddenFile(path) && !isMatch(path, config.include))
        return true;
    return false;
}
exports.isExcludedFile = isExcludedFile;
exports.ignoreTmpAndHiddenFile = new hexo_util_1.Pattern(path => {
    if (isTmpFile(path) || isHiddenFile(path))
        return false;
    return true;
});
function toDate(date) {
    if (!date || moment_timezone_1.default.isMoment(date))
        return date;
    if (!(date instanceof Date)) {
        date = new Date(date);
    }
    if (isNaN(date.getTime()))
        return;
    return date;
}
exports.toDate = toDate;
function timezone(date, timezone) {
    if (moment_timezone_1.default.isMoment(date))
        date = date.toDate();
    const offset = date.getTimezoneOffset();
    const ms = date.getTime();
    const target = moment_timezone_1.default.tz.zone(timezone).utcOffset(ms);
    const diff = (offset - target) * DURATION_MINUTE;
    return new Date(ms - diff);
}
exports.timezone = timezone;
//# sourceMappingURL=common.js.map