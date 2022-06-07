package files;

import java.nio.file.Path;

public class FilePath implements Comparable<FilePath> {
    private final Path absolutePath;
    private final Path fileName;

    public FilePath(Path absolutePath, Path fileName) {
        this.absolutePath = absolutePath;
        this.fileName = fileName;
    }

    public Path getAbsolutePath() {
        return absolutePath;
    }

    public Path getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "FilePath{" +
                "absolutePath=" + absolutePath +
                ", fileName=" + fileName +
                '}';
    }

    @Override
    public int compareTo(FilePath otherPath) {
        return fileName.toString().compareTo(otherPath.getFileName().toString());
    }
}
