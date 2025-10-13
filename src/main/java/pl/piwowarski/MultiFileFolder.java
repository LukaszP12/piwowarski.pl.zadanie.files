package pl.piwowarski;

import java.util.List;
import java.util.Objects;

class MultiFileFolder extends SingleFileFolder implements MultiFolder{
    private final List<Folder> folders;

    public MultiFileFolder(String name, String size, List<Folder> folders) {
        super(name, size);
        if (folders == null){
            this.folders = List.of();
        }else {
            this.folders = folders.stream()
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    @Override
    public List<Folder> getFolders() {
        return folders;
    }
}
