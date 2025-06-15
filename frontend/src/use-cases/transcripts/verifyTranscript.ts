import api from "@/axios";
import { getUserProfile } from "@/lib/getUserProfile";
import { sha3Digest, stringToPublicKey, verify, type RsaPublicKey } from "@/lib/rsa";
import { getPrivateKey } from "@/private-key-store/opfs";
import type { TranscriptSignPayload } from "@/types/TranscriptPayload";
import type { UserProfile } from "@/types/UserProfile";
import axios from "axios";
import BN from "bn.js";
import type { ResponseFormat } from "../response";
import { encryptTranscriptEntriesFromKeyString } from "./util";


type UserResponse = Omit<UserProfile, "publicKey"> & { publicKey: string }

async function getHodPublicKey(hodId: string): Promise<RsaPublicKey> {
    const response = await api.get<ResponseFormat<UserResponse>>(`api/v1/users/${hodId}`);
    return stringToPublicKey(response.data.data!.publicKey);
}

export async function verifyTranscript(transcript: TranscriptSignPayload & { hodId: string, hodDigitalSignature: string }): Promise<ResponseFormat<boolean>> {
    try {
        let hodPK: RsaPublicKey;
        if (getUserProfile()!.userId === transcript.hodId) {
            hodPK = getUserProfile()!.publicKey;
        } else {
            hodPK = await getHodPublicKey(transcript.hodId)
        }

        const selfPK = (await getPrivateKey(getUserProfile()!.email))!;

        const encryptedEntries = encryptTranscriptEntriesFromKeyString(transcript, selfPK);
        const localDigest = sha3Digest(encryptedEntries);
        const result = verify(localDigest, hodPK, new BN(transcript.hodDigitalSignature, 16));

        return {
            success: true,
            message: "verification ok",
            data: result,
        }
    } catch (error) {
        console.error(error);

        if (axios.isAxiosError(error) && error.response) {
            return error.response.data;
        }

        return {
            success: false,
            message: "An unexpected error occurred",
            data: null,
        };
    }
}