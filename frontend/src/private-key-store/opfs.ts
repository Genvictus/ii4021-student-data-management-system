"use client";

import {
    privateKeyToString,
    stringToPrivateKey,
    type RsaPrivateKey,
} from "@/lib/rsa";

let cachedPrivateKey: RsaPrivateKey;

export async function getPrivateKey(
    username: string
): Promise<RsaPrivateKey | undefined> {
    if (cachedPrivateKey) return cachedPrivateKey;

    try {
        const opfsRoot = await navigator.storage.getDirectory();
        const fileHandle = await opfsRoot.getFileHandle(`${username}-pk-rsa`, {
            create: false,
        });
        const file = await fileHandle.getFile();
        const text = await file.text();
        cachedPrivateKey = stringToPrivateKey(text);
        return cachedPrivateKey;
    } catch (err: any) {
        if (err.name === "NotFoundError") return;
        throw err;
    }
}

export async function setPrivateKey(
    username: string,
    privateKey: RsaPrivateKey
): Promise<void> {
    const opfsRoot = await navigator.storage.getDirectory();
    const fileHandle = await opfsRoot.getFileHandle(`${username}-pk-rsa`, {
        create: true,
    });
    const writable = await fileHandle.createWritable();
    await writable.write(privateKeyToString(privateKey));
    await writable.close();
}
