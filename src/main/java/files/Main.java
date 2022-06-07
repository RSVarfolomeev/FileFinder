package files;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static ArrayList<FilePath> arrayList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String[] os = System.getProperty("os.name").split(" ");
        if (args.length == 0 && os[0].equalsIgnoreCase("Windows")) {
//      Код для запуска консоли в Windows при двойном клике мышкой по jar-файлу
            Runtime.getRuntime().exec("cmd.exe /c start java -jar " +
                    (new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()))
                            .getAbsolutePath() + " cmd");
        } else {
            System.out.println("Введите путь к обрабатываемой директории:");
            try (Scanner scanner = new Scanner(System.in)) {
                Path checkedPath;
                boolean checkedPathResult;
                do {
                    checkedPath = Path.of(scanner.nextLine());
                    checkedPathResult = Files.isDirectory(checkedPath);
                    if (checkedPathResult) {
                        checkDirectory(checkedPath);
                    } else {
                        System.out.println("Путь к директории введен неверно." +
                                " Проверьте правильность написания и попробуйте еще раз.");
                    }
                } while (!checkedPathResult);

                Collections.sort(arrayList);

                StringBuilder stringBuilder = new StringBuilder();
                for (FilePath filePath : arrayList) {
                    try (BufferedReader bufferedReader = Files.newBufferedReader(filePath.getAbsolutePath())) {
                        while (bufferedReader.ready()) {
                            stringBuilder.append("\n").append(bufferedReader.readLine());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String tempString = stringBuilder.substring(1);

                try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(checkedPath +
                        "\\SumFile.txt"))) {
                    bufferedWriter.write(tempString);
                    System.out.println("Создан общий текстовый файл: " + checkedPath + "\\SumFile.txt");
                    System.out.println("--------------------------------------------------------------------------------");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Нажмите \"Enter\" для завершения программы...");
                scanner.nextLine();
            }
        }
    }

    private static String getFileExtension(Path path) {
        String pathName = path.getFileName().toString();
        if (pathName.lastIndexOf(".") != -1 && pathName.lastIndexOf(".") != 0) {
            return pathName.substring(pathName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private static void checkDirectory(Path checkedDirectory) {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(checkedDirectory)) {
            for (Path path : files) {
                if (Files.isRegularFile(path) && getFileExtension(path).equals("txt")) {
                    arrayList.add(new FilePath(path, path.getFileName()));
                }
                if (Files.isDirectory(path)) {
                    checkDirectory(path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}