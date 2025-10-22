package pl.piwowarski;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileCabinetUniqueNamesTest {

    @Test
    void given_valid_input_should_create_file_cabinet_object() {
        // given
        Map<String, Folder> input = new LinkedHashMap<>();
        input.put("Documents", new SingleFileFolder("Documents", FolderSize.MEDIUM.toString()));
        input.put("MediaData", new SingleFileFolder("MediaData", FolderSize.SMALL.toString()));

        // when
        FileCabinetUniqueNames cabinet = new FileCabinetUniqueNames(input);

        // then
        Assertions.assertNotNull(cabinet);
    }

    @Test
    void given_folder_is_null_should_throw_exception() {
        // given
        Map<String, Folder> input = new LinkedHashMap<>();
        input.put("Documents", null);
        // when

        // then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileCabinetUniqueNames(input));
    }

    @Test
    void given_folder_name_does_not_correspond_should_throw_exception() {
        // given
        Map<String, Folder> input = new LinkedHashMap<>();
        input.put("DummyFolderWrongName", new SingleFileFolder("DummyFolder", FolderSize.SMALL.toString()));

        // when

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FileCabinetUniqueNames(input));
    }

    @Test
    void given_map_should_retain_insertion_order() {
        // given
        Map<String, Folder> input = new LinkedHashMap<>();
        input.put("Document", new SingleFileFolder("Document", FolderSize.SMALL.toString()));
        input.put("MediaFile", new SingleFileFolder("MediaFile", FolderSize.MEDIUM.toString()));
        input.put("AudioFile", new SingleFileFolder("AudioFile", FolderSize.SMALL.toString()));

        FileCabinetUniqueNames fileCabinetUniqueNames = new FileCabinetUniqueNames(input);
        // when
        List<String> outputOrder = input.keySet().stream()
                .map(fileName -> fileCabinetUniqueNames.findFolderByName(fileName)
                        .orElseThrow(() -> new AssertionError("Folder not found: " + fileName))
                        .getName()).toList();


        // then
        assertEquals(List.of("Document", "MediaFile", "AudioFile"), outputOrder);
    }
}
