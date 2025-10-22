package pl.piwowarski;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class FileCabinetUniqueNames implements Cabinet {
    private Map<String, Folder> folders = new LinkedHashMap();

    public FileCabinetUniqueNames(Map<String, Folder> folders) {
        if (folders == null) {
            throw new IllegalArgumentException("Folder list cannot be null");
        }
        for (Map.Entry<String, Folder> entry : folders.entrySet()) {
            String key = entry.getKey();
            Folder folder = entry.getValue();

            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null. ");
            }

            if (folder == null) {
                throw new IllegalArgumentException("Folder cannot be null. ");
            }

            if (!key.equals(folder.getName())) {
                throw new IllegalArgumentException(
                        "Map key '" + key + "' must equal folder.getName(): '" + folder.getName() + "'"
                );
            }

            addWithSubFolders(folder);
        }
    }

    private void addWithSubFolders(Folder folder) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }

        String name = folder.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder name cannot be null or blank");
        }

        if (folders.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate folder name detected: " + name);
        }

        this.folders.put(name, folder);
        if (folder instanceof MultiFolder multiFolder) {
            for (Folder subFolder : multiFolder.getFolders()) {
                addWithSubFolders(subFolder);
            }
        }
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder size must be valid, it cannot be null or blank");
        }

        return Optional.ofNullable(folders.get(name));
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Folder size must be valid, it cannot be null or blank");
        }

        final FolderSize folderSize;
        try {
            folderSize = FolderSize.valueOf(size.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid folder size type: " + size +
                    ". It must be one of corresponding types SMALL/MEDIUM/LARGE.");
        }

        return folders.values().stream()
                .filter(folder -> folderSize.name() == folder.getSize())
                .toList();
    }

    @Override
    public int count() {
        return folders.size();
    }
}
