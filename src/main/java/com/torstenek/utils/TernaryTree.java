package com.torstenek.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Ternary Search Tree (http://en.wikipedia.org/wiki/Ternary_search_tree) to efficiently back a suggest / automcomplete.
 * User: torstenek, 10/19/11:20:09.
 */
public class TernaryTree {
    private Node root;

    private void add(String s, String orig, int pos, Node node) {
        final char currentChar = s.charAt(pos);
        if (currentChar < node.character) {
            if (node.left == null) {
                node.left = new Node(currentChar, false);
            }
            add(s, orig, pos, node.left);
        } else if (currentChar > node.character) {
            if (node.right == null) {
                node.right = new Node(currentChar, false);
            }
            add(s, orig, pos, node.right);
        } else {
            if (pos + 1 == s.length()) {
                node.wordEnd = true;
                node.words.add(orig);
            } else {
                if (node.middle == null) {
                    node.middle = new Node(s.charAt(pos + 1), false);
                }
                add(s, orig, pos + 1, node.middle);
            }
        }
    }

    public void add(String s) {
        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Don't add a null or empty string");
        }
        int pos = 0;
        if (root == null) {
            root = new Node(s.charAt(0), false);
        }
        add(s.toLowerCase(), s, pos, root);
    }

    public boolean contains(String s) {
        if (s == null || s.length() == 0) { throw new IllegalArgumentException(); }
        int pos = 0;
        Node node = root;
        while (node != null) {
            final char c = Character.toLowerCase(s.charAt(pos));
            if (c < node.character) { node = node.left; } else if (c > node.character) { node = node.right; } else {
                if (++pos == s.length()) { return node.wordEnd; }
                node = node.middle;
            }
        }
        return false;
    }

    public List<String> getCompletionsFor(String orig) {
        String s = orig.toLowerCase();
        if (s == null || s.length() == 0) { throw new IllegalArgumentException(); }
        int pos = 0;
        Node node = root;
        boolean foundMatch = false;
        while (!foundMatch && node != null) {
            final char c = s.charAt(pos);
            if (c < node.character) {
                node = node.left;
            } else if (c > node.character) {
                node = node.right;
            } else {
                if (++pos == s.length()) {
                    foundMatch = true;
                }
                node = node.middle;
            }
        }
        if (node != null) {
            List<String> completions = new ArrayList<String>();
            collectCompletions(node, completions);
            return completions;
        } else {
            return null;
        }
    }

    private void collectCompletions(Node node, List<String> completions) {
        if (node != null) {
            if (node.wordEnd) {
                completions.addAll(node.words);
            }
            collectCompletions(node.middle, completions);
            collectCompletions(node.left, completions);
            collectCompletions(node.right, completions);
        }
    }

    class Node {
        char character;
        boolean wordEnd;
        Set<String> words = new HashSet<String>();
        Node left, middle, right;

        Node(char character, boolean wordEnd) {
            this.character = character;
            this.wordEnd = wordEnd;
        }
    }

}
