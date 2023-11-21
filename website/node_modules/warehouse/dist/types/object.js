"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const schematype_1 = __importDefault(require("../schematype"));
/**
 * Object schema type.
 */
class SchemaTypeObject extends schematype_1.default {
    /**
     *
     * @param {String} [name]
     * @param {Object} [options]
     *   @param {Boolean} [options.required=false]
     *   @param {Object|Function} [options.default={}]
     */
    constructor(name, options) {
        super(name, Object.assign({ default: {} }, options));
    }
}
exports.default = SchemaTypeObject;
//# sourceMappingURL=object.js.map