"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.escapeHTML = exports.escape_html = exports.truncate = exports.wordWrap = exports.word_wrap = exports.titlecase = exports.trim = exports.stripHTML = exports.strip_html = void 0;
const hexo_util_1 = require("hexo-util");
Object.defineProperty(exports, "strip_html", { enumerable: true, get: function () { return hexo_util_1.stripHTML; } });
Object.defineProperty(exports, "stripHTML", { enumerable: true, get: function () { return hexo_util_1.stripHTML; } });
Object.defineProperty(exports, "word_wrap", { enumerable: true, get: function () { return hexo_util_1.wordWrap; } });
Object.defineProperty(exports, "wordWrap", { enumerable: true, get: function () { return hexo_util_1.wordWrap; } });
Object.defineProperty(exports, "truncate", { enumerable: true, get: function () { return hexo_util_1.truncate; } });
Object.defineProperty(exports, "escape_html", { enumerable: true, get: function () { return hexo_util_1.escapeHTML; } });
Object.defineProperty(exports, "escapeHTML", { enumerable: true, get: function () { return hexo_util_1.escapeHTML; } });
const titlecase_1 = __importDefault(require("titlecase"));
exports.titlecase = titlecase_1.default;
function trim(str) {
    return str.trim();
}
exports.trim = trim;
//# sourceMappingURL=format.js.map