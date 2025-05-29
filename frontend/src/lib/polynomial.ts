import BN from 'bn.js';

export class Polynomial {
    private _coefficients: BN[]
    private _mod: BN

    constructor(coefficients: BN[], mod: BN) {
        this._coefficients = coefficients
        this._mod = mod
    }

    // f(x: BN): BN {
    //     let result = this._coefficients[0].umod(this._mod)
    //     let xPower = new BN(1)

    //     for (let i = 1; i < this._coefficients.length; i++) {
    //         xPower = xPower.mul(x).umod(this._mod) // x^i mod m
    //         const term = this._coefficients[i].mul(xPower).umod(this._mod)
    //         result = result.add(term).umod(this._mod)
    //     }

    //     return result
    // }

    // Horner's method
    f(x: BN): BN {
        let result = this._coefficients[this._coefficients.length - 1].umod(this._mod)

        for (let i = this._coefficients.length - 2; i >= 0; i--) {
            result = result.mul(x).add(this._coefficients[i]).umod(this._mod)
        }

        return result
    }
}
