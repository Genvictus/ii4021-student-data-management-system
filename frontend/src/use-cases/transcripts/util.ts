import { aesEncrypt, aesDecrypt, type AESKey, aesKeyFromString } from "@/lib/aes";
import { BNToString, stringToBN } from "@/lib/converter";
import { decrypt, encrypt, type RsaPrivateKey, type RsaPublicKey } from "@/lib/rsa";
import type { TranscriptUpdatePayload } from "@/types/TranscriptPayload";
import type { TranscriptEntry, TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import BN from "bn.js";

/**
 * returns hex representation of the encrypted key
 * @param targetKey 
 * @param selfPK 
 */
export function encryptKeys(targetKey: BN, selfPK: RsaPublicKey): string {
    return encrypt(targetKey, selfPK).toString(16);
}

export function decryptKeys(encryptedKey: string, selfPK: RsaPrivateKey): BN {
    const encryptedKeyBN = new BN(encryptedKey, 16);
    return decrypt(encryptedKeyBN, selfPK);
}

export function encryptTranscriptEntries(entries: TranscriptEntry[], key: AESKey): string {
    const entriesString = JSON.stringify(entries);
    return aesEncrypt(entriesString, key);
}

export function decryptTranscriptEntries(entries: string, key: AESKey): TranscriptEntry[] {
    const decryptedString = aesDecrypt(entries, key);
    return JSON.parse(decryptedString);
}

export function toUpdatePayload(transcript: TranscriptWithStudent, selfPK?: RsaPrivateKey): TranscriptUpdatePayload {
    let transcriptData = "";
    if (selfPK) {
        const keyString = BNToString(decryptKeys(transcript.encryptedKey, selfPK))
        const key = aesKeyFromString(keyString)
        transcriptData = encryptTranscriptEntries(transcript.transcriptData, key)
    }
    return {
        ...transcript,
        encryptedTranscriptData: transcriptData,
        encryptedKeyStudent: "",
        encryptedKeySupervisor: "",
        encryptedKeyHod: ""
    }
}