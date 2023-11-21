/// <reference types="node" />
/// <reference types="node" />
import { SpawnOptions } from 'child_process';
interface Options extends SpawnOptions {
    verbose?: boolean;
    encoding?: BufferEncoding;
}
declare function promiseSpawn(command: string, args?: string | string[] | Options, options?: Options): Promise<string | void | Buffer>;
export = promiseSpawn;
