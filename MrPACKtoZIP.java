import java.io.*;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MrpackToZipConverter {

    public static void main(String[] args) {
        // Pilih file mrpack
        String mrpackPath = chooseFilePath("Pilih File Mrpack", "mrpack");
        
        if (mrpackPath == null) {
            System.out.println("Pemilihan file dibatalkan.");
            return;
        }
        
        // Pilih lokasi untuk menyimpan file zip
        String zipPath = chooseSaveFilePath("Simpan File Zip", "zip");
        
        if (zipPath == null) {
            System.out.println("Pemilihan lokasi penyimpanan dibatalkan.");
            return;
        }
        
        convertMrpackToZip(mrpackPath, zipPath);
    }

    public static String chooseFilePath(String title, String extension) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File " + extension.toUpperCase(), extension);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle(title);
        
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getPath();
        } else {
            return null;
        }
    }

    public static String chooseSaveFilePath(String title, String extension) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("File " + extension.toUpperCase(), extension);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(filter);
        chooser.setDialogTitle(title);
        
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            if (!path.endsWith("." + extension)) {
                path += "." + extension;
            }
            return path;
        } else {
            return null;
        }
    }

    public static void convertMrpackToZip(String mrpackPath, String zipPath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(mrpackPath));
             ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // Jika entry adalah direktori, buat direktori tersebut di zip
                if (entry.isDirectory()) {
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    zos.closeEntry();
                } else {
                    // Jika entry adalah file, salin kontennya ke zip
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }
            System.out.println("Konversi berhasil!");
        } catch (IOException e) {
            System.err.println("Error mengkonversi mrpack ke zip: " + e.getMessage());
        }
    }
}
