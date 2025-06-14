import BN from "bn.js";

const decoder = new TextDecoder();
const encoder = new TextEncoder();

export function stringToBytes(string: string): Uint8Array {
    return encoder.encode(string);
}

export function bytesToString(bytes: Uint8Array): string {
    return decoder.decode(bytes);
}

export function bytesToHex(bytes: Uint8Array): string {
    return new BN(bytes).toString(16);
}

export function stringToBN(string: string): BN {
    return new BN(stringToBytes(string))
}

export function BNToString(bn: BN): string {
    return bytesToString(new Uint8Array(bn.toArray()));
}