"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || function (mod) {
    if (mod && mod.__esModule) return mod;
    var result = {};
    if (mod != null) for (var k in mod) if (k !== "default" && Object.prototype.hasOwnProperty.call(mod, k)) __createBinding(result, mod, k);
    __setModuleDefault(result, mod);
    return result;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.source = void 0;
const hexo_util_1 = require("hexo-util");
const common = __importStar(require("../../plugins/processor/common"));
function process(file) {
    const Asset = this.model('Asset');
    const id = file.source.substring(this.base_dir.length).replace(/\\/g, '/');
    const { path } = file.params;
    const doc = Asset.findById(id);
    if (file.type === 'delete') {
        if (doc) {
            return doc.remove();
        }
        return;
    }
    return Asset.save({
        _id: id,
        path,
        modified: file.type !== 'skip'
    });
}
const pattern = new hexo_util_1.Pattern(path => {
    if (!path.startsWith('source/'))
        return false;
    path = path.substring(7);
    if (common.isHiddenFile(path) || common.isTmpFile(path) || path.includes('node_modules'))
        return false;
    return { path };
});
exports.source = {
    pattern,
    process
};
//# sourceMappingURL=source.js.map