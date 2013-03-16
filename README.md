# 'Feature Sketch': Plugin for IntelliJ Idea

## Purpose
In chapter '' of his book 'Legacy Code' Michael Feathers describes what he calls feature sketches.
The idea is that by having a visual representation of the cohesion of a single class it should be easier to find
out how to split it into more meaningful parts.

## Prerequisites
Since the tool creates input text for the graphviz tool to visualize the feature sketch you
need to have graphviz installed.

## Installation
Either
a) Install the plugin as binary from: TBD (jetbrains plugin repo | bintray | ...)
b) clone this git repo, open the IntelliJ project, compile it and intall the plugin from disk from
the file 'out/artifacts/feature_sketch_plugin_jar/feature_sketch_plugin.jar

## Usage
- Open any Java class, put the cursor somewhere in the editor and press CTRL-ALT-G
- In the tab 'Dependency Viewer' the dependencies for the class should appear in text form.
- Copy this text into a file 'feature_sketch.dot'
- Run graphviz on the file like this for example:
-- dot -Tpng -o feature_sketch.png feature_sketch.dot
-- dot -Tsvg -o feature_sketch.svg feature_sketch.dot
-- circo -Tsvg -o feature_sketch.svg feature_sketch.dot

and open the output file in a browser for example.

## Status
Early alpha

## Notes
This is the first IntelliJ Idea plugin I developed. I hope to be able to improve many pieces of the code
when I have learned more about plugin development.

Please let me know about bugs and problems you encounter and any improvement suggestions.

Thank you.

Sven




