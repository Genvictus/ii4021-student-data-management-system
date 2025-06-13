class ByteStreamGenerator {
    private K: Uint8Array;
    private i: number;
    private j: number;

    /**
     * byteStreamGenerator uses ES6 generators which will be 'XOR-ed' to encrypt and decrypt
     * @param {number[]} K, the array generated from the keySetup
     */
    constructor(K: Uint8Array) {
        this.K = new Uint8Array(K);
        this.i = 0;
        this.j = 0;
    }

    /**
     * @returns {number} the current value which will be 'XOR-ed' to encrypt or decrypt
     */
    next(): number {
        this.i = (this.i + 1) % 256;
        this.j = (this.j + this.K[this.i]) % 256;
        [this.K[this.i], this.K[this.j]] = [this.K[this.j], this.K[this.i]];
        return (this.K[(this.K[this.i] + this.K[this.j]) % 256]);
    }
}

export class RC4 {
    private privateKey: Uint8Array

    constructor(key: string) {
        this.privateKey = RC4.keySetup(key);
    }

    /**
     * encrypts the input bytes
     * @param  {Uint8Array} input the bytes to encrypt
     * @return {Uint8Array}, the encrypted bytes
     */
    encrypt(input: Uint8Array): Uint8Array {
        return this.map(input);
    }

    /**
     * Decrypts the input bytes
     * @param  {Uint8Array} input the bytes to decrypt
     * @return {Uint8Array}, the decrypted bytes
     */
    decrypt(input: Uint8Array): Uint8Array {
        return this.map(input);
    }

    private map(input: Uint8Array): Uint8Array {
        const byteStream = new ByteStreamGenerator(this.privateKey);
        return input.map(b => {
            return b ^ byteStream.next();
        })
    }

    /**
     * Converts the text into an array of the characters numeric Unicode values
     * @param  {String} text, the text to convert
     * @return {Array} the array of Unicode values
     */
    private static convert(text: string): Uint8Array {
        const encoder = new TextEncoder();
        const codes = encoder.encode(text);

        return codes;
    }

    /**
     * Sets up the key to use with the byte stream (KSA step)
     * @param  {String} key, The key that you want to use
     * @return {Array}, the key stream which with be used in the byteStreamGenerator
     */
    private static keySetup(key: string): Uint8Array {

        let K = [...Array(256).keys()],
            j = 0,
            bytesKey = RC4.convert(key);

        for (let i = 0, ii = K.length; i < ii; i++) {
            j = (j + K[i] + bytesKey[i % bytesKey.length]) % 256;
            [K[i], K[j]] = [K[j], K[i]];
        }

        return Uint8Array.from(K);
    }
}
