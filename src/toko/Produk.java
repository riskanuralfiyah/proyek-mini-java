package toko;

import java.io.*;
import java.util.*;

public class Produk {
    private String kode;
    private String nama;
    private int stok;
    private double hargaBeli;
    private double hargaJual;
    private List<Produk> daftarProduk;
    private final String FILE_PRODUK = "produk.txt";

    public Produk() {
        daftarProduk = new ArrayList<>();
        loadProduk();
    }

    public Produk(String kode, String nama, int stok, double hargaBeli, double hargaJual) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
    }

    public List<Produk> getDaftarProduk() {
        return daftarProduk;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public int getStok() {
        return stok;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public void lihatProduk() {
        System.out.println("\n+----------------------------------------------------------------------------------+");
        System.out.println("|                                   Daftar Produk                                  |");
        System.out.println("+-----+------------+----------------------+------------+-------------+-------------+");
        System.out.println("| No  | Kode       | Nama                 | Stok       | Harga Beli  | Harga Jual  |");
        System.out.println("+-----+------------+----------------------+------------+-------------+-------------+");

        int no = 1;
        for (Produk produk : daftarProduk) {
            System.out.printf("| %-3d | %-10s | %-20s | %-10d | %-11.2f | %-11.2f |%n", 
                              no++, produk.getKode(), produk.getNama(), produk.getStok(), produk.getHargaBeli(), produk.getHargaJual());
        }

        System.out.println("+-----+------------+----------------------+------------+-------------+-------------+");
    }

    private void loadProduk() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PRODUK))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                daftarProduk.add(new Produk(data[0], data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4])));
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat produk: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Format angka salah pada produk: " + e.getMessage());
        }
    }
    
    public void updateStok(String kode, int jumlah) {
        for (Produk produk : daftarProduk) {
            if (produk.getKode().equals(kode)) {
                if (produk.getStok() >= jumlah) {  // Pastikan stok cukup sebelum mengurangi
                    produk.setStok(produk.getStok() - jumlah);
                    saveProduk();  // Simpan perubahan stok ke file
                } else {
                    System.out.println("Stok produk tidak mencukupi untuk kode: " + kode);
                }
                return;
            }
        }
        System.out.println("Produk dengan kode " + kode + " tidak ditemukan.");
    }

    public void tambahProduk(String kode, String nama, int stok, double hargaBeli, double hargaJual) {
        Produk produk = new Produk(kode, nama, stok, hargaBeli, hargaJual);
        daftarProduk.add(produk);
        saveProduk();
    }

    public void tambahProduk() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan Kode Produk : ");
        String kode = scanner.nextLine();
        System.out.print("Masukkan Nama Produk : ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Stok Produk : ");
        int stok = Integer.parseInt(scanner.nextLine());
        System.out.print("Masukkan Harga Beli  : ");
        double hargaBeli = Double.parseDouble(scanner.nextLine());
        System.out.print("Masukkan Harga Jual  : ");
        double hargaJual = Double.parseDouble(scanner.nextLine());

        Produk produk = new Produk(kode, nama, stok, hargaBeli, hargaJual);
        daftarProduk.add(produk);
        saveProduk();

        System.out.println("\nProduk berhasil ditambahkan.");
    }

    public void ubahProduk(String kode) {
        for (Produk produk : daftarProduk) {
            if (produk.getKode().equals(kode)) {
                System.out.println("Produk lama:");
                System.out.println("Kode Produk: " + produk.getKode());
                System.out.println("Nama Produk: " + produk.getNama());
                System.out.println("Stok Produk: " + produk.getStok());
                System.out.println("Harga Beli: " + produk.getHargaBeli());
                System.out.println("Harga Jual: " + produk.getHargaJual());

                Scanner scanner = new Scanner(System.in);

                System.out.print("Masukkan nama baru (kosongkan jika tidak ingin mengubah): ");
                String namaBaru = scanner.nextLine();
                if (!namaBaru.isEmpty()) {
                    produk.nama = namaBaru;
                }

                System.out.print("Masukkan stok baru (kosongkan jika tidak ingin mengubah): ");
                String stokBaru = scanner.nextLine();
                if (!stokBaru.isEmpty()) {
                    produk.stok = Integer.parseInt(stokBaru);
                }

                System.out.print("Masukkan harga beli baru (kosongkan jika tidak ingin mengubah): ");
                String hargaBeliBaru = scanner.nextLine();
                if (!hargaBeliBaru.isEmpty()) {
                    produk.hargaBeli = Double.parseDouble(hargaBeliBaru);
                }

                System.out.print("Masukkan harga jual baru (kosongkan jika tidak ingin mengubah): ");
                String hargaJualBaru = scanner.nextLine();
                if (!hargaJualBaru.isEmpty()) {
                    produk.hargaJual = Double.parseDouble(hargaJualBaru);
                }

                saveProduk();
                System.out.println("Produk berhasil diubah.");
                return;
            }
        }
        System.out.println("Produk dengan kode " + kode + " tidak ditemukan.");
    }

    public void hapusProduk(String kode) {
        Iterator<Produk> iterator = daftarProduk.iterator();
        while (iterator.hasNext()) {
            Produk produk = iterator.next();
            if (produk.getKode().equals(kode)) {
                iterator.remove();
                saveProduk();
                System.out.println("Produk berhasil dihapus.");
                return;
            }
        }
        System.out.println("Produk dengan kode " + kode + " tidak ditemukan.");
    }

    public void saveProduk() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRODUK))) {
            for (Produk produk : daftarProduk) {
                writer.write(produk.getKode() + "," + produk.getNama() + "," + produk.getStok() + "," + produk.getHargaBeli() + "," + produk.getHargaJual());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan produk: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Produk produkManager = new Produk();  // Inisialisasi Produk
        Scanner scanner = new Scanner(System.in);

    }
}
