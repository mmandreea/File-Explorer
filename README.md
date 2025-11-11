A lightweight **file manager** built in **Java(Swing)** that allows users to browse directories,
view partitions, open and read text files, rename files, and delete files. 

## Features

**Graphical Interface** - Built using Java Swing for a simple, responsive UI
**Partition Detection** - Automatically detects and lists disk partitions.
**File Operations** - Supports file deletion, renaming, and navigation.
**Folder Navigation** - Double-click to open folders or move back to parent directories.
**File Viewer** - Displays the content of text files in a pop-up window.
**Safe Deletion** - Includes confirmation prompts before deleting files.

<img width="797" height="537" alt="image" src="https://github.com/user-attachments/assets/bd06933e-4905-41ea-ac63-0afa89e334d6" />


## Project Structure
📁 FileManager
│
├── 📄 App.java # Main GUI application
│
├── 📁 partitions/
│ └── GetPartitions.java # Handles detection and display of disk partitions
│
└── 📁 fileContent/
└── FileContent.java # Handles reading content from files


## User Interface Overview

- **Top Toolbar:** Displays all available disk partitions (e.g., `C:\`, `D:\`, etc.).
- **Center Table:** Lists files and folders in the current directory with columns:
  - **Name**
  - **Type** (File or Folder)
  - **Size**
- **Bottom Buttons:**
  - 🗑️ **Delete:** Removes the selected file after confirmation.
  - ✏️ **Rename:** Opens a dialog to rename the selected file.
  - 🔙 **Go Back:** Navigates to the parent folder.

- **Double-Click Actions:**
  - Folder → Opens it.
  - File → Displays its content (if a readable text file).
 
  <img width="792" height="573" alt="image" src="https://github.com/user-attachments/assets/0522e497-063f-4e50-9ba3-4b73bb7455e6" />
