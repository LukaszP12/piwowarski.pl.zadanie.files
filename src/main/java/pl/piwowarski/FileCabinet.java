package pl.piwowarski;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        if (folders == null) {
            throw new IllegalArgumentException("Folder list cannot be null");
        }
        this.folders = List.copyOf(folders);
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder size must be valid, it cannot be null or blank");
        }

        return folders.stream()
                .flatMap(folder -> getSubtreeFolders(folder).stream())
                .filter(f -> f.getName().equals(name))
                .findFirst();
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

        return folders.stream()
                .flatMap(folder -> getSubtreeFolders(folder).stream())
                .filter(folder -> folderSize.name().equalsIgnoreCase(folder.getSize()))
                .toList();
    }

    @Override
    public int count() {
        return folders.stream()
                .flatMap(folder -> getSubtreeFolders(folder).stream())
                .mapToInt(countingFolder -> 1)
                .sum();
    }

    private List<Folder> getSubtreeFolders(Folder folder) {
        List<Folder> allFolders = new ArrayList<>();
        allFolders.add(folder);
        if (folder instanceof MultiFolder) {
            for (Folder sub : ((MultiFolder) folder).getFolders()) {
                allFolders.addAll(getSubtreeFolders(sub));
            }
        }
        return allFolders;
    }
}
