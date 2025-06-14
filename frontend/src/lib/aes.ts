import BN from "bn.js";
import CryptoJS from "crypto-js";
import { bytesToHex } from "./converter";

export type AESKey = {
    key: string,
    iv: Uint8Array
}

function bytesToCryptoArray(bytes: Uint8Array): CryptoJS.lib.WordArray {
    const hex = bytesToHex(bytes);
    return CryptoJS.enc.Hex.parse(hex);
}

export function aesEncrypt(plaintext: string, key: AESKey): string {
    const ciphertext = CryptoJS.AES.encrypt(plaintext, key.key, { iv: bytesToCryptoArray(key.iv) }).toString();
    return ciphertext;
}

export function aesDecrypt(ciphertext: string, key: AESKey): string {
    const bytes = CryptoJS.AES.decrypt(ciphertext, key.key, { iv: bytesToCryptoArray(key.iv) });
    const plaintext = bytes.toString(CryptoJS.enc.Utf8);
    return plaintext;
}

export function aesKeyToString(aesKey: AESKey): string {
    const ivString = new BN(aesKey.iv).toString(16);
    return JSON.stringify({
        key: aesKey.key,
        iv: ivString
    });
}

export function aesKeyFromString(keyString: string): AESKey {
    const { key, iv } = JSON.parse(keyString);
    const ivBytes = new Uint8Array(new BN(iv, 16).toArray())
    return {
        key: key,
        iv: ivBytes
    };
}

export function generateAesKey(key: string): AESKey {
    const iv = crypto.getRandomValues(new Uint8Array(16))
    return {
        key: key,
        iv: iv
    }
}