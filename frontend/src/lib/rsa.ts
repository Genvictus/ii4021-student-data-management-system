import BN from 'bn.js';
import { generatePrimeSync } from 'crypto';

type RsaPublicKey = {
    e: BN,
    n: BN
}

type RsaPrivateKey = {
    d: BN,
    n: BN
}

type RsaKeyPair = {
    publicKey: RsaPublicKey,
    privateKey: RsaPrivateKey
}

export function generateKeyPair(p: BN, q: BN): RsaKeyPair {
    if (p.eq(q)) {
        throw Error("p should not be equal to q")
    }

    const n = p.mul(q)
    
    const one = new BN("1");
    const pMinus1 = p.sub(one);
    const qMinus1 = q.sub(one);
    const totient_n = pMinus1.mul(qMinus1);

    const e: BN = new BN("65537") // TODO: decide on this later
    const d = e.invm(totient_n)

    const publicKey: RsaPublicKey = {
        e, n
    }
    const privateKey: RsaPrivateKey = {
        d, n
    }

    return {publicKey, privateKey}
}

export function encrypt(plaintext: BN, key: RsaPublicKey): BN {
    const {e, n} = key
    const reduced = BN.red(n)
    const encrypted = plaintext.toRed(reduced).redPow(e).fromRed()
    return encrypted
}

export function decrypt(ciphertext: BN, key: RsaPrivateKey): BN {
    const {d, n} = key
    const reduced = BN.red(n);
    const decrypted = ciphertext.toRed(reduced).redPow(d).fromRed();
    return decrypted
}

export function sign(messageDigest: BN, key: RsaPrivateKey): BN {
    return decrypt(messageDigest, key);
}

export function verify(messageDigest: BN, key: RsaPublicKey, signature: BN): boolean {
    const encryptedSignature = encrypt(signature, key)
    const isVerified = messageDigest.eq(encryptedSignature)
    return isVerified
}



