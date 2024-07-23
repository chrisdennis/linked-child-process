/*
 * Copyright 2003-2008 Terracotta, Inc.
 * Copyright Super iPaaS Integration LLC, an IBM Company 2024
 */
package com.tc.lcp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * A test program for {@link LinkedJavaProcessTest}that, in turn, spawns some of
 * its own children as linked processes. The test then kills this process, and
 * makes sure that the children die, too.
 */
public class LinkedJavaProcessTestMain5 {

  public static void main(String[] args) throws Exception {
    File destFile = new File(args[0]);
    boolean spawnChildren = Boolean.parseBoolean(args[1]);

    if (spawnChildren) {
      LinkedJavaProcess child1 = new LinkedJavaProcess(
          LinkedJavaProcessTestMain5.class.getName(), Arrays.asList(args[0]
              + "-child-1", "false"));
      LinkedJavaProcess child2 = new LinkedJavaProcess(
          LinkedJavaProcessTestMain5.class.getName(), Arrays.asList(args[0]
              + "-child-2", "false"));
      child1.start();
      child2.start();
    }

    File stdoutFile = new File(destFile + ".stdout.log");
    System.setOut(new PrintStream(new FileOutputStream(stdoutFile)));

    long start = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < 3 * 60 * 1000) {
      FileOutputStream out = new FileOutputStream(destFile, true);
      out.write("DATA: Just a line of text.\n".getBytes());
      out.flush();
      out.close();

      Thread.sleep(100);
    }
  }

}
