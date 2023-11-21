"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const tildify_1 = __importDefault(require("tildify"));
const picocolors_1 = require("picocolors");
const path_1 = require("path");
const reservedKeys = {
    _: true,
    title: true,
    layout: true,
    slug: true,
    s: true,
    path: true,
    p: true,
    replace: true,
    r: true,
    // Global options
    config: true,
    debug: true,
    safe: true,
    silent: true
};
function newConsole(args) {
    const path = args.p || args.path;
    let title;
    if (args._.length) {
        title = args._.pop();
    }
    else if (path) {
        // Default title
        title = (0, path_1.basename)(path);
    }
    else {
        // Display help message if user didn't input any arguments
        return this.call('help', { _: ['new'] });
    }
    const data = {
        title,
        layout: args._.length ? args._[0] : this.config.default_layout,
        slug: args.s || args.slug,
        path
    };
    const keys = Object.keys(args);
    for (let i = 0, len = keys.length; i < len; i++) {
        const key = keys[i];
        if (!reservedKeys[key])
            data[key] = args[key];
    }
    return this.post.create(data, args.r || args.replace).then(post => {
        this.log.info('Created: %s', (0, picocolors_1.magenta)((0, tildify_1.default)(post.path)));
    });
}
module.exports = newConsole;
//# sourceMappingURL=new.js.map