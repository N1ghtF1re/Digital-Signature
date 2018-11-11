<h1 align="center">Digital Signature</h1>
<p align="center"><img src="https://i.imgur.com/ZfMSTrk.png" width=150></p>

<p align="center">
<a href="https://github.com/N1ghtF1re/Digital-Signature/stargazers"><img src="https://img.shields.io/github/stars/N1ghtF1re/Digital-Signature.svg" alt="Stars"></a>
<a href="https://github.com/N1ghtF1re/Digital-Signature/releases"><img src="https://img.shields.io/badge/downloads-4-brightgreen.svg" alt="Total Downloads"></a>
<a href="https://github.com/N1ghtF1re/Digital-Signature/releases"><img src="https://img.shields.io/github/tag/N1ghtF1re/Digital-Signature.svg" alt="Latest Stable Version"></a>
<a href="https://github.com/N1ghtF1re/Digital-Signature/blob/master/LICENSE"><img src="https://img.shields.io/github/license/N1ghtF1re/Digital-Signature.svg" alt="License"></a>
</p>
</p>

## About the library
The library contains contains RSA digital signature algorithm

## Class Elgamal: 

### Constructors: 
- **RSASignature(BigInteger p, BigInteger q, BigInteger privateexp, CryptoHash hashFunction)** 
  - p,q - prime number
  - privateexp - private exponent, 1 < p < (p-1)(q-1), gcd(privateexp, (p-1)(q-1)) = 1, 
  - hashFunction - The object to get the hash function (Must implement the interface CryptoHash)
     

### Methods: 
- **BigInteger sign(byte[] message)** - Sign an array of bytes
  - return signature = hash(message)^d mod r


- static boolean checkSignature(RSAPublicKey key, BigInteger signature, byte[] message, CryptoHash hashFunction) - Verifies signature validity



