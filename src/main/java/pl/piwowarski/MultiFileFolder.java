package pl.piwowarski;

import java.util.List;

class MultiFileFolder extends SingleFileFolder implements MultiFolder{
    private final List<Folder> folders;

    public MultiFileFolder(String name, String size, List<Folder> folders) {
        super(name, size);
        this.folders = folders;
    }

    @Override
    public List<Folder> getFolders() {
        return folders;
    }
}
