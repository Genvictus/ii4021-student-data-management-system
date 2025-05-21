import express from "express"
export const userRouter = express.Router();

const users = ["john", "bob", "alice"]

/* GET users listing. */
userRouter.get('/', (req, res, next) => {
  res.send(users);
});

userRouter.get('/empty', (req, res, next) => {
  res.send([])
})