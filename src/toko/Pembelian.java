package toko;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


public class Pembelian {
    private String id_pembelian;
    private String nama_sup;
    private String nama_brg;
    private int jumlah_brg;
    private double hargaSatuan;
    private double totalHarga;
    private List<Pembelian> daftarPembelian;
    private final String FILE_PEMBELIAN = "pembelian.txt";
    private Produk produkManager;

    public Pembelian(Produk produkManager) {
        this.produkManager = produkManager;
        daftarPembelian = new ArrayList<>();
        loadPembelian(); // Memuat data pembelian saat inisialisasi
    }

    public Pembelian(String id_pembelian, String nama_sup, String nama_brg, int jumlah_brg, double hargaSatuan, double totalHarga, Produk produkManager) {
        this.id_pembelian = id_pembelian;
        this.nama_sup = nama_sup;
        this.nama_brg = nama_brg;
        this.jumlah_brg = jumlah_brg;
        this.hargaSatuan = hargaSatuan;
        this.totalHarga = totalHarga;
        this.produkManager = produkManager;
    }

    public String getIdPembelian() {
        return id_pembelian;
    }

    public String getNamaSup() {
        return nama_sup;
    }

    public String getNamaBrg() {
        return nama_brg;
    }

    public int getJumlahBrg() {
        return jumlah_brg;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void lihatPembelian() {
        loadPembelian(); // Pastikan data terbaru dimuat
        System.out.println("\n+---------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                               Daftar Pembelian                                                |");
        System.out.println("+-----+--------------+-----------------------+-------------------+--------------+---------------+---------------+");
        System.out.println("| No  | ID Pembelian | Nama Supplier         | Nama Barang       | Jumlah       | Harga Satuan  |  Total Harga  |");
        System.out.println("+-----+--------------+-----------------------+-------------------+--------------+---------------+---------------+");

        int no = 1;
        for (Pembelian pembelian : daftarPembelian) {
            System.out.printf("| %-3d | %-12s | %-21s | %-17s | %-12d | %-13.2f | %-13.2f |%n",
                              no++, pembelian.getIdPembelian(), pembelian.getNamaSup(), pembelian.getNamaBrg(), pembelian.getJumlahBrg(), pembelian.getHargaSatuan(), pembelian.getTotalHarga());
        }

        System.out.println("+---------------------------------------------------------------------------------------------------------------+");
    }

    public void tambahPembelian() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan ID Pembelian: ");
        String id_pembelian = scanner.nextLine();
        System.out.print("Masukkan Nama Supplier: ");
        String nama_sup = scanner.nextLine();
        System.out.print("Masukkan Nama Barang: ");
        String nama_brg = scanner.nextLine();
        System.out.print("Masukkan Jumlah Barang: ");
        int jumlah_brg = scanner.nextInt();
        System.out.print("Masukkan Harga Satuan: ");
        double hargaSatuan = scanner.nextDouble();
        System.out.print("Masukkan Harga Jual: ");
        double hargaJual = scanner.nextDouble();
        double totalHarga = jumlah_brg * hargaSatuan;
        System.out.printf("Total Harga: %.2f%n", totalHarga);
        System.out.println("\nPembelian berhasil ditambahkan.");
        Pembelian pembelian = new Pembelian(id_pembelian, nama_sup, nama_brg, jumlah_brg, hargaSatuan, totalHarga, produkManager);
        daftarPembelian.add(pembelian);

        // Update stok produk
        updateStokProduk(nama_brg, jumlah_brg, hargaSatuan, hargaJual);

        savePembelian(); // Simpan data ke file
    }

    private void updateStokProduk(String namaBarang, int jumlahBarang, double hargaSatuan, double hargaJual) {
        boolean produkDitemukan = false;
        for (Produk produk : produkManager.getDaftarProduk()) {
            if (produk.getNama().equalsIgnoreCase(namaBarang)) {
                produk.setStok(produk.getStok() + jumlahBarang);
                produk.setHargaJual(hargaJual);
                produkDitemukan = true;
                break;
            }
        }

        if (!produkDitemukan) {
            System.out.print("Produk tidak ditemukan dalam daftar produk. Tambahkan produk baru? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("y")) {
                System.out.print("Masukkan kode produk baru: ");
                String kode = scanner.nextLine();
                produkManager.tambahProduk(kode, namaBarang, jumlahBarang, hargaSatuan, hargaJual);
            }
        }

        produkManager.saveProduk(); // Simpan perubahan stok ke file produk
    }

    public void ubahPembelian(String idPembelianUbah) {
        loadPembelian(); // Pastikan data terbaru dimuat
        for (Pembelian pembelian : daftarPembelian) {
            if (pembelian.getIdPembelian().equals(idPembelianUbah)) {
                Scanner scanner = new Scanner(System.in);

                System.out.print("Masukkan Nama Supplier baru (Tekan Enter jika tidak ingin diubah: " + pembelian.getNamaSup() + "): ");
                String nama_sup = scanner.nextLine();
                if (nama_sup.isEmpty()) {
                    nama_sup = pembelian.getNamaSup();
                }

                System.out.print("Masukkan Nama Barang baru (Tekan Enter jika tidak ingin diubah: " + pembelian.getNamaBrg() + "): ");
                String nama_brg = scanner.nextLine();
                if (nama_brg.isEmpty()) {
                    nama_brg = pembelian.getNamaBrg();
                }

                System.out.print("Masukkan Jumlah Barang baru (Tekan Enter jika tidak ingin diubah: " + pembelian.getJumlahBrg() + "): ");
                String jumlah_brg_input = scanner.nextLine();
                int jumlah_brg;
                if (jumlah_brg_input.isEmpty()) {
                    jumlah_brg = pembelian.getJumlahBrg();
                } else {
                    jumlah_brg = Integer.parseInt(jumlah_brg_input);
                }

                System.out.print("Masukkan Harga Satuan baru (Tekan Enter jika tidak ingin diubah: " + pembelian.getHargaSatuan() + "): ");
                String hargaSatuan_input = scanner.nextLine();
                double hargaSatuan;
                if (hargaSatuan_input.isEmpty()) {
                    hargaSatuan = pembelian.getHargaSatuan();
                } else {
                    hargaSatuan = Double.parseDouble(hargaSatuan_input);
                }

                double totalHarga = jumlah_brg * hargaSatuan;
                System.out.printf("Total Harga: %.2f%n", totalHarga);

                pembelian.nama_sup = nama_sup;
                pembelian.nama_brg = nama_brg;
                pembelian.jumlah_brg = jumlah_brg;
                pembelian.hargaSatuan = hargaSatuan;
                pembelian.totalHarga = totalHarga;

                savePembelian();
                System.out.println("\nData pembelian berhasil diubah.");
                break;
            }
        }
    }

    public void hapusPembelian(String IdPembelianHapus) {
        loadPembelian(); // Pastikan data terbaru dimuat
        Pembelian pembelianDihapus = null;
        for (Pembelian pembelian : daftarPembelian) {
            if (pembelian.getIdPembelian().equals(IdPembelianHapus)) {
                pembelianDihapus = pembelian;
                break;
            }
        }
        if (pembelianDihapus != null) {
            daftarPembelian.remove(pembelianDihapus);
            System.out.println("Pembelian dengan ID " + IdPembelianHapus + " telah dihapus.");
            savePembelian();
        } else {
            System.out.println("Pembelian dengan ID " + IdPembelianHapus + " tidak ditemukan.");
        }
    }

    private void savePembelian() {
        Locale locale = Locale.US; // Menetapkan Locale untuk memastikan format titik
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat df = new DecimalFormat("#.00", symbols);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PEMBELIAN))) {
            for (Pembelian pembelian : daftarPembelian) {
                writer.write(pembelian.getIdPembelian() + "," + pembelian.getNamaSup() + ","
                        + pembelian.getNamaBrg() + "," + pembelian.getJumlahBrg() + ","
                        + df.format(pembelian.getHargaSatuan()) + ","
                        + df.format(pembelian.getTotalHarga()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadPembelian() {
        daftarPembelian.clear(); // Clear existing data before loading
        Locale locale = Locale.US; // Menetapkan Locale untuk memastikan parsing titik
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        DecimalFormat df = new DecimalFormat("#.00", symbols);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PEMBELIAN))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    String id_pembelian = data[0];
                    String nama_sup = data[1];
                    String nama_brg = data[2];
                    int jumlah_brg = Integer.parseInt(data[3]);
                    double hargaSatuan = Double.parseDouble(data[4]);
                    double totalHarga = Double.parseDouble(data[5]);

                    Pembelian pembelian = new Pembelian(id_pembelian, nama_sup, nama_brg, jumlah_brg, hargaSatuan, totalHarga, produkManager);
                    daftarPembelian.add(pembelian);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
