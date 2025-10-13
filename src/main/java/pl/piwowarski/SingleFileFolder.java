package pl.piwowarski;

class SingleFileFolder implements Folder{
    private final String name;
    private final String size;

    public SingleFileFolder(String name, String size) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Folder name cannot be null or blank");
        }
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Folder size cannot be null or blank");
        }

        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSize() {
        return size;
    }
}
