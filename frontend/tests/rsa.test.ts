import { BN } from "bn.js";
import { expect, test } from "vitest";
import {
    decrypt,
    encrypt,
    generateKeyPair,
    sign,
    verify,
    sha3Digest,
} from "../src/lib/rsa";
import { generatePrime } from "../src/lib/random";
import { describe } from "node:test";

describe("rsa encryption", () => {
    test("decrypt(encrypt(plaintext)) is equal to plaintext", async () => {
        const plaintext = "Example plaintext";
        const plaintextBytes = Buffer.from(plaintext, "utf8");
        const plaintextBN = new BN(plaintextBytes);

        const p = await generatePrime(1024);
        const q = await generatePrime(1024);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const ciphertextBN = encrypt(plaintextBN, publicKey);
        expect(ciphertextBN.eq(plaintextBN)).toBe(false);

        const decryptedCiphertextBN = decrypt(ciphertextBN, privateKey);
        expect(plaintextBN.eq(decryptedCiphertextBN)).toBe(true);
    });

    test("encrypt(decrypt(ciphertext)) is equal to ciphertext", async () => {
        const ciphertext = "fake encrypted value";
        const ciphertextBytes = Buffer.from(ciphertext, "utf8");
        const ciphertextBN = new BN(ciphertextBytes);

        const p = await generatePrime(1024);
        const q = await generatePrime(1024);
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
        const digest = sha3Digest(message);

        const p = await generatePrime(1024);
        const q = await generatePrime(1024);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const signature = sign(digest, privateKey);
        const isVerified = verify(digest, publicKey, signature);

        expect(isVerified).toBe(true);
    });

    test("rsa fails to verify tampered message", async () => {
        const message = "This is a test message";
        const tamperedMessage = "This is a **tampered** message";

        const digest = sha3Digest(message);
        const tamperedDigest = sha3Digest(tamperedMessage);

        const p = await generatePrime(1024);
        const q = await generatePrime(1024);
        const keyPair = generateKeyPair(p, q);

        const { publicKey, privateKey } = keyPair;

        const signature = sign(digest, privateKey);
        const isVerified = verify(tamperedDigest, publicKey, signature);

        expect(isVerified).toBe(false);
    });
});
