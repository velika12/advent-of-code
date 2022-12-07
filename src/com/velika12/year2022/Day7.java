package com.velika12.year2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 {

    private static final Pattern commandPattern = Pattern.compile("\\$ (\\w+)\\s{0,1}([\\w/.]*)");
    private static final Pattern lsDirPattern = Pattern.compile("dir (\\w+)");
    private static final Pattern lsFilePattern = Pattern.compile("(\\d+) ([\\w.]+)");

    private static final DeviceFolder root = new DeviceFolder("/", null);

    private static final long FOLDER_MAX_SIZE = 100000;
    private static final long FILE_SYSTEM_SIZE = 70000000;
    private static final long UPDATE_SIZE = 30000000;

    private static DeviceFolder changeCurrentDirectory(String name, DeviceFolder currentDirectory) {
        if (name.equals("/")) {
            return root;
        }

        if (name.equals("..")) {
            return currentDirectory.getParentFolder();
        }

        return currentDirectory.getInnerFolder(name);
    }

    private static void registerFolder(String name, DeviceFolder currentDirectory) {
        if (!currentDirectory.contains(name)) {
            currentDirectory.addFolder(name);
        }
    }

    private static void registerFile(String name, long size, DeviceFolder currentDirectory) {
        if (!currentDirectory.contains(name)) {
            currentDirectory.addFile(name, size);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(new File("input/year2022/input_day7.txt"))) {

            DeviceFolder currentDirectory = root;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Matcher commandMatcher = commandPattern.matcher(line);
                if (commandMatcher.find()) {
                    // command line
                    String command = commandMatcher.group(1);
                    if (command.equals("cd")) {
                        String name = commandMatcher.group(2);
                        currentDirectory = changeCurrentDirectory(name, currentDirectory);
                    }
                } else {
                    // ls output line
                    Matcher lsDirMatcher = lsDirPattern.matcher(line);
                    if (lsDirMatcher.find()) {
                        String name = lsDirMatcher.group(1);
                        registerFolder(name, currentDirectory);
                    }

                    Matcher lsFileMatcher = lsFilePattern.matcher(line);
                    if (lsFileMatcher.find()) {
                        long size = Long.parseLong(lsFileMatcher.group(1));
                        String name = lsFileMatcher.group(2);
                        registerFile(name, size, currentDirectory);
                    }
                }
            }

            System.out.println(root);
            System.out.println();

            long sumSize = calculateSumSizeOfFoldersFittingMaxSize(root, 0);

            System.out.println("Sum of total sizes of directories which are at most 100000: " + sumSize);

            long sizeToFree = UPDATE_SIZE - (FILE_SYSTEM_SIZE - root.getSize());
            DeviceFolder smallestFolderToDelete = findSmallestFolderToDelete(root, root, sizeToFree);

            System.out.println("The smallest directory to delete: " + smallestFolderToDelete.getName() + ", " + smallestFolderToDelete.getSize());
        }
    }

    private static long calculateSumSizeOfFoldersFittingMaxSize(DeviceFolder currentFolder, long sumSize) {
        if (currentFolder.getSize() <= FOLDER_MAX_SIZE) {
            sumSize += currentFolder.getSize();
        }

        for (DeviceFolder innerFolder : currentFolder.getInnerFolders()) {
            sumSize = calculateSumSizeOfFoldersFittingMaxSize(innerFolder, sumSize);
        }

        return sumSize;
    }

    private static DeviceFolder findSmallestFolderToDelete(DeviceFolder currentFolder, DeviceFolder smallestFolder, long sizeToFree) {
        if (currentFolder.getSize() >= sizeToFree && currentFolder.getSize() < smallestFolder.getSize()) {
            smallestFolder = currentFolder;
        }

        for (DeviceFolder innerFolder : currentFolder.getInnerFolders()) {
            smallestFolder = findSmallestFolderToDelete(innerFolder, smallestFolder, sizeToFree);
        }

        return smallestFolder;
    }
}

abstract class DeviceObject {
    protected DeviceFolder parentFolder;
    protected String name;
    protected long size;
    protected int level;

    public DeviceObject(String name, DeviceFolder parentFolder) {
        this.name = name;
        this.parentFolder = parentFolder;
        this.level = parentFolder == null ? 1 : parentFolder.level + 1;
    }

    public DeviceObject(String name, long size, DeviceFolder parentFolder) {
        this(name, parentFolder);
        this.size = size;
    }

    protected abstract String getType();

    @Override
    public String toString() {
        return String.format("%d %" + this.level + "s %s (%s, size=%d)", this.level, "-", this.name, getType(), this.size);
    }
}

class DeviceFolder extends DeviceObject {
    private final Map<String, DeviceObject> content = new HashMap<>();

    public DeviceFolder(String name, DeviceFolder parentFolder) {
        super(name, parentFolder);
    }

    @Override
    protected String getType() {
        return "dir";
    }

    public void addFile(String name, long size) {
        this.content.put(name, new DeviceFile(name, size, this));
        this.size += size;

        DeviceFolder parentFolder = this.parentFolder;
        while (parentFolder != null) {
            parentFolder.size += size;
            parentFolder = parentFolder.parentFolder;
        }
    }

    public void addFolder(String name) {
        this.content.put(name, new DeviceFolder(name, this));
    }

    public boolean contains(String name) {
        return this.content.containsKey(name);
    }

    public DeviceFolder getInnerFolder(String name) {
        DeviceObject result = this.content.get(name);
        if (result instanceof DeviceFolder) {
            return (DeviceFolder) result;
        }

        return null;
    }

    public DeviceFolder getParentFolder() {
        return this.parentFolder;
    }

    public List<DeviceFolder> getInnerFolders() {
        return this.content.values().stream()
                .filter(e -> e instanceof DeviceFolder)
                .map(e -> (DeviceFolder) e)
                .collect(Collectors.toList());
    }

    public long getSize() {
        return this.size;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        this.content.forEach((s, deviceObject) -> sb.append("\n").append(deviceObject.toString()));
        return sb.toString();
    }
}

class DeviceFile extends DeviceObject {
    public DeviceFile(String name, long size, DeviceFolder parentFolder) {
        super(name, size, parentFolder);
    }

    @Override
    protected String getType() {
        return "file";
    }
}
