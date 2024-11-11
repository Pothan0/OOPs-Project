import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAExample {

    private BigInteger n, d, e;
    private int bitLength = 1024; // Key length

    // Constructor to generate public and private keys
    public RSAExample() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, random);
        n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitLength / 2, random);
        while (phi.gcd(e).intValue() > 1) {
            e = BigInteger.probablePrime(bitLength / 2, random);
        }
        d = e.modInverse(phi);
    }

    // Encrypt method
    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    // Decrypt method
    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(d, n);
    }

    // Method to read input from file, encrypt it, and write to output file
    public void encryptFile(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             FileWriter writer = new FileWriter(outputFilePath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                BigInteger message = new BigInteger(line.getBytes());
                BigInteger encrypted = encrypt(message);
                writer.write(encrypted.toString() + "\n");
            }
            System.out.println("Encryption completed. Output written to " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read encrypted data from file, decrypt it, and write to output file
    public void decryptFile(String inputFilePath, String outputFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             FileWriter writer = new FileWriter(outputFilePath)) {

            String line;
            while ((line = reader.readLine()) != null) {
                BigInteger encrypted = new BigInteger(line);
                BigInteger decrypted = decrypt(encrypted);
                writer.write(new String(decrypted.toByteArray()) + "\n");
            }
            System.out.println("Decryption completed. Output written to " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RSAExample rsa = new RSAExample();

        String inputFile = "input.txt"; // The file with plain text to be encrypted
        String encryptedFile = "encrypted_output.txt"; // Where encrypted text will be saved
        String decryptedFile = "decrypted_output.txt"; // Where decrypted text will be saved

        // Encrypt the input file and write to encrypted output file
        rsa.encryptFile(inputFile, encryptedFile);

        // Decrypt the encrypted file and write to decrypted output file
        rsa.decryptFile(encryptedFile, decryptedFile);
    }
}
