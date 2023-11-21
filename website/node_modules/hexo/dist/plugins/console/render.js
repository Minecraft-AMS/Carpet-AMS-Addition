"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const path_1 = require("path");
const tildify_1 = __importDefault(require("tildify"));
const pretty_hrtime_1 = __importDefault(require("pretty-hrtime"));
const hexo_fs_1 = require("hexo-fs");
const picocolors_1 = require("picocolors");
function renderConsole(args) {
    // Display help message if user didn't input any arguments
    if (!args._.length) {
        return this.call('help', { _: 'render' });
    }
    const baseDir = this.base_dir;
    const src = (0, path_1.resolve)(baseDir, args._[0]);
    const output = args.o || args.output;
    const start = process.hrtime();
    const { log } = this;
    return this.render.render({
        path: src,
        engine: args.engine
    }).then(result => {
        if (typeof result === 'object') {
            if (args.pretty) {
                result = JSON.stringify(result, null, '  ');
            }
            else {
                result = JSON.stringify(result);
            }
        }
        if (!output)
            return console.log(result);
        const dest = (0, path_1.resolve)(baseDir, output);
        const interval = (0, pretty_hrtime_1.default)(process.hrtime(start));
        log.info('Rendered in %s: %s -> %s', (0, picocolors_1.cyan)(interval), (0, picocolors_1.magenta)((0, tildify_1.default)(src)), (0, picocolors_1.magenta)((0, tildify_1.default)(dest)));
        return (0, hexo_fs_1.writeFile)(dest, result);
    });
}
module.exports = renderConsole;
//# sourceMappingURL=render.js.map