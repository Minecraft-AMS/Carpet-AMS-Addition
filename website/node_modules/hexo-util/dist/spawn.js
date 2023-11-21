"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const cross_spawn_1 = __importDefault(require("cross-spawn"));
const cache_stream_1 = __importDefault(require("./cache_stream"));
class StatusError extends Error {
}
function promiseSpawn(command, args = [], options = {}) {
    if (!command)
        throw new TypeError('command is required!');
    if (typeof args === 'string')
        args = [args];
    if (!Array.isArray(args)) {
        options = args;
        args = [];
    }
    return new Promise((resolve, reject) => {
        const task = (0, cross_spawn_1.default)(command, args, options);
        const verbose = options.verbose;
        const { encoding = 'utf8' } = options;
        const stdoutCache = new cache_stream_1.default();
        const stderrCache = new cache_stream_1.default();
        if (task.stdout) {
            const stdout = task.stdout.pipe(stdoutCache);
            if (verbose)
                stdout.pipe(process.stdout);
        }
        if (task.stderr) {
            const stderr = task.stderr.pipe(stderrCache);
            if (verbose)
                stderr.pipe(process.stderr);
        }
        task.on('close', code => {
            if (code) {
                const e = new StatusError(getCache(stderrCache, encoding));
                e.code = code;
                return reject(e);
            }
            resolve(getCache(stdoutCache, encoding));
        });
        task.on('error', reject);
        // Listen to exit events if neither stdout and stderr exist (inherit stdio)
        if (!task.stdout && !task.stderr) {
            task.on('exit', code => {
                if (code) {
                    const e = new StatusError('Spawn failed');
                    e.code = code;
                    return reject(e);
                }
                resolve();
            });
        }
    });
}
function getCache(stream, encoding) {
    const buf = stream.getCache();
    stream.destroy();
    if (!encoding)
        return buf;
    return buf.toString(encoding);
}
module.exports = promiseSpawn;
//# sourceMappingURL=spawn.js.map