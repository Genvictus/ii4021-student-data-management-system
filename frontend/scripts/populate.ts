import { register } from "../src/use-cases/auth/register";
import { createStudentTranscript } from "../src/use-cases/transcripts/createStudentTranscript";
import { generatePrime } from "../src/lib/random";
import { encrypt, generateKeyPair, privateKeyToString, publicKeyToString, RsaPrivateKey } from "../src/lib/rsa";
import { writeFile } from "fs";
import path from "path";

const keyPath = path.join(__dirname, "..", "generated", "pk-rsa.txt");

const [p, q] = await Promise.all([
    generatePrime(1024),
    generatePrime(1024),
]);

const { publicKey, privateKey } = generateKeyPair(p, q);
const publicKeyString = publicKeyToString(publicKey);

const hod = {
    userId: "13500000",
    email: "hod135@email.com",
    password: "passhod135",
    fullName: "Yudistira Dwi",
    role: "HOD",
    publicKey: publicKeyString,
    departmentId: "135",
}

const supervisor = {
    userId: "13500001",
    email: "supervisor135@email.com",
    password: "passsup135",
    fullName: "Rinaldi Munir",
    role: "SUPERVISOR",
    publicKey: publicKeyString,
    departmentId: "135",
}

const student = {
    userId: "13599001",
    email: "student135@email.com",
    password: "passstd135",
    fullName: "Michael Leon",
    role: "SUPERVISOR",
    publicKey: publicKeyString,
    departmentId: "135",
}

const transcript = {
    transcriptId: "123456789",
    student: student,
    studentId: student.userId,
    transcriptStatus: "PENDING",
    transcriptData: [
        {
            courseCode: "II4021",
            credits: 3,
            score: "A"
        }
    ],
    hodId: hod.userId,
    hodDigitalSignature: null,
    encryptedKey: null
}

async function populateUser() {
    register(hod);
    register(supervisor);
    register(student);

    writeFile(keyPath, privateKeyToString(privateKey), "utf8", (err) => {
        console.error('Error writing file:', err);
        return;
    });

    return privateKey;
}

async function populateTranscript() {
    createStudentTranscript(transcript, "secret_key");
}

function main() {
    populateUser();
    populateTranscript();
}

main();