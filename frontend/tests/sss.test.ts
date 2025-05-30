import { BN } from "bn.js";
import { describe, expect, test } from "vitest";
import { Polynomial } from "../src/lib/polynomial";
import {
    generatePolynomial,
    reconstructSecret,
    Share,
    generateShare,
    reconstructSecretFromShares,
} from "../src/lib/sss";
import { generateRandomNumber } from "../src/lib/random";

describe("Secret reconstruction", () => {
    test("Can reconstruct secret correctly (basic)", async () => {
        const mod = new BN("1973");
        const secret = new BN("1954");
        const polynomial = new Polynomial(
            [secret, new BN("43"), new BN("12")],
            mod
        );

        const xs = [1, 2, 3, 4].map((n) => new BN(n));
        const points = xs.map((x) => ({ x, y: polynomial.f(x) }));

        const reconstructed = await reconstructSecret(points, mod);
        expect(reconstructed.eq(secret)).toBe(true);
    });

    test("Reconstructs with exact threshold points", async () => {
        const secret = new BN("123");
        const threshold = 3;
        const mod = new BN("1009");
        const polynomial = new Polynomial(
            [secret, new BN("5"), new BN("7")],
            mod
        );

        const xs = [1, 2, 3].map((n) => new BN(n));
        const points = xs.map((x) => ({ x, y: polynomial.f(x) }));

        const reconstructed = await reconstructSecret(points, mod);
        expect(reconstructed.eq(secret)).toBe(true);
    });

    test("Reconstructs with more than threshold points", async () => {
        const secret = new BN("42");
        const threshold = 2;
        const mod = new BN("101");
        const polynomial = new Polynomial([secret, new BN("3")], mod);

        const xs = [1, 2, 3, 4].map((n) => new BN(n));
        const points = xs.map((x) => ({ x, y: polynomial.f(x) }));

        const reconstructed = await reconstructSecret(points, mod);
        expect(reconstructed.eq(secret)).toBe(true);
    });

    test("Fails (wrong result) if fewer than threshold points", async () => {
        const secret = new BN("555");
        const threshold = 3;
        const mod = new BN("787");
        const polynomial = new Polynomial(
            [secret, new BN("12"), new BN("34")],
            mod
        );

        const xs = [1, 2].map((n) => new BN(n)); // only 2 < threshold
        const points = xs.map((x) => ({ x, y: polynomial.f(x) }));

        const reconstructed = await reconstructSecret(points, mod);
        expect(reconstructed.eq(secret)).toBe(false);
    });

    test("Throws error if no points", async () => {
        const mod = new BN("101");
        await expect(reconstructSecret([], mod)).rejects.toThrow(
            "At least one point is required"
        );
    });

    test("Throws if threshold is invalid", async () => {
        const secret = new BN("42");

        await expect(generatePolynomial(secret, 0)).rejects.toThrow();
        await expect(generatePolynomial(secret, -1)).rejects.toThrow();
        await expect(generatePolynomial(secret, 2.5)).rejects.toThrow();
    });

    test("Reconstructs with random mod and random coefficients", async () => {
        const secret = new BN("99");
        const mod = new BN("1009");
        const polynomial = new Polynomial(
            [secret, new BN("17"), new BN("23"), new BN("5")],
            mod
        );

        const xs = [1, 2, 3, 4].map((n) => new BN(n));
        const points = xs.map((x) => ({ x, y: polynomial.f(x) }));

        const reconstructed = await reconstructSecret(points, mod);
        expect(reconstructed.eq(secret)).toBe(true);
    });
});

describe("Secret Sharing full flow", async () => {
    test("Generate polynomial, generate shares, reconstruct secret", async () => {
        const secret = await generateRandomNumber(32);
        const threshold = 5;

        const polynomial = await generatePolynomial(secret, threshold);

        const shares: Share[] = [];
        for (let i = 0; i < threshold; i++) {
            const pt = await generateShare(polynomial);
            shares.push(pt);
        }

        const reconstructed = await reconstructSecretFromShares(shares);

        expect(reconstructed.eq(secret)).toBe(true);
    });
});
