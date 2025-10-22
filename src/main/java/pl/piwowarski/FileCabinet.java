package pl.piwowarski;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        if (folders == null) {
            throw new IllegalArgumentException("Folder list cannot be null");
        }
        this.folders = List.copyOf(folders.stream()
                .filter(Objects::nonNull)
                .toList());
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder size must be valid, it cannot be null or blank");
        }

        return getFolders()
                .filter(folder -> name.equals(folder.getName()))
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

        return getFolders()
                .filter(folder -> folderSize.name().equalsIgnoreCase(folder.getSize()))
                .toList();
    }

    @Override
    public int count() {
        return getFolders().mapToInt(countEachFolder -> 1).sum();
    }

    // streams are always not mutable I do not need Collections.unmodifiableList() any more
    public Stream<Folder> getFolders() {
        return folders.stream().flatMap(this::getSubFolders);
    }

    private Stream<Folder> getSubFolders(Folder folder) {
        Stream<Folder> rootFolder = Stream.of(folder);
        if (folder instanceof MultiFolder) {
            Stream<Folder> subFolders = ((MultiFolder) folder)
                    .getFolders()
                    .stream()
                    .flatMap(this::getSubFolders);
            return Stream.concat(rootFolder, subFolders);
        }
        return rootFolder;
    }
}
