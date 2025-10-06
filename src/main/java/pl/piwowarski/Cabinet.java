package pl.piwowarski;

import java.util.List;
import java.util.Optional;

public interface Cabinet {
    Optional<Folder> findFolderByName(String name);

    List<Folder> findFoldersBySize(String size);

    int count();
}
