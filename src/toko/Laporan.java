package toko;

import java.io.*;
import java.util.*;

public class Laporan {
    private final String FILE_PEMBAYARAN = "pembayaran.txt";

    // Method untuk membaca transaksi dari file dan menampilkan laporan
    public void tampilkanLaporan() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PEMBAYARAN))) {
            String line;
            Map<String, Integer> produkTerjual = new HashMap<>();
            Map<String, Double> hargaProduk = new HashMap<>();
            double totalPenjualan = 0;
            int jumlahTransaksi = 0;
            boolean readingTransaksi = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Transaksi:")) {
                    readingTransaksi = true;
                    jumlahTransaksi++;
                } else if (line.startsWith("Total Harga:")) {
                    if (readingTransaksi) {
                        String[] parts = line.split(" ");
                        String totalStr = parts[2].replace(",", ".");
                        double total = Double.parseDouble(totalStr);
                        totalPenjualan += total;
                        readingTransaksi = false;  // End of current transaction
                    }
                } else if (line.matches("^[A-Z0-9]{4}\\s+.*")) {
                    // Parsing lines that have product details
                    String[] parts = line.split("\\s{4,}");  // Split by at least 4 spaces
                    if (parts.length >= 4) {
                        try {
                            String kode = parts[0].trim();
                            int jumlah = Integer.parseInt(parts[2].trim());
                            double hargaSatuan = Double.parseDouble(parts[3].trim().replace(",", "."));
                            double totalHarga = hargaSatuan * jumlah;

                            produkTerjual.put(kode, produkTerjual.getOrDefault(kode, 0) + jumlah);
                            hargaProduk.put(kode, hargaSatuan);
                        } catch (NumberFormatException e) {
                            // Abaikan baris yang tidak bisa diparse
                        }
                    }
                }
            }

            // Menampilkan laporan dalam format tabel
            System.out.println("\n+----------------------------------------------------------------------+");
            System.out.println("|                            Laporan Penjualan                         |");
            System.out.println("+-----+-------------+----------------+-----------------+---------------+");
            System.out.println("| No  | Kode Produk | Jumlah Terjual |  Harga Satuan   | Total Harga   |");
            System.out.println("+-----+-------------+----------------+-----------------+---------------+");

            int no = 1;
            for (Map.Entry<String, Integer> entry : produkTerjual.entrySet()) {
                String kode = entry.getKey();
                int jumlah = entry.getValue();
                double hargaSatuan = hargaProduk.getOrDefault(kode, 0.0);
                double totalHarga = hargaSatuan * jumlah;

                System.out.printf("| %-3d | %-11s | %-14d | %-15.2f | %-13.2f |%n",
                                  no++, kode, jumlah, hargaSatuan, totalHarga);
            }

            System.out.println("+-----+-------------+----------------+-----------------+---------------+");
            
            // Menampilkan ringkasan laporan
            System.out.printf("Jumlah Transaksi: %d%n", jumlahTransaksi);
            System.out.printf("Total Penjualan: %.2f%n", totalPenjualan);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method main untuk menjalankan laporan
    public static void main(String[] args) {
        Laporan laporan = new Laporan();
        laporan.tampilkanLaporan();
    }
}
