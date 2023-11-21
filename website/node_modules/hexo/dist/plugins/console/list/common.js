"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.stringLength = void 0;
const strip_ansi_1 = __importDefault(require("strip-ansi"));
function stringLength(str) {
    str = (0, strip_ansi_1.default)(str);
    const len = str.length;
    let result = len;
    // Detect double-byte characters
    for (let i = 0; i < len; i++) {
        if (str.charCodeAt(i) > 255) {
            result++;
        }
    }
    return result;
}
exports.stringLength = stringLength;
//# sourceMappingURL=common.js.map