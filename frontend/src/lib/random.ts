import forge from "node-forge";
import BN from "bn.js";

/**
 * Generates a probable prime number of the specified bit length,
 * using browser-compatible node-forge.
 *
 * @param bits Number of bits (e.g. 512, 1024)
 * @returns BN instance of the prime
 */
export async function generatePrime(bits: number): Promise<BN> {
    return new Promise((resolve, reject) => {
        // @ts-ignore
        forge.prime.generateProbablePrime(bits, (err, num) => {
            if (err) reject(err);
            else {
                const primeBN = new BN(num.toString(10), 10);
                resolve(primeBN);
            }
        });
    });
}

/**
 * Generates a cryptographically secure random number of the given byte size.
 *
 * @param bytes The size of the number in bytes (e.g. 32 for 256-bit)
 * @returns BN instance of the random number
 */
export async function generateRandomNumber(bytes: number): Promise<BN> {
    const randomBytes = forge.random.getBytesSync(bytes); // returns raw binary string
    const hex = forge.util.bytesToHex(randomBytes); // convert to hex
    return new BN(hex, 16); // create BN from hex
}
