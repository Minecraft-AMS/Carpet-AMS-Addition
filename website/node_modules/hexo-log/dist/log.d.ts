interface Options {
    debug?: boolean;
    silent?: boolean;
}
declare type ConsoleArgs = any[];
declare type writeLogF = (...args: ConsoleArgs) => void;
declare class Logger {
    _silent: boolean;
    _debug: boolean;
    level: number;
    d: writeLogF;
    i: writeLogF;
    w: writeLogF;
    e: writeLogF;
    log: writeLogF;
    constructor({ debug, silent }?: Options);
    _writeLogOutput(level: number, consoleArgs: ConsoleArgs): void;
    trace(...args: ConsoleArgs): void;
    debug(...args: ConsoleArgs): void;
    info(...args: ConsoleArgs): void;
    warn(...args: ConsoleArgs): void;
    error(...args: ConsoleArgs): void;
    fatal(...args: ConsoleArgs): void;
}
export default function createLogger(options?: Options): Logger;
export declare const logger: (option?: Options) => Logger;
export {};
