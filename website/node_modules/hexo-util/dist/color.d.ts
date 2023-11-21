interface RGBA {
    r: number;
    g: number;
    b: number;
    a: number;
}
declare class Color {
    r: number;
    g: number;
    b: number;
    a: number;
    /**
     * @param {string|{ r: number; g: number; b: number; a: number;}} color
     */
    constructor(color: string | Partial<RGBA>);
    /**
     * @param {string} color
     */
    _parse(color: string): void;
    toString(): string;
    /**
     * @param {string|{ r: number; g: number; b: number; a: number;}} color
     * @param {number} ratio
     */
    mix(color: RGBA, ratio: number): Color;
}
export = Color;
