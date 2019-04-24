package controller;

import model.TestFile;

import java.io.File;
import java.util.List;

public interface FileParser {
  List<TestFile> parseFile(File f);
}
