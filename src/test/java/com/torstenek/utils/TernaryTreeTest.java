package com.torstenek.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * User: torstenek, 3/4/12:08:56
 */
public class TernaryTreeTest {
    TernaryTree tree, tree2;


        @Test
        public void testStuff() throws Exception {

            tree = new TernaryTree();
            tree.add("ABCD");
            tree.add("AB");
            tree.add("ABBA");
            tree.add("BCD");

            // Test some simple stuff
            Assert.assertTrue(tree.contains("AB"));
            Assert.assertFalse(tree.contains("ABB"));

            List<String> completions;
            completions = tree.getCompletionsFor("A");
            Assert.assertTrue(completions.size() == 3);
            completions = tree.getCompletionsFor("AB");
            Assert.assertTrue(completions.size() == 2);

            completions = tree.getCompletionsFor("B");
            Assert.assertTrue(completions.size() == 1);
            completions = tree.getCompletionsFor("JFEJFFE");
            Assert.assertNull(completions);

            // Make sure we are Case Insensitive
            tree2 = new TernaryTree();
            tree2.add("Kalle Anka");
            final String cc1 = "thisIsCamelCase";
            tree2.add(cc1);
            tree2.add("kallE ankA");
            completions = tree2.getCompletionsFor("KA");
            Assert.assertEquals(completions.size(), 2);
            final String cc2 = "tHisiSCamelCaS";
            Assert.assertEquals(cc1.toLowerCase(), cc2.toLowerCase() + "e");
            completions = tree2.getCompletionsFor(cc2);
            Assert.assertEquals(completions.size(), 1);
            Assert.assertEquals(completions.get(0), "thisIsCamelCase");

            // Throw some random strings at it
            tree = new TernaryTree();
            for (int i = 0; i < 10000; i++) {
                String uuid = UUID.randomUUID().toString();
                tree.add(uuid);
                Assert.assertTrue(tree.contains(uuid));
            }
        }
}
