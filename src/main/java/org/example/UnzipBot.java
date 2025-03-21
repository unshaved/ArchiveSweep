package org.example;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipBot {

    // Enable/disable supported extensions
    private static final boolean ENABLE_ZIP = true;
    private static final boolean ENABLE_RAR = true;
    private static final boolean ENABLE_7Z = true;

    public static void main(String[] args) {
        String sourceFolderPath = "C:\\Users\\test\\Downloads"; // Change to your ZIP source folder
        String destinationFolderPath = "C:\\Users\\test\\Downloads\\2025"; // Change to your destination folder

        File sourceFolder = new File(sourceFolderPath);
        File destinationFolder = new File(destinationFolderPath);

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        File[] archiveFiles = sourceFolder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return (ENABLE_ZIP && lowerName.endsWith(".zip")) ||
                    (ENABLE_RAR && lowerName.endsWith(".rar")) ||
                    (ENABLE_7Z && lowerName.endsWith(".7z"));
        });

        if (archiveFiles != null) {
            for (File file : archiveFiles) {
                String lowerName = file.getName().toLowerCase();
                if (lowerName.endsWith(".zip") && ENABLE_ZIP) {
                    unzipZip(file, destinationFolder);
                } else if (lowerName.endsWith(".rar") && ENABLE_RAR) {
                    unzipRar(file, destinationFolder);
                } else if (lowerName.endsWith(".7z") && ENABLE_7Z) {
                    unzip7z(file, destinationFolder);
                }
            }
        } else {
            System.out.println("No archive files found in the source folder.");
        }
    }

    private static void unzipZip(File zipFile, File destinationFolder) {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile.toPath()))) {
            String folderName = zipFile.getName().replaceFirst("\\.zip$", "");
            File outputDir = new File(destinationFolder, folderName);
            outputDir.mkdirs();

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path outputPath = Paths.get(outputDir.getAbsolutePath(), entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    Files.createDirectories(outputPath.getParent());
                    Files.copy(zis, outputPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
            System.out.println("Unzipped: " + zipFile.getName());
        } catch (IOException e) {
            System.err.println("Failed to unzip ZIP file: " + zipFile.getName());
            e.printStackTrace();
        }
    }

    private static void unzipRar(File rarFile, File destinationFolder) {
        try (Archive archive = new Archive(rarFile)) {
            String folderName = rarFile.getName().replaceFirst("\\.rar$", "");
            File outputDir = new File(destinationFolder, folderName);
            outputDir.mkdirs();

            FileHeader fileHeader;
            while ((fileHeader = archive.nextFileHeader()) != null) {
                if (fileHeader.isDirectory()) continue;

                File outFile = new File(outputDir, fileHeader.getFileNameString().trim());
                outFile.getParentFile().mkdirs();
                try (OutputStream os = new FileOutputStream(outFile)) {
                    archive.extractFile(fileHeader, os);
                }
            }
            System.out.println("Unrarred: " + rarFile.getName());
        } catch (Exception e) {
            System.err.println("Failed to unrar file: " + rarFile.getName());
            e.printStackTrace();
        }
    }

    private static void unzip7z(File sevenZFile, File destinationFolder) {
        try (SevenZFile sevenZ = new SevenZFile(sevenZFile)) {
            String folderName = sevenZFile.getName().replaceFirst("\\.7z$", "");
            File outputDir = new File(destinationFolder, folderName);
            outputDir.mkdirs();

            SevenZArchiveEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = sevenZ.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;

                File outFile = new File(outputDir, entry.getName());
                outFile.getParentFile().mkdirs();
                try (FileOutputStream out = new FileOutputStream(outFile)) {
                    int len;
                    while ((len = sevenZ.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                }
            }
            System.out.println("Extracted: " + sevenZFile.getName());
        } catch (IOException e) {
            System.err.println("Failed to extract 7z file: " + sevenZFile.getName());
            e.printStackTrace();
        }
    }
}