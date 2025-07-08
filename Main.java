import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        // Создаем три экземпляра GameProgress
        GameProgress progress1 = new GameProgress(100, 3, 1, 0.0);
        GameProgress progress2 = new GameProgress(85, 5, 2, 125.5);
        GameProgress progress3 = new GameProgress(30, 8, 5, 450.7);

        // Сохраняем объекты в файлы
        String savePath1 = "C://savegames/save1.dat";
        String savePath2 = "C://savegames/save2.dat";
        String savePath3 = "C://savegames/save3.dat";

        saveGame(savePath1, progress1);
        saveGame(savePath2, progress2);
        saveGame(savePath3, progress3);

        // Упаковываем файлы в архив
        String zipPath = "C://savegames/saves.zip";
        zipFiles(zipPath, List.of(savePath1, savePath2, savePath3));

        // Удаляем исходные файлы
        deleteFiles(List.of(savePath1, savePath2, savePath3));
    }

    public static void saveGame(String filePath, GameProgress progress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
            System.out.println("Сохранение успешно создано: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : filesToZip) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("Файл не найден: " + filePath);
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(filePath)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    System.out.println("Файл добавлен в архив: " + filePath);
                } catch (IOException e) {
                    System.err.println("Ошибка при добавлении файла в архив: " + e.getMessage());
                }
            }
            System.out.println("Архив успешно создан: " + zipPath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании архива: " + e.getMessage());
        }
    }

    public static void deleteFiles(List<String> filesToDelete) {
        for (String filePath : filesToDelete) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Файл успешно удален: " + filePath);
            } else {
                System.err.println("Не удалось удалить файл: " + filePath);
            }
        }
    }
}
