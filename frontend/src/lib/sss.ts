import { Polynomial } from "./polynomial";
import BN from "bn.js";
import { generatePrime, generateRandomNumber } from "./random";

export type Point = {
    x: BN;
    y: BN;
};

export type Share = {
    point: Point;
    mod: BN;
};

/**
 *
 * @param secret The secret encoded as big number (BN)
 * @param threshold The minimum amount of shares needed to reconstruct the secret
 */
export async function generatePolynomial(
    secret: BN,
    threshold: number
): Promise<Polynomial> {
    if (!Number.isInteger(threshold) || threshold <= 0) {
        throw new Error("Threshold must be an integer and greater than zero");
    }

    // The mod value needs to be larger than the secret,
    // so we generata a prime number that's several bits longer than the secret
    const secretBitLength = secret.bitLength();
    const modBitLength = secretBitLength + 16;
    const mod = await generatePrime(modBitLength);

    const polynomialCoef = [secret];

    const nextCoefPromise = [];
    for (let i = 1; i < threshold; i++) {
        const randomNumber = generateRandomNumber(32);
        nextCoefPromise.push(randomNumber);
    }

    const nextCoef = await Promise.all(nextCoefPromise);
    polynomialCoef.push(...nextCoef);

    const polynomial = new Polynomial(polynomialCoef, mod);

    return polynomial;
}

export async function generatePoint(polynomial: Polynomial): Promise<Point> {
    const x = await generateRandomNumber(32);
    const y = polynomial.f(x);

    return { x, y };
}

export async function generateShare(polynomial: Polynomial): Promise<Share> {
    const point = await generatePoint(polynomial);
    const mod = polynomial.mod;

    return { point, mod };
}

function lagrangeBasis(i: number, points: Point[], mod: BN): BN {
    let xi = points[i].x;

    let numerator = new BN(1);
    let denominator = new BN(1);

    for (let j = 0; j < points.length; j++) {
        if (i === j) continue;
        let xj = points[j].x;

        numerator = numerator.mul(xj).umod(mod); // Π xj
        denominator = denominator.mul(xj.sub(xi)).umod(mod); // Π (xj - xi)
    }

    const denominatorInv = denominator.invm(mod);
    return numerator.mul(denominatorInv).umod(mod);
}

export async function reconstructSecret(points: Point[], mod: BN): Promise<BN> {
    if (points.length === 0) {
        throw new Error("At least one point is required");
    }

    let secret = new BN(0);

    for (let i = 0; i < points.length; i++) {
        const li = lagrangeBasis(i, points, mod);
        const term = points[i].y.mul(li).umod(mod);
        secret = secret.add(term).umod(mod);
    }

    return secret;
}

export async function reconstructSecretFromShares(
    shares: Share[]
): Promise<BN> {
    if (shares.length === 0) {
        throw new Error("At least one share is required");
    }

    const mod = shares[0].mod;
    const isAllSharesModEqual = shares.every((share) => share.mod.eq(mod));

    if (!isAllSharesModEqual) {
        throw new Error("All shares must have equal mod value");
    }

    const points = shares.map((share) => share.point);
    const secret = await reconstructSecret(points, mod);

    return secret;
}
