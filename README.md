Hoggar
=========

[![Build Status](https://travis-ci.org/beajeanm/hoggar.svg?branch=master)](https://travis-ci.org/beajeanm/hoggar)

An Ocaml language plugin for IntelliJ IDEA. It uses merlin for most of it's heavy lifting.
This plugin is base on a fork of [ocaml-ide](https://github.com/sidharthkuruvila/ocaml-ide) 

### Features

 * Syntax highlighting.
 * Auto-completion.
 * Got to declaration of value.

The merlin based features require a .merlin file to work.

Development
------------------

### Install merlin

    opam switch 4.05.0
    opam install merlin ocamlformat ocp-indent

### Run the plugin from gradle

    ./gradlew runIdea

### Build the plugin

    ./gradlew buildPlugin
    
The plugin will be located at build/distributions/hoggar-##version##-SNAPSHOT.zip

It should be possible to install the plugin on IntelliJ IDEA versions 2017.1 and later.