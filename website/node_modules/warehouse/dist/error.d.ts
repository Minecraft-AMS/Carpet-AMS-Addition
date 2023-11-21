declare class WarehouseError extends Error {
    code?: string;
    /**
     * WarehouseError constructor
     *
     * @param {string} msg
     * @param {string} code
     */
    constructor(msg: string, code?: string);
    static ID_EXIST: string;
    static ID_NOT_EXIST: string;
    static ID_UNDEFINED: string;
}
export default WarehouseError;
