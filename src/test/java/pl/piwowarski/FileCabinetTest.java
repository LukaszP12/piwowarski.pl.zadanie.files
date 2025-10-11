package pl.piwowarski;


import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileCabinetTest {

    @Test
    void should_throw_exception_if_input_list_is_null() {
        assertThrows(IllegalArgumentException.class, () -> new FileCabinet(null));
    }

    @Test
    void findFolderByName_an_exisiting_folder() {
        // given
        Folder file1 = new SingleFileFolder("File1", "SMALL");
        Folder file2 = new SingleFileFolder("File2", "MEDIUM");

        FileCabinet fileCabinet = new FileCabinet(List.of(file1, file2));
        // when
        Optional<Folder> retrievedFile = fileCabinet.findFolderByName("File1");

        // then
        assertTrue(retrievedFile.isPresent());
        assertEquals("File1", retrievedFile.get().getName());
    }

    @Test
    void findFolderByName_a_nonExistingFolder() {
        // given
        SingleFileFolder singleFileFolder = new SingleFileFolder("dummyFile1", "SMALL");
        FileCabinet fileCabinet = new FileCabinet(List.of(singleFileFolder));

        // when
        Optional<Folder> notPresent = fileCabinet.findFolderByName("NonExistent");

        // then
        assertTrue(notPresent.isEmpty());
    }

    @Test
    void find_folder_of_small_size() {
        // given
        Folder file1 = new SingleFileFolder("dummyFile1", "SMALL");
        Folder file2 = new SingleFileFolder("dummyFile2", "MEDIUM");
        MultiFileFolder multiFileFolder = new MultiFileFolder("dummyMultiFileFolder1", "LARGE", List.of(file1, file2));
        FileCabinet fileCabinet = new FileCabinet(List.of(multiFileFolder));

        // when
        List<Folder> allSmallFolders = fileCabinet.findFoldersBySize("SMALL");

        // then
        assertEquals(1, allSmallFolders.size());
        assertEquals("dummyFile1", allSmallFolders.get(0).getName());
    }

    @Test
    void find_aMultiFolder_By_Size_Polymorphic() {
        // given
        Folder file1 = new SingleFileFolder("dummyFile1", "SMALL");
        Folder file2 = new SingleFileFolder("dummyFile2", "MEDIUM");
        MultiFileFolder multiFileFolder = new MultiFileFolder("dummyFolder1", "LARGE", List.of(file1, file2));
        FileCabinet fileCabinet = new FileCabinet(List.of(multiFileFolder));

        // when
        List<Folder> largeSizeFolders = fileCabinet.findFoldersBySize("LARGE");

        // then
        assertEquals(1, largeSizeFolders.size());
        assertEquals("dummyFolder1", largeSizeFolders.get(0).getName());
    }

    @Test
    void test_counting_folders_all_single_file_folders() {
        // given
        Folder file1 = new SingleFileFolder("dummyFile1", "SMALL");
        Folder file2 = new SingleFileFolder("dummyFile2", "MEDIUM");
        Folder file3 = new SingleFileFolder("dummyFile2", "MEDIUM");
        Folder file4 = new SingleFileFolder("dummyFile2", "LARGE");

        FileCabinet fileCabinet = new FileCabinet(List.of(file1, file2, file3, file4));
        // when

        // then
        assertEquals(4, fileCabinet.count());
    }

    @Test
    void test_counting_folders_single_file_folders_and_multiple_folder() {
        // given
        Folder folder1 = new SingleFileFolder("dummyFile1", "SMALL");
        Folder folder2 = new SingleFileFolder("dummyFile2", "MEDIUM");
        Folder folder3 = new SingleFileFolder("dummyFile2", "MEDIUM");
        Folder folder4 = new SingleFileFolder("dummyFile2", "LARGE");
        MultiFileFolder multiFileFolder = new MultiFileFolder("dummyMultiFolder", "LARGE", List.of(folder1, folder2, folder3, folder4));

        FileCabinet fileCabinet = new FileCabinet(List.of(multiFileFolder));
        // when

        // then
        assertEquals(5, fileCabinet.count());
    }


    @Test
    void emptyCabinet_hasZeroCountAndReturnsEmptyForSearches(){
        // given
        FileCabinet empty = new FileCabinet(List.of());

        // when

        // then
        assertEquals(0, empty.count());
        assertTrue(empty.findFolderByName("Any").isEmpty());
        assertTrue(empty.findFoldersBySize("SMALL").isEmpty());
    }

    @Test
    void testCount() {
        // given
        Folder folder1 = new SingleFileFolder("File1", "SMALL");
        Folder folder2 = new SingleFileFolder("File2", "MEDIUM");
        MultiFolder multi = new MultiFileFolder("Folder1", "LARGE", List.of(folder1, folder2));

        // when
        FileCabinet cabinet = new FileCabinet(List.of(multi));

        // then
        assertEquals(3, cabinet.count());
    }

    @Test
    void testing_if_nested_folders_are_counted_correctly_when_traversed(){
        // given
        Folder folder1 = new SingleFileFolder("File1", "SMALL");
        Folder folder2 = new SingleFileFolder("File2", "MEDIUM");
        Folder folder3 = new SingleFileFolder("File3", "MEDIUM");
        MultiFileFolder folder4 = new MultiFileFolder("File4", "LARGE", List.of(folder1, folder2,folder3));
        MultiFileFolder baseFolder = new MultiFileFolder("baseRoot", "LARGE", List.of(folder4));

        // when
        FileCabinet fileCabinet = new FileCabinet(List.of(baseFolder));

        // then
        assertEquals(5,fileCabinet.count());
    }

    @Test
    void testing_if_nested_folders_are_counted_correctly_when_traversed_more_levels(){
        // given
        Folder folder1 = new SingleFileFolder("File1", "SMALL");
        Folder folder2 = new SingleFileFolder("File2", "MEDIUM");
        Folder folder3 = new SingleFileFolder("File3", "MEDIUM");

        Folder folder6 = new SingleFileFolder("File1", "SMALL");
        Folder folder5 = new MultiFileFolder("File5", "LARGE",List.of(folder6));

        MultiFileFolder folder4 = new MultiFileFolder("File4", "LARGE", List.of(folder1, folder2,folder3,folder5));

        MultiFileFolder baseFolder = new MultiFileFolder("baseRoot", "LARGE", List.of(folder4));

        // when
        FileCabinet fileCabinet = new FileCabinet(List.of(baseFolder));

        // then
        assertEquals(7,fileCabinet.count());
    }

    @Test
    void testing_finding_folders_with_duplicate_names(){
        // given
        Folder folder1 = new SingleFileFolder("Duplicated", "SMALL");
        Folder folder2 = new SingleFileFolder("Duplicated", "MEDIUM");
        FileCabinet fileCabinet = new FileCabinet(List.of(folder1, folder2));

        // when
        Optional<Folder> retrievedFile = fileCabinet.findFolderByName("Duplicated");

        // then
        assertTrue(retrievedFile.isPresent());
    }

    @Test
    void testing_finding_folder_by_name_when_given_invalid_inputs(){
        // given
        FileCabinet fileCabinet = new FileCabinet(List.of());
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> fileCabinet.findFolderByName(null));
        assertThrows(IllegalArgumentException.class, () -> fileCabinet.findFolderByName(""));
    }

    @Test
    void testing_finding_folder_by_size_when_given_invalid_inputs(){
        // given
        FileCabinet fileCabinet = new FileCabinet(List.of());
        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> fileCabinet.findFoldersBySize(null));
        assertThrows(IllegalArgumentException.class, () -> fileCabinet.findFoldersBySize(""));
        assertThrows(IllegalArgumentException.class, () -> fileCabinet.findFoldersBySize("EXTREMELY LARGE"));
    }

    @Test
    void testing_empty_multi_folder(){
        // given
        MultiFileFolder anEmptyFolder = new MultiFileFolder("EmptyFolder", "LARGE", List.of());
        FileCabinet fileCabinet = new FileCabinet(List.of(anEmptyFolder));

        // when

        // then
        assertEquals(1,fileCabinet.count());
        assertTrue(fileCabinet.findFoldersBySize("LARGE").contains(anEmptyFolder));
        assertTrue(fileCabinet.findFolderByName("EmptyFolder").isPresent());
    }

    // I should verify that FolderSize.valueOf()
    // as well as corresponding name lookups are both case-sensitive.
    @Test
    void testing_if_Case_Sensitivity_is_handled_correctly(){
        // given
        SingleFileFolder folder1 = new SingleFileFolder("File1", "MEDIUM");
        FileCabinet fileCabinet = new FileCabinet(List.of(folder1));
        // when & then
        assertThrows(IllegalArgumentException.class,() -> fileCabinet.findFoldersBySize("medium"));

        // then
        assertTrue(fileCabinet.findFolderByName("file1").isEmpty());
    }
}
