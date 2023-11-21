"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const warehouse_1 = __importDefault(require("warehouse"));
module.exports = (ctx) => {
    const Data = new warehouse_1.default.Schema({
        _id: { type: String, required: true },
        data: Object
    });
    return Data;
};
//# sourceMappingURL=data.js.map