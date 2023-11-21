"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const help_1 = __importDefault(require("./help"));
const init_1 = __importDefault(require("./init"));
const version_1 = __importDefault(require("./version"));
module.exports = function (ctx) {
    const { console } = ctx.extend;
    console.register('help', 'Get help on a command.', {}, help_1.default);
    console.register('init', 'Create a new Hexo folder.', {
        desc: 'Create a new Hexo folder at the specified path or the current directory.',
        usage: '[destination]',
        arguments: [
            { name: 'destination', desc: 'Folder path. Initialize in current folder if not specified' }
        ],
        options: [
            { name: '--no-clone', desc: 'Copy files instead of cloning from GitHub' },
            { name: '--no-install', desc: 'Skip npm install' }
        ]
    }, init_1.default);
    console.register('version', 'Display version information.', {}, version_1.default);
};
//# sourceMappingURL=index.js.map