"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const error_1 = __importDefault(require("../error"));
class PopulationError extends error_1.default {
}
PopulationError.prototype.name = 'PopulationError';
exports.default = PopulationError;
//# sourceMappingURL=population.js.map