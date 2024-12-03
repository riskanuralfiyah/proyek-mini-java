package toko;

import java.io.*;
import java.util.*;

public class Pembayaran {
    private Produk produkManager;
    private final String FILE_PEMBAYARAN = "pembayaran.txt";

    // Konstruktor untuk menginisialisasi produkManager
    public Pembayaran(Produk produkManager) {
        this.produkManager = produkManager;
    }

    // Mengambil harga produk berdasarkan kode
    public double getHargaProduk(String kode) {
        List<Produk> produkList = produkManager.getDaftarProduk();
        for (Produk produk : produkList) {
            if (produk.getKode().equals(kode)) {
                return produk.getHargaJual();
            }
        }
        return 0;
    }

    // Menu pembayaran
    public void menuPembayaran() {
        Scanner scanner = new Scanner(System.in);
        List<String> daftarBarang = new ArrayList<>();
        List<Integer> jumlahBarang = new ArrayList<>();
        String pilihan;
        double totalHarga = 0;

        do {
            System.out.print("Masukkan Kode Produk: ");
            String idProduk = scanner.nextLine();
            System.out.print("Masukkan Jumlah: ");
            int jumlah = scanner.nextInt();
            scanner.nextLine(); // consume newline

            double hargaProduk = getHargaProduk(idProduk);
            if (hargaProduk > 0) {
                daftarBarang.add(idProduk);
                jumlahBarang.add(jumlah);
                totalHarga += hargaProduk * jumlah;
            } else {
                System.out.println("Produk dengan kode " + idProduk + " tidak ditemukan.");
            }

            System.out.print("Apakah ingin menambah barang lain? (y/n): ");
            pilihan = scanner.nextLine();
        } while (pilihan.equalsIgnoreCase("y"));

        System.out.printf("Total Harga: %.2f%n", totalHarga);

        System.out.print("Masukkan Uang Bayar: ");
        double uangBayar = scanner.nextDouble();
        double kembalian = uangBayar - totalHarga;

        if (kembalian < 0) {
            System.out.printf("Uang tidak cukup! Kekurangan: %.2f%n", -kembalian);
        } else {
            System.out.printf("Kembalian: %.2f%n", kembalian);
            System.out.println("Pembayaran berhasil.");

            // Simpan transaksi ke file
            simpanTransaksi(daftarBarang, jumlahBarang, totalHarga);

            // Cetak struk
            cetakStruk(daftarBarang, jumlahBarang, totalHarga, uangBayar, kembalian);

            // Update stok produk
            for (int i = 0; i < daftarBarang.size(); i++) {
                String kode = daftarBarang.get(i);
                int jumlah = jumlahBarang.get(i);
                produkManager.updateStok(kode, jumlah); // Kurangi stok produk
            }
        }
    }

    // Simpan transaksi ke file
    private void simpanTransaksi(List<String> daftarBarang, List<Integer> jumlahBarang, double totalHarga) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PEMBAYARAN, true))) { // Append mode
            writer.write("Transaksi:");
            writer.newLine();
            writer.write(String.format("%-10s %-20s %-10s %-15s", "Kode", "Nama", "Jumlah", "Harga Satuan"));
            writer.newLine();
            
            for (int i = 0; i < daftarBarang.size(); i++) {
                String kode = daftarBarang.get(i);
                int jumlah = jumlahBarang.get(i);
                double hargaSatuan = getHargaProduk(kode);
                Produk produk = produkManager.getDaftarProduk().stream()
                                            .filter(p -> p.getKode().equals(kode))
                                            .findFirst()
                                            .orElse(null);

                String namaProduk = (produk != null) ? produk.getNama() : "Unknown";
                writer.write(String.format("%-10s %-20s %-10d %-15.2f", kode, namaProduk, jumlah, hargaSatuan));
                writer.newLine();
            }
            
            writer.write(String.format("\nTotal Harga: %.2f", totalHarga));
            writer.newLine();
            writer.write("----------------------------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Cetak struk
    private void cetakStruk(List<String> daftarBarang, List<Integer> jumlahBarang, double totalHarga, double uangBayar, double kembalian) {
        System.out.println("\n+----------------------------------------------------------------+");
        System.out.println("|                        STRUK PEMBAYARAN                        |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.println("| Kode  | Nama Produk     | Jumlah | Harga Satuan |  Harga Total |");
        System.out.println("+----------------------------------------------------------------+");

        for (int i = 0; i < daftarBarang.size(); i++) {
            String kode = daftarBarang.get(i);
            Produk produk = produkManager.getDaftarProduk().stream()
                                          .filter(p -> p.getKode().equals(kode))
                                          .findFirst()
                                          .orElse(null);

            if (produk != null) {
                int jumlah = jumlahBarang.get(i);
                double hargaSatuan = produk.getHargaJual();
                double hargaTotal = hargaSatuan * jumlah;
                System.out.printf("| %-5s | %-15s | %-6d | %-12.2f | %-12.2f |%n", 
                                  kode, produk.getNama(), jumlah, hargaSatuan, hargaTotal);
            }
        }

        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("| Total Harga: %-49.2f |\n", totalHarga);
        System.out.printf("| Uang Bayar : %-49.2f |\n", uangBayar);
        System.out.printf("| Kembalian  : %-49.2f |\n", kembalian);
        System.out.println("+----------------------------------------------------------------+");
    }

    // Method main untuk menjalankan aplikasi
    public static void main(String[] args) {
        Produk produkManager = new Produk();  // Inisialisasi Produk
        Pembayaran pembayaran = new Pembayaran(produkManager);
        pembayaran.menuPembayaran();
    }
}
