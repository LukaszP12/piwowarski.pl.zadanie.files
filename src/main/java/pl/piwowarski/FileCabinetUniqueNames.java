package pl.piwowarski;

import java.util.LinkedHashMap;
import java.util.List;
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

            if (folder == null) {
                throw new IllegalArgumentException("Folder cannot be null");
            }

            if (folder.getName() == null || folder.getName().isBlank()) {
                throw new IllegalArgumentException("Folder name cannot be null or blank");
            }

            if (!folder.getName().equals(key)) {
                throw new IllegalArgumentException(
                        "Map key '" + key + "' must equal folder.getName(): '" + folder.getName() + "'"
                );
            }

            if (this.folders.containsKey(folder.getName())) {
                throw new IllegalArgumentException("Folder name cannot be duplicated");
            }

            this.folders.put(key, folder);
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
        return List.of();
    }

    @Override
    public int count() {
        return 0;
    }
}
