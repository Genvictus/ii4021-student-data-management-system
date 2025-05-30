import { BN } from "bn.js";
import { expect, test } from "vitest";
import {
    decrypt,
    encrypt,
    generateKeyPair,
    sign,
    verify,
} from "../src/lib/rsa";
import { generatePrime } from "../src/lib/random";
import { describe } from "node:test";

describe("rsa encryption", () => {
    test("decrypt(encrypt(plaintext)) is equal to plaintext", async () => {
        const plaintext = "Example plaintext";
        const plaintextBytes = Buffer.from(plaintext, "utf8");
        const plaintextBN = new BN(plaintextBytes);

        const p = await generatePrime(2048);
        const q = await generatePrime(2048);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const ciphertextBN = encrypt(plaintextBN, publicKey);
        expect(ciphertextBN.eq(plaintextBN)).toBe(false); // Should not match original

        const decryptedCiphertextBN = decrypt(ciphertextBN, privateKey);
        expect(plaintextBN.eq(decryptedCiphertextBN)).toBe(true); // Should match original
    });

    test("encrypt(decrypt(ciphertext)) is equal to ciphertext", async () => {
        const ciphertext = "fake encrypted value";
        const ciphertextBytes = Buffer.from(ciphertext, "utf8");
        const ciphertextBN = new BN(ciphertextBytes);

        const p = await generatePrime(2048);
        const q = await generatePrime(2048);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const decryptedBN = decrypt(ciphertextBN, privateKey);
        const reEncryptedBN = encrypt(decryptedBN, publicKey);

        expect(reEncryptedBN.eq(ciphertextBN)).toBe(true);
    });
});

describe("rsa digital signature", () => {
    test("rsa verifies authentic message", async () => {
        const message = "This is a test message";
        const messageBytes = Buffer.from(message, "utf8");
        const messageBN = new BN(messageBytes);

        const p = await generatePrime(2048);
        const q = await generatePrime(2048);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const signature = sign(messageBN, privateKey);
        const isVerified = verify(messageBN, publicKey, signature);

        expect(isVerified).toBe(true);
    });

    test("rsa fails to verify tampered message", async () => {
        const message = "This is a test message";
        const tamperedMessage = "This is a **tampered** message";
        const messageBytes = Buffer.from(message, "utf8");
        const tamperedMessageBytes = Buffer.from(tamperedMessage, "utf8");

        const messageBN = new BN(messageBytes);
        const tamperedMessageBN = new BN(tamperedMessageBytes);

        const p = await generatePrime(2048);
        const q = await generatePrime(2048);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const signature = sign(messageBN, privateKey);
        const isVerified = verify(tamperedMessageBN, publicKey, signature);

        expect(isVerified).toBe(false);
    });
});
