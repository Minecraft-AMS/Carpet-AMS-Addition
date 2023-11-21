"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const error_1 = __importDefault(require("../error"));
class ValidationError extends error_1.default {
}
ValidationError.prototype.name = 'ValidationError';
exports.default = ValidationError;
//# sourceMappingURL=validation.js.map