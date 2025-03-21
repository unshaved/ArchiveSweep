# ArchiveSweep

**ArchiveSweep** is a lightweight and efficient Java-based utility for bulk extracting `.zip`, `.rar`, and `.7z` archives. It's designed to simplify the process of organizing and unpacking multiple archive files at once, each into its own dedicated folder.

---

## Features

- **Supports multiple archive formats:** `.zip`, `.rar`, `.7z`
- **Auto-extracts into named folders** matching the archive filename
- ⚙**Selective format control** — enable/disable support for specific archive types
- **Automatic directory handling** — creates destination folders if they don’t exist
- **Smart path management** — works cross-platform with Java’s native file system

---

## How It Works

1. You define:
   - A **source folder** (where your archives are located)
   - A **destination folder** (where extracted files will go)
2. ArchiveSweep scans the source for `.zip`, `.rar`, or `.7z` files.
3. Each archive is unpacked into its own subfolder inside the destination directory.
4. You’ll see real-time logs in the console for all extraction progress or errors.

---

## 🔧 Setup

### 1. Clone & Build

Make sure you have Java 8+ installed.

```dependencies {
    implementation 'com.github.junrar:junrar:7.5.4'
    implementation 'net.sf.sevenzipjbinding:sevenzipjbinding-all-platforms:16.02-2.01'
}
