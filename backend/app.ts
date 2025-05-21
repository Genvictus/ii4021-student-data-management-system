import express from "express";
import "dotenv/config"

import { userRouter } from './routes/users';

const PORT = 8080
const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use('/users', userRouter);

app.listen(PORT, (error) => {
    if (!error)
        console.log("Server is running on port " + PORT)
    else
        console.log("Error occurred, server can't start", error);
}
);