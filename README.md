# File Cabinet Traversor
Zadanie w Javie implementujące strukturę katalogów z uwzględnieniem obsługi folderów zagnieżdżonych.

## Opis implementacji

`FileCabinet` implementuje interfejs `Cabinet` i umożliwia:  

- wyszukiwanie folderów po nazwie (`findFolderByName`)  - w specyfikacji zadania metoda zwraca Optional<Folder> wieć wiadomo że chodzi o pojedyńcza wartość, która może być albo nie być nie musi rzucać wyjątku RuntimeException. 
  Przy wyszukiwania jest to normalne że pliku o jakiejs nazwie moze nie byc i użytkownik będzie chciał od razu wyszukać ponownie.
  Nie ma potrzeby, aby metoda rzucała RuntimeException w przypadku, gdy folder o podanej nazwie nie został znaleziony.
  Brak wyniku w wyszukiwaniu jest sytuacją całkowicie normalną — użytkownik może po prostu spróbować ponownie lub wyszukać inną nazwę.

  RuntimeException należy rzucać jeżeli powstaje jakiś błąd uniemożliwiający dalsze działanie programu.  (np. problem z odczytem danych, brak dostępu do systemu plików itp.).

- wyszukiwanie folderów po rozmiarze (`findFoldersBySize`)  - tutaj prawidłowej wartości są ograniczone do SMALL/MEDIUM/LARGE więc tworzymy pomocniczego enum które implementuje ta weryfikacje, w specyfikacji zwraca List<Folder> więc kolekcja sama z siebie jest w stanie obsłużyć stan pusty, inaczej niż pojedyńcze wartości dla których przeznaczony jest Optional 
- zliczanie wszystkich folderów w strukturze (`count`)  

Obsługiwane są foldery pojedyncze (`Folder`) i wielopoziomowe (`MultiFolder`).

metody korzystają z pomocniczej metody getSubtreeFolders() która zlicza foldery w sposób rekurencyjny i zwraca ich listę

## Struktura

- `Folder` – reprezentacja interfejsowa pojedynczego folderu  
- `MultiFolder` – reprezentacja interfejsowa wielokrotnego folderu  
- `Cabinet` – interfejs główny z metodami operującymi na folderach  
- `FileCabinet` – klasa implementująca `Cabinet`  
