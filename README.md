# AndroidHex-AutoNFC 📱💾

**AndroidHex-AutoNFC** is a specialized hexadecimal manipulation utility for Android. This personal project was developed to automate the editing of 32-character hex strings that follow a specific redundancy and integrity pattern, commonly found in low-level data structures such as NFC card sectors or memory dumps.

## ⚙️ The Transformation Logic

The application is designed to handle a 16-byte (32-character) hex string divided into four 8-character blocks. It automates the complex process of parsing, calculating, and reconstructing these blocks.

`[ Block A ] [ Block B ] [ Block C ] [ Block D ]`

### 1. Data Structure Rules
* **Block A**: The primary value stored in **Little Endian** format.
* **Block B**: The bitwise inverse (**NOT**) of Block A, acting as a checksum/integrity byte.
* **Block C**: A redundant copy of **Block A**.
* **Block D**: Static data or padding (ignored by the arithmetic logic).

### 2. Arithmetic Algorithm
When adding a value $N$ through the UI, the app performs the following:
1.  **Endianness Conversion**: Extracts **Block A** and converts it from Little Endian Hex to a Decimal integer.
2.  **Weighted Addition**: As per the specific system requirements this tool targets, the input $N$ is doubled. The final calculation is: 
    $$Value_{new} = Value_{old} + (N \times 2)$$
3.  **String Reconstruction**: 
    * Converts the result back to **Little Endian Hex** (New Block A).
    * Computes the **Bitwise NOT** of the result (New Block B).
    * Syncs the redundant copy (New Block C).
4.  **Output**: Generates the final valid 32-character string ready for use.

## 🌟 Key Features
* **Automated Integrity**: No need to manually calculate bitwise inverses; the app ensures Block B is always mathematically correct.
* **Endianness Management**: Handles the swap between human-readable decimal values and the Little Endian hex format used by the hardware.
* **Minimalist UI**: Focused on speed and efficiency for field testing and data editing.

## 📁 Project Structure
* **`app/src/main/java/`**: Core logic for string parsing, Little Endian conversion, and bitwise operations.
* **`app/src/main/res/layout/`**: Clean interface with dedicated inputs for the raw hex string and the units to be added.

## 🛠️ Built With
* **Android Studio**: Development environment.
* **Java/Kotlin**: Programming language for the implementation of the logic and Android framework.

## 👤 Author
* **Luis Carmona** - [eLeCe2611](https://github.com/eLeCe2611)

---
*This is a personal utility tool created for specialized data manipulation.*
