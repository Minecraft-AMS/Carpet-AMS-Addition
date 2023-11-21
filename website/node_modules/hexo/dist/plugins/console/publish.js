"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const tildify_1 = __importDefault(require("tildify"));
const picocolors_1 = require("picocolors");
function publishConsole(args) {
    // Display help message if user didn't input any arguments
    if (!args._.length) {
        return this.call('help', { _: ['publish'] });
    }
    return this.post.publish({
        slug: args._.pop(),
        layout: args._.length ? args._[0] : this.config.default_layout
    }, args.r || args.replace).then(post => {
        this.log.info('Published: %s', (0, picocolors_1.magenta)((0, tildify_1.default)(post.path)));
    });
}
module.exports = publishConsole;
//# sourceMappingURL=publish.js.map