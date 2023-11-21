"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const schematype_1 = __importDefault(require("../schematype"));
const cuid_1 = __importDefault(require("cuid"));
const validation_1 = __importDefault(require("../error/validation"));
/**
 * [CUID](https://github.com/ericelliott/cuid) schema type.
 */
class SchemaTypeCUID extends schematype_1.default {
    /**
     * Casts data. Returns a new CUID only if value is null and the field is
     * required.
     *
     * @param {String} value
     * @return {String}
     */
    cast(value) {
        if (value == null && this.options.required) {
            return (0, cuid_1.default)();
        }
        return value;
    }
    /**
     * Validates data. A valid CUID must be started with `c` and 25 in length.
     *
     * @param {*} value
     * @return {String|Error}
     */
    validate(value) {
        if (value && (value[0] !== 'c' || value.length !== 25)) {
            throw new validation_1.default(`\`${value}\` is not a valid CUID`);
        }
        return value;
    }
}
exports.default = SchemaTypeCUID;
//# sourceMappingURL=cuid.js.map