import { expect, test } from "vitest";
import {
    RC4,
} from "../src/lib/rc4";
import { describe } from "node:test";

describe("RC4 encryption", () => {
    const encoder = new TextEncoder();
    const decoder = new TextDecoder();

    test("decryption with same plaintext and key is deterministic", async () => {
        const key = "secret_key";
        const plaintext = "Example plaintext";
        const plaintextBytes = encoder.encode(plaintext);

        const firstRC4 = new RC4(key);
        const ciphertext1 = firstRC4.encrypt(plaintextBytes);

        const secondRC4 = new RC4(key);
        const ciphertext2 = secondRC4.encrypt(plaintextBytes);

        expect(ciphertext1).toStrictEqual(ciphertext2);
    })

    test("decrypt(encrypt(plaintext)) is equal to plaintext", async () => {
        const key = "secret_key";
        const plaintext = "Example plaintext";
        const plaintextBytes = encoder.encode(plaintext);

        const rc4 = new RC4(key);

        const ciphertextBytes = rc4.encrypt(plaintextBytes);
        expect(ciphertextBytes).not.toStrictEqual(plaintextBytes); // Should not match original

        const decryptedCiphertextBytes = rc4.decrypt(ciphertextBytes);
        expect(decryptedCiphertextBytes).toStrictEqual(plaintextBytes); // Should match original
    });

    test("encrypt(decrypt(ciphertext)) is equal to ciphertext", async () => {
        const key = "secret_key";
        const ciphertext = "fake encrypted value";
        const ciphertextBytes = encoder.encode(ciphertext);

        const rc4 = new RC4(key)

        const plaintextBytes = rc4.decrypt(ciphertextBytes);
        const encryptedPlaintextBytes = rc4.encrypt(plaintextBytes);
        expect(encryptedPlaintextBytes).toStrictEqual(ciphertextBytes); // Should match original
    });
});