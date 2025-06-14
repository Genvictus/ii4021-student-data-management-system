import BN from "bn.js";
import { sha3_256 } from "js-sha3";

export type RsaPublicKey = {
    e: BN;
    n: BN;
};

export type RsaPrivateKey = {
    d: BN;
    n: BN;
};

export function publicKeyToString(publicKey: RsaPublicKey): string {
    return JSON.stringify({
        e: publicKey.e.toString(16),
        n: publicKey.n.toString(16),
    });
}

export function stringToPublicKey(text: string): RsaPublicKey {
    const { e, n } = JSON.parse(text);
    return {
        e: new BN(e, 16),
        n: new BN(n, 16),
    };
}

export function privateKeyToString(privateKey: RsaPrivateKey): string {
    return JSON.stringify({
        d: privateKey.d.toString(16),
        n: privateKey.n.toString(16),
    });
}

export function stringToPrivateKey(text: string): RsaPrivateKey {
    const { d, n } = JSON.parse(text);
    return {
        d: new BN(d, 16),
        n: new BN(n, 16),
    };
}

export type RsaKeyPair = {
    publicKey: RsaPublicKey;
    privateKey: RsaPrivateKey;
};

export function generateKeyPair(p: BN, q: BN): RsaKeyPair {
    if (p.eq(q)) {
        throw Error("p should not be equal to q");
    }

    const n = p.mul(q);

    const one = new BN("1");
    const pMinus1 = p.sub(one);
    const qMinus1 = q.sub(one);
    const totient_n = pMinus1.mul(qMinus1);

    const e: BN = new BN("65537");
    const d = e.invm(totient_n);

    const publicKey: RsaPublicKey = {
        e,
        n,
    };
    const privateKey: RsaPrivateKey = {
        d,
        n,
    };

    return { publicKey, privateKey };
}

export function encrypt(plaintext: BN, key: RsaPublicKey): BN {
    const { e, n } = key;
    const reduced = BN.red(n);
    const encrypted = plaintext.toRed(reduced).redPow(e).fromRed();
    return encrypted;
}

export function decrypt(ciphertext: BN, key: RsaPrivateKey): BN {
    const { d, n } = key;
    const reduced = BN.red(n);
    const decrypted = ciphertext.toRed(reduced).redPow(d).fromRed();
    return decrypted;
}

export function sha3Digest(message: string): BN {
    const hashHex = sha3_256(message);
    return new BN(hashHex, 16);
}

export function sign(messageDigest: BN, key: RsaPrivateKey): BN {
    return decrypt(messageDigest, key);
}

export function verify(
    messageDigest: BN,
    key: RsaPublicKey,
    signature: BN
): boolean {
    const encryptedSignature = encrypt(signature, key);
    const isVerified = messageDigest.eq(encryptedSignature);
    return isVerified;
}
