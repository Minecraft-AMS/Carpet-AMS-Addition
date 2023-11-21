import helpConsole from './console/help';
import initConsole from './console/init';
import versionConsole from './console/version';
declare function entry(cwd: string, args: any): any;
declare namespace entry {
    var console: {
        init: typeof initConsole;
        help: typeof helpConsole;
        version: typeof versionConsole;
    };
    var version: string;
}
export = entry;
