/// <reference types="node" />
/// <reference types="node" />
import crypto from 'crypto';
declare function createSha1Hash(): crypto.Hash;
declare function hash(content: crypto.BinaryLike): Buffer;
export { hash, createSha1Hash };
