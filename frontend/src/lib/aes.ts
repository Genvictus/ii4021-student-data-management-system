import CryptoJS from "crypto-js";

export function aesEncrypt(plaintext: string, secretKey: string): string {
    const ciphertext = CryptoJS.AES.encrypt(plaintext, secretKey).toString();
    return ciphertext;
}

export function aesDecrypt(ciphertext: string, secretKey: string): string {
    const bytes = CryptoJS.AES.decrypt(ciphertext, secretKey);
    const plaintext = bytes.toString(CryptoJS.enc.Utf8);
    return plaintext;
}
