import { expect, test } from 'vitest'
import BN from 'bn.js'
import { Polynomial } from '../src/lib/polynomial'

test('Polynomial f(x) evaluates correctly mod m', () => {
    // f(x) = 1 + 2x + 3x² mod 5
    const coefficients = [
        new BN(1),
        new BN(2),
        new BN(3)
    ]
    const mod = new BN(5)
    const poly = new Polynomial(coefficients, mod)

    const x = new BN(2)
    // Expected:
    // f(2) = 1 + 2*2 + 3*4 = 1 + 4 + 12 = 17 mod 5 = 17 % 5 = 2
    const result = poly.f(x)

    expect(result.toString()).toBe('2')
})
