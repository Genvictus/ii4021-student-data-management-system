import { expect, test, describe } from "vitest";
import { aesEncrypt, aesDecrypt, generateAesKey, AESKey } from "../src/lib/aes";

describe("aes encryption and decryption", () => {
    test("decrypt(encrypt(plaintext)) is equal to plaintext", () => {
        const secret = "strong-password";
        const key = generateAesKey(secret);
        const plaintext = "Confidential message";

        const ciphertext = aesEncrypt(plaintext, key);
        expect(ciphertext).not.toBe(plaintext);

        const decrypted = aesDecrypt(ciphertext, key);
        expect(decrypted).toBe(plaintext);
    });

    test("decrypting with wrong key fails", () => {
        const correctSecret = "correct-password";
        const correctKey = generateAesKey(correctSecret);

        const wrongSecret = "wrong-password";
        const wrongKey: AESKey = {
            key: wrongSecret,
            iv: correctKey.iv
        };

        const plaintext = "Top secret";

        const ciphertext = aesEncrypt(plaintext, correctKey);

        const decrypted = aesDecrypt(ciphertext, wrongKey);
        expect(decrypted).not.toBe(plaintext);
        expect(decrypted).toBe("");
    });
});
