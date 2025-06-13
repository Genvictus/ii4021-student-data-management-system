import { expect, test, describe } from "vitest";
import { aesEncrypt, aesDecrypt } from "../src/lib/aes";

describe("aes encryption and decryption", () => {
    test("decrypt(encrypt(plaintext)) is equal to plaintext", () => {
        const secretKey = "strong-password";
        const plaintext = "Confidential message";

        const ciphertext = aesEncrypt(plaintext, secretKey);
        expect(ciphertext).not.toBe(plaintext);

        const decrypted = aesDecrypt(ciphertext, secretKey);
        expect(decrypted).toBe(plaintext);
    });

    test("decrypting with wrong key fails", () => {
        const correctKey = "correct-password";
        const wrongKey = "wrong-password";
        const plaintext = "Top secret";

        const ciphertext = aesEncrypt(plaintext, correctKey);

        const decrypted = aesDecrypt(ciphertext, wrongKey);
        expect(decrypted).not.toBe(plaintext);
        expect(decrypted).toBe("");
    });
});
