package isa;

import java.util.List;
import java.util.Vector;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class AbstractAssembler {
  public static class AssemblyException extends RuntimeException {
    String description;
    public AssemblyException (String desc) {
      description = desc;
    }
    public String toString () {
      return description;
    }
  }
  public abstract void assembleFile (String filename, Memory memory) throws AssemblyException, FileNotFoundException, IOException;
  public abstract void assembleLine (int address, String label, String statement, String comment, Memory memory) throws AssemblyException;
  public abstract void checkLineSyntax (int address, String label, String statement, String comment, Memory memory) throws AssemblyException;
  public abstract void checkLabelSyntax (String label, Memory memory) throws AssemblyException;
}