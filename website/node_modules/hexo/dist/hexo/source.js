"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const box_1 = __importDefault(require("../box"));
class Source extends box_1.default {
    constructor(ctx) {
        super(ctx, ctx.source_dir);
        this.processors = ctx.extend.processor.list();
    }
}
module.exports = Source;
//# sourceMappingURL=source.js.map