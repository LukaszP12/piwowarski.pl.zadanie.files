package pl.piwowarski;

import java.util.List;
import java.util.Optional;

class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet(List<Folder> folders) {
        this.folders = folders;
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder name must not be null or blank");
        }

        return folders.stream()
                .flatMap(folder -> getSubtreeFolders(folder).stream())
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }

}
