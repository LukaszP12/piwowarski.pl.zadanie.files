package pl.piwowarski;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void given_method_FoldersBySize_should_return_matches_including_nested() {
        // given
        Folder audioFile = new SingleFileFolder("AudioFile", "SMALL");
        Folder videoFile = new SingleFileFolder("VideoFile", "LARGE");
        Folder subtitleFile = new SingleFileFolder("SubtitleFile", "SMALL");
        Folder mediaFolder = new MultiFileFolder("MediaFolder", "MEDIUM", List.of(videoFile, subtitleFile));

        Folder documents = new SingleFileFolder("Documents", "SMALL");

        Map<String, Folder> base = new LinkedHashMap<>();
        base.put(mediaFolder.getName(), mediaFolder);
        base.put(audioFile.getName(), audioFile);
        base.put(documents.getName(), documents);

        FileCabinetUniqueNames cabinet = new FileCabinetUniqueNames(base);

        // when
        List<Folder> results = cabinet.findFoldersBySize("SMALL");

        // then
        assertEquals(3, results.size());
        List<String> foldersNames = results.stream().map(Folder::getName).toList();
        assertTrue(foldersNames.containsAll(List.of("AudioFile", "SubtitleFile", "Documents")));
    }

    @Test
    void given_number_of_folders_including_nested_should_return_total_number() {
        // given
        SingleFileFolder audioFile = new SingleFileFolder("AudioFile", "SMALL");
        SingleFileFolder videoFile = new SingleFileFolder("VideoFile", "MEDIUM");
        MultiFileFolder multiFileFolder = new MultiFileFolder("MultiMediaFile", "LARGE", List.of(audioFile, videoFile));

        Folder documents = new SingleFileFolder("Documents", "SMALL");
        Folder financial = new SingleFileFolder("Financialsheets", "SMALL");

        Map<String, Folder> base = new LinkedHashMap<>();
        base.put(multiFileFolder.getName(), multiFileFolder);
        base.put(documents.getName(), documents);
        base.put(financial.getName(), financial);

        FileCabinetUniqueNames cabinet = new FileCabinetUniqueNames(base);

        // when
        int countedFolders = cabinet.count();

        // then
        assertEquals(5, countedFolders);
    }
}
