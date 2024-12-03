package toko;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Produk produkManager = new Produk();
        Pembayaran pembayaran = new Pembayaran(produkManager);
        Pembelian pembelian = new Pembelian(produkManager); // Asumsi Anda memiliki kelas Pembelian yang sudah diimplementasikan
        Laporan laporan = new Laporan(); // Menambahkan instance Laporan

        System.out.println("\n˚₊‧꒰า ☆ ໒꒱ ‧₊˚ Selamat datang di Toko Lolipopski ˚₊‧꒰า ☆ ໒꒱ ‧₊˚");

        while (true) {
            System.out.println("\n+---------------------------+");
            System.out.println("|         Menu Utama        |");
            System.out.println("+---------------------------+");
            System.out.println("| 1 | Produk                |");
            System.out.println("| 2 | Pembelian             |");
            System.out.println("| 3 | Pembayaran            |");
            System.out.println("| 4 | Laporan               |");
            System.out.println("| 5 | Keluar                |");
            System.out.println("+---------------------------+");
            System.out.print("Pilih menu (1-5): ");

            int pilihan;
            try {
                pilihan = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Pilihan tidak valid. Silakan pilih menu (1-5).");
                continue;
            }

            switch (pilihan) {
                case 1:
                    // Menu Produk
                    while (true) {
                        System.out.println("\n+---------------------------+");
                        System.out.println("|         Menu Produk       |");
                        System.out.println("+---------------------------+");
                        System.out.println("| 1 | Lihat Daftar Produk   |");
                        System.out.println("| 2 | Tambah Produk         |");
                        System.out.println("| 3 | Ubah Produk           |");
                        System.out.println("| 4 | Hapus Produk          |");
                        System.out.println("| 5 | Kembali ke Menu Utama |");
                        System.out.println("+---------------------------+");
                        System.out.print("Pilih menu (1-5): ");

                        int subPilihan;
                        try {
                            subPilihan = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Pilihan tidak valid. Silakan pilih menu (1-5).");
                            continue;
                        }

                        switch (subPilihan) {
                            case 1:
                                produkManager.lihatProduk(); // Menampilkan daftar produk
                                break;
                            case 2:
                                produkManager.tambahProduk(); // Menambahkan produk
                                break;
                            case 3:
                                System.out.print("\nMasukkan kode produk yang ingin diubah: ");
                                String kodeUbah = scanner.nextLine();
                                produkManager.ubahProduk(kodeUbah); // Mengubah produk
                                break;
                            case 4:
                                System.out.print("Masukkan kode produk yang ingin dihapus: ");
                                String kodeHapus = scanner.nextLine();
                                produkManager.hapusProduk(kodeHapus); // Menghapus produk
                                break;
                            case 5:
                                break; // Kembali ke Menu Utama
                            default:
                                System.out.println("Pilihan tidak valid.");
                        }
                        if (subPilihan == 5) break; // Kembali ke Menu Utama
                    }
                    break;

                case 2:
                    // Menu Pembelian
                    while (true) {
                        System.out.println("\n+----------------------------+");
                        System.out.println("|        Menu Pembelian      |");
                        System.out.println("+----------------------------+");
                        System.out.println("| 1 | Lihat Daftar Pembelian |");
                        System.out.println("| 2 | Tambah Pembelian       |");
                        System.out.println("| 3 | Ubah Pembelian         |");
                        System.out.println("| 4 | Hapus Pembelian        |");
                        System.out.println("| 5 | Kembali ke Menu Utama  |");
                        System.out.println("+----------------------------+");
                        System.out.print("Pilih menu (1-5): ");
                
                        int subPilihan;
                        try {
                            subPilihan = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Pilihan tidak valid. Silakan pilih menu (1-5).");
                            continue;
                        }
                
                        switch (subPilihan) {
                            case 1:
                                pembelian.lihatPembelian(); // Menampilkan daftar pembelian
                                break;
                            case 2:
                                pembelian.tambahPembelian(); // Menambahkan pembelian
                                break;
                            case 3:
                                System.out.print("\nMasukkan ID Pembelian yang ingin diubah: ");
                                String idPembelianUbah = scanner.nextLine();
                                pembelian.ubahPembelian(idPembelianUbah); // Mengubah pembelian
                                break;
                            case 4:
                                System.out.print("Masukkan ID Pembelian yang ingin dihapus: ");
                                String idPembelianHapus = scanner.nextLine();
                                pembelian.hapusPembelian(idPembelianHapus); // Menghapus pembelian
                                break;
                            case 5:
                                break; // Kembali ke Menu Utama
                            default:
                                System.out.println("Pilihan tidak valid.");
                        }
                        if (subPilihan == 5) break; // Kembali ke Menu Utama
                    }
                    break;

                case 3:
                    // Menu Pembayaran
                    pembayaran.menuPembayaran();
                    break;

                case 4:
                    // Menu Laporan
                    laporan.tampilkanLaporan(); // Memanggil laporan
                    break;

                case 5:
                    System.out.println("Terima kasih telah menggunakan sistem kasir Toko Lolipopski ˶ᵔ ᵕ ᵔ˶");
                    scanner.close(); // Menutup scanner
                    return;

                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih menu (1-5).");
            }
        }
    }
}
