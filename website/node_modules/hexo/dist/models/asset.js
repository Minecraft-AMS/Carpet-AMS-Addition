"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const warehouse_1 = __importDefault(require("warehouse"));
const path_1 = require("path");
module.exports = (ctx) => {
    const Asset = new warehouse_1.default.Schema({
        _id: { type: String, required: true },
        path: { type: String, required: true },
        modified: { type: Boolean, default: true },
        renderable: { type: Boolean, default: true }
    });
    Asset.virtual('source').get(function () {
        return (0, path_1.join)(ctx.base_dir, this._id);
    });
    return Asset;
};
//# sourceMappingURL=asset.js.map