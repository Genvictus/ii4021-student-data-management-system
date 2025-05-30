import BN from "bn.js";
import crypto from "node:crypto";

/**
 *
 * @param bits The size of the generated prime number in bits
 * @returns
 */
export async function generatePrime(bits: number): Promise<BN> {
    return new Promise((resolve, reject) => {
        crypto.generatePrime(bits, { bigint: true }, (err, prime) => {
            if (err) {
                reject(err);
            } else {
                const bn = new BN(prime.toString(10), 10);
                resolve(bn);
            }
        });
    });
}

/**
 *
 * @param bytes The size of the generated number in bytes
 * @returns
 */
export async function generateRandomNumber(bytes: number): Promise<BN> {
    return new Promise((resolve, reject) => {
        crypto.randomBytes(bytes, (err, buf) => {
            if (err) {
                reject(err);
            } else {
                const bn = new BN(buf);
                resolve(bn);
            }
        });
    });
}
