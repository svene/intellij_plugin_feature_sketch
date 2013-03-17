# 'Feature Sketch': Plugin for IntelliJ Idea

## Purpose
In chapter 20 'This class is too big and I do not want it to get any bigger' of his book 'Working effectively with Legacy Code'
Michael Feathers describes what he calls feature sketches.
The idea is that by having a visual representation of the dependencies inside a single class it should be easier to find
out how to split it into more meaningful parts.

## Prerequisites
Since the tool creates input text for the graphviz tool to visualize the feature sketch you
need to have graphviz installed.

## Installation

* clone this git repo
* open the IntelliJ project, build it (which also produces the plugin's jar artifact)
* intall the plugin from disk from the file `out/artifacts/feature_sketch_plugin_jar/feature_sketch_plugin.jar`

## Usage
* Open any Java class, put the cursor somewhere in the editor and press CTRL-ALT-G
* In the tab 'Dependency Viewer' the dependencies for the class should appear in text form.
* Copy this text into a file 'feature_sketch.dot'
* Run graphviz on the file like this for example:
    * dot -Tpng -o feature_sketch.png feature_sketch.dot
    * dot -Tsvg -o feature_sketch.svg feature_sketch.dot
    * circo -Tsvg -o feature_sketch.svg feature_sketch.dot

and open the output file in a browser for example.

## Status
Early alpha.
Things to be done:

* Provide binary installation from: jetbrains plugin repo or bintray
* Display diagram inside IDE similar to UML diagram

## Notes
This is the first IntelliJ Idea plugin I developed. I hope to be able to improve many pieces of the code
when I have learned more about plugin development.

Please let me know about bugs and problems you encounter and any improvement suggestions.

Thank you.

Sven




